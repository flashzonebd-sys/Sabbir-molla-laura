package com.example.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CompletedLessonEntity
import com.example.data.local.SavedVocabEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.DialogueLine
import com.example.data.model.GrammarLesson
import com.example.data.model.LessonItem
import com.example.data.model.NoraExpression
import com.example.data.model.PronunciationExercise
import com.example.data.model.UserProfile
import com.example.data.model.VocabWord
import com.example.data.remote.GeminiTutorService
import com.example.data.repository.CurriculumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class NoraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val geminiService = GeminiTutorService()

    // State flows
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _noraExpression = MutableStateFlow(NoraExpression.WELCOME)
    val noraExpression: StateFlow<NoraExpression> = _noraExpression.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessageEntity>> = _chatMessages.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading: StateFlow<Boolean> = _isAILoading.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _recognizedSpeech = MutableStateFlow("")
    val recognizedSpeech: StateFlow<String> = _recognizedSpeech.asStateFlow()

    private val _selectedScenario = MutableStateFlow("Meeting Someone New")
    val selectedScenario: StateFlow<String> = _selectedScenario.asStateFlow()

    private val _vocabList = MutableStateFlow<List<VocabWord>>(CurriculumRepository.sampleVocabWords)
    val vocabList: StateFlow<List<VocabWord>> = _vocabList.asStateFlow()

    private val _completedLessonIds = MutableStateFlow<Set<String>>(setOf("lesson_1"))
    val completedLessonIds: StateFlow<Set<String>> = _completedLessonIds.asStateFlow()

    private val _darkThemeEnabled = MutableStateFlow(false)
    val darkThemeEnabled: StateFlow<Boolean> = _darkThemeEnabled.asStateFlow()

    private val _explanationLanguage = MutableStateFlow("English + Bangla Tips")
    val explanationLanguage: StateFlow<String> = _explanationLanguage.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    // Active lesson / quiz state
    private val _activeLesson = MutableStateFlow<LessonItem?>(null)
    val activeLesson: StateFlow<LessonItem?> = _activeLesson.asStateFlow()

    private val _activeGrammarLesson = MutableStateFlow<GrammarLesson?>(null)
    val activeGrammarLesson: StateFlow<GrammarLesson?> = _activeGrammarLesson.asStateFlow()

    private val _pronunciationScore = MutableStateFlow<Pair<Int, String>?>(null) // Score %, feedback
    val pronunciationScore: StateFlow<Pair<Int, String>?> = _pronunciationScore.asStateFlow()

    // Android TTS
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    // Android Speech Recognizer
    private var speechRecognizer: SpeechRecognizer? = null

    init {
        initTts()
        initSpeechRecognizer()
        observeData()
        seedInitialData()
    }

    private fun initTts() {
        tts = TextToSpeech(getApplication()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(_speechRate.value)
                isTtsReady = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                        _noraExpression.value = NoraExpression.SPEAKING
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                        _noraExpression.value = NoraExpression.WELCOME
                    }

                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                        _noraExpression.value = NoraExpression.WELCOME
                    }
                })
            }
        }
    }

    private fun initSpeechRecognizer() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                if (SpeechRecognizer.isRecognitionAvailable(getApplication())) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplication())
                    speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _isListening.value = true
                            _noraExpression.value = NoraExpression.LISTENING
                        }

                        override fun onBeginningOfSpeech() {}
                        override fun onRmsChanged(rmsdB: Float) {}
                        override fun onBufferReceived(buffer: ByteArray?) {}
                        override fun onEndOfSpeech() {
                            _isListening.value = false
                            _noraExpression.value = NoraExpression.THINKING
                        }

                        override fun onError(error: Int) {
                            _isListening.value = false
                            _noraExpression.value = NoraExpression.WELCOME
                            Log.d("NoraSpeech", "Speech error code: $error")
                        }

                        override fun onResults(results: Bundle?) {
                            _isListening.value = false
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            val text = matches?.firstOrNull() ?: ""
                            _recognizedSpeech.value = text
                            if (text.isNotBlank()) {
                                sendMessageToNora(text)
                            } else {
                                _noraExpression.value = NoraExpression.WELCOME
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            _recognizedSpeech.value = matches?.firstOrNull() ?: ""
                        }

                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }
            } catch (e: Exception) {
                Log.e("NoraSpeech", "Failed to init SpeechRecognizer: ${e.message}")
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            db.userProfileDao().getUserProfileFlow().collectLatest { entity ->
                if (entity != null) {
                    _userProfile.value = UserProfile(
                        name = entity.name,
                        englishLevel = entity.englishLevel,
                        primaryGoal = entity.primaryGoal,
                        explanationLanguage = entity.explanationLanguage,
                        dailyGoalMinutes = entity.dailyGoalMinutes,
                        biggestDifficulty = entity.biggestDifficulty,
                        xp = entity.xp,
                        streakDays = entity.streakDays,
                        completedLessonsCount = entity.completedLessonsCount,
                        wordsLearnedCount = entity.wordsLearnedCount,
                        speakingMinutes = entity.speakingMinutes,
                        isOnboarded = entity.isOnboarded
                    )
                    _explanationLanguage.value = entity.explanationLanguage
                }
            }
        }

        viewModelScope.launch {
            db.chatDao().getAllMessagesFlow().collectLatest { messages ->
                _chatMessages.value = messages
            }
        }

        viewModelScope.launch {
            db.vocabDao().getAllVocabFlow().collectLatest { entities ->
                if (entities.isNotEmpty()) {
                    _vocabList.value = entities.map {
                        VocabWord(
                            id = it.id,
                            word = it.word,
                            phonetic = it.phonetic,
                            partOfSpeech = it.partOfSpeech,
                            definition = it.definition,
                            definitionBangla = it.definitionBangla,
                            exampleSentence = it.exampleSentence,
                            category = it.category,
                            isFavorite = it.isFavorite,
                            masteryLevel = it.masteryLevel
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            db.lessonProgressDao().getCompletedLessonsFlow().collectLatest { list ->
                _completedLessonIds.value = list.map { it.lessonId }.toSet() + "lesson_1"
            }
        }
    }

    private fun seedInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = db.userProfileDao().getUserProfile()
            if (existing == null) {
                db.userProfileDao().insertOrUpdate(
                    UserProfileEntity(
                        name = "Alex",
                        englishLevel = "Intermediate",
                        primaryGoal = "Everyday Conversation",
                        explanationLanguage = "English + Bangla Tips",
                        dailyGoalMinutes = 15,
                        biggestDifficulty = "Speaking Confidence",
                        xp = 180,
                        streakDays = 3,
                        completedLessonsCount = 2,
                        wordsLearnedCount = 8,
                        speakingMinutes = 14,
                        isOnboarded = true
                    )
                )
            }

            // Seed sample vocabulary if empty
            val vocabEntities = CurriculumRepository.sampleVocabWords.map {
                SavedVocabEntity(
                    id = it.id,
                    word = it.word,
                    phonetic = it.phonetic,
                    partOfSpeech = it.partOfSpeech,
                    definition = it.definition,
                    definitionBangla = it.definitionBangla,
                    exampleSentence = it.exampleSentence,
                    category = it.category,
                    isFavorite = it.isFavorite,
                    masteryLevel = it.masteryLevel
                )
            }
            db.vocabDao().insertAll(vocabEntities)

            // Seed initial welcome message from Nora if empty
            val initialMsgs = db.chatDao().getAllMessagesFlow()
            if (_chatMessages.value.isEmpty()) {
                db.chatDao().insertMessage(
                    ChatMessageEntity(
                        sender = "nora",
                        text = "Hello Alex! 😊 I'm Nora, your personal English tutor. What would you like to talk about today? Try telling me about your morning or pick a scenario above!",
                        originalSentence = null,
                        correctedSentence = null,
                        correctionExplanation = null
                    )
                )
            }
        }
    }

    fun completeOnboarding(
        name: String,
        level: String,
        goal: String,
        explanationLang: String,
        dailyTarget: Int,
        biggestDifficulty: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.userProfileDao().insertOrUpdate(
                UserProfileEntity(
                    name = name.ifBlank { "Alex" },
                    englishLevel = level,
                    primaryGoal = goal,
                    explanationLanguage = explanationLang,
                    dailyGoalMinutes = dailyTarget,
                    biggestDifficulty = biggestDifficulty,
                    xp = 200,
                    streakDays = 1,
                    completedLessonsCount = 1,
                    wordsLearnedCount = 5,
                    speakingMinutes = 5,
                    isOnboarded = true
                )
            )
            _explanationLanguage.value = explanationLang
        }
    }

    fun setScenario(scenario: String) {
        _selectedScenario.value = scenario
        viewModelScope.launch(Dispatchers.IO) {
            val starterMsg = when (scenario) {
                "Ordering Food at a Café" -> "Welcome to the coffee shop! What can I get for you today?"
                "Job Interview Practice" -> "Good morning! Thank you for coming in today. Could you start by introducing yourself?"
                "Traveling Abroad & Airport" -> "Excuse me, where are you flying to today? May I see your passport and boarding pass?"
                "IELTS Speaking Test" -> "Good afternoon. In this part of the speaking test, I'd like to ask you about your hometown. Could you describe where you grew up?"
                else -> "I'm ready to chat! Tell me what is on your mind or how your day is going."
            }
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    sender = "nora",
                    text = starterMsg,
                    originalSentence = null,
                    correctedSentence = null,
                    correctionExplanation = null
                )
            )
            speakText(starterMsg)
        }
    }

    fun sendMessageToNora(text: String) {
        if (text.isBlank()) return
        val currentProfile = _userProfile.value
        val scenario = _selectedScenario.value
        val includeBangla = _explanationLanguage.value.contains("Bangla")

        viewModelScope.launch(Dispatchers.IO) {
            // Save user message
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    sender = "user",
                    text = text
                )
            )

            _isAILoading.value = true
            _noraExpression.value = NoraExpression.THINKING

            val history = _chatMessages.value.map { it.sender to it.text }
            val noraResponse = geminiService.chatWithNora(
                userMessage = text,
                scenario = scenario,
                level = currentProfile.englishLevel,
                includeBangla = includeBangla,
                conversationHistory = history
            )

            // Save Nora reply
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    sender = "nora",
                    text = noraResponse.reply,
                    originalSentence = if (noraResponse.correctedSentence != null) text else null,
                    correctedSentence = noraResponse.correctedSentence,
                    correctionExplanation = noraResponse.explanation
                )
            )

            // Add speaking minutes & XP
            db.userProfileDao().addSpeakingMinutes(1)

            _isAILoading.value = false
            if (noraResponse.correctedSentence != null) {
                _noraExpression.value = NoraExpression.CORRECTING
            } else {
                _noraExpression.value = NoraExpression.SPEAKING
            }

            speakText(noraResponse.reply)
        }
    }

    fun startVoiceRecording() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak English clearly to Nora...")
            }
            speechRecognizer?.startListening(intent)
            _isListening.value = true
            _noraExpression.value = NoraExpression.LISTENING
        } catch (e: Exception) {
            Log.e("NoraSpeech", "Error starting listening: ${e.message}")
            _isListening.value = false
        }
    }

    fun stopVoiceRecording() {
        speechRecognizer?.stopListening()
        _isListening.value = false
        _noraExpression.value = NoraExpression.WELCOME
    }

    fun speakText(text: String, slow: Boolean = false) {
        if (!isTtsReady) return
        tts?.setSpeechRate(if (slow) 0.75f else _speechRate.value)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "nora_utterance_${System.currentTimeMillis()}")
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
        _noraExpression.value = NoraExpression.WELCOME
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        tts?.setSpeechRate(rate)
    }

    fun toggleVocabFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.vocabDao().toggleFavorite(id)
        }
    }

    fun updateVocabMastery(id: String, level: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.vocabDao().updateMastery(id, level)
            db.userProfileDao().incrementWordsCount()
        }
    }

    fun setActiveLesson(lesson: LessonItem?) {
        _activeLesson.value = lesson
    }

    fun setActiveGrammarLesson(lesson: GrammarLesson?) {
        _activeGrammarLesson.value = lesson
    }

    fun completeLesson(lessonId: String, xpEarned: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.lessonProgressDao().markLessonCompleted(
                CompletedLessonEntity(
                    lessonId = lessonId,
                    scorePercent = 100,
                    xpEarned = xpEarned
                )
            )
            db.userProfileDao().addLessonCompletion(xpEarned)
            _noraExpression.value = NoraExpression.CELEBRATING
        }
    }

    fun testPronunciation(targetSentence: String, spokenSentence: String) {
        val targetWords = targetSentence.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").split(" ").filter { it.isNotBlank() }
        val spokenWords = spokenSentence.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").split(" ").filter { it.isNotBlank() }

        var matchCount = 0
        for (w in spokenWords) {
            if (targetWords.contains(w)) matchCount++
        }

        val totalWords = targetWords.size.coerceAtLeast(1)
        val score = ((matchCount.toFloat() / totalWords.toFloat()) * 100).toInt().coerceIn(0, 100)

        val feedback = when {
            score >= 85 -> "Outstanding! Clear pronunciation and natural rhythm. 🎉"
            score >= 60 -> "Good effort! Practice smooth linking between words. 👍"
            else -> "Keep going! Listen to Nora's sample audio and try speaking with the slower playback. ✨"
        }

        _pronunciationScore.value = Pair(score, feedback)
        if (score >= 80) {
            _noraExpression.value = NoraExpression.CELEBRATING
        } else {
            _noraExpression.value = NoraExpression.ENCOURAGING
        }
    }

    fun clearPronunciationScore() {
        _pronunciationScore.value = null
    }

    fun clearChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            db.chatDao().clearHistory()
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    sender = "nora",
                    text = "Hello! Fresh start. What would you like to practice today?",
                    originalSentence = null,
                    correctedSentence = null,
                    correctionExplanation = null
                )
            )
        }
    }

    fun toggleDarkTheme() {
        _darkThemeEnabled.value = !_darkThemeEnabled.value
    }

    fun setExplanationLanguage(language: String) {
        _explanationLanguage.value = language
        viewModelScope.launch(Dispatchers.IO) {
            val current = db.userProfileDao().getUserProfile()
            if (current != null) {
                db.userProfileDao().insertOrUpdate(current.copy(explanationLanguage = language))
            }
        }
    }

    fun setNoraExpression(expression: NoraExpression) {
        _noraExpression.value = expression
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        speechRecognizer?.destroy()
    }
}
