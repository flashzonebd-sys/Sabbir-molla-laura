package com.example.data.model

data class UserProfile(
    val name: String = "Learner",
    val englishLevel: String = "Intermediate",
    val primaryGoal: String = "Speaking Confidently",
    val explanationLanguage: String = "English", // or "Bangla"
    val dailyGoalMinutes: Int = 15,
    val biggestDifficulty: String = "Speaking Confidence",
    val xp: Int = 120,
    val streakDays: Int = 3,
    val completedLessonsCount: Int = 5,
    val wordsLearnedCount: Int = 24,
    val speakingMinutes: Int = 18,
    val isOnboarded: Boolean = false
)

enum class NoraExpression {
    WELCOME,
    SPEAKING,
    LISTENING,
    THINKING,
    CELEBRATING,
    ENCOURAGING,
    CORRECTING
}

data class VocabWord(
    val id: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val definitionBangla: String,
    val exampleSentence: String,
    val category: String,
    val isFavorite: Boolean = false,
    val masteryLevel: Int = 1 // 1: New, 2: Learning, 3: Mastered
)

data class LessonItem(
    val id: String,
    val categoryId: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val isLocked: Boolean,
    val isCompleted: Boolean,
    val level: String,
    val overview: String,
    val banglaTip: String,
    val dialogueLines: List<DialogueLine> = emptyList(),
    val keyPhrases: List<KeyPhrase> = emptyList(),
    val quizQuestions: List<QuizItem> = emptyList()
)

data class DialogueLine(
    val speaker: String, // "Nora" or "You"
    val englishText: String,
    val banglaTranslation: String
)

data class KeyPhrase(
    val phrase: String,
    val meaning: String,
    val example: String
)

data class QuizItem(
    val question: String,
    val questionBangla: String = "",
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val explanationBangla: String = ""
)

data class GrammarLesson(
    val id: String,
    val title: String,
    val tag: String,
    val level: String,
    val explanation: String,
    val banglaExplanation: String,
    val rules: List<String>,
    val examples: List<Pair<String, String>>,
    val commonMistake: Pair<String, String>, // Incorrect, Correct
    val questions: List<QuizItem>
)

data class PronunciationExercise(
    val id: String,
    val sentence: String,
    val phonetic: String,
    val focusSound: String,
    val tip: String,
    val difficulty: String
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val progress: Float,
    val xpReward: Int
)

data class DailyMission(
    val id: String,
    val title: String,
    val xp: Int,
    val current: Int,
    val target: Int,
    val isDone: Boolean
)
