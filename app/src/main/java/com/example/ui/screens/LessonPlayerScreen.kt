package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DialogueLine
import com.example.data.model.KeyPhrase
import com.example.data.model.LessonItem
import com.example.data.model.NoraExpression
import com.example.data.model.QuizItem
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.NoraAvatar
import com.example.ui.components.PrimaryGradientButton
import com.example.ui.theme.NoraCoralPink
import com.example.ui.theme.NoraDarkNavy
import com.example.ui.theme.NoraElectricViolet
import com.example.ui.theme.NoraMintGreen
import com.example.ui.theme.NoraPrimaryPurple
import com.example.ui.theme.NoraSkyCyan
import com.example.ui.theme.NoraSoftLavender
import com.example.ui.theme.NoraSunshineYellow
import com.example.ui.theme.NoraWhite
import com.example.viewmodel.NoraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlayerScreen(
    lesson: LessonItem,
    viewModel: NoraViewModel,
    onFinish: () -> Unit
) {
    // 0: Dialogue, 1: Key Phrases, 2: Quiz, 3: Completed
    var playerStep by remember { mutableIntStateOf(0) }
    var currentQuizIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    val explanationLang by viewModel.explanationLanguage.collectAsState()
    val showBangla = explanationLang.contains("Bangla")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = lesson.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        LinearProgressIndicator(
                            progress = { ((playerStep + 1).toFloat() / 4f) },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NoraPrimaryPurple,
                            trackColor = NoraSoftLavender
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (playerStep) {
                0 -> DialogueStep(
                    lesson = lesson,
                    showBangla = showBangla,
                    onPlayAudio = { text -> viewModel.speakText(text) },
                    onNext = { playerStep = 1 }
                )
                1 -> KeyPhrasesStep(
                    keyPhrases = lesson.keyPhrases,
                    onPlayAudio = { text -> viewModel.speakText(text) },
                    onNext = {
                        if (lesson.quizQuestions.isNotEmpty()) {
                            playerStep = 2
                        } else {
                            viewModel.completeLesson(lesson.id, lesson.xpReward)
                            showConfetti = true
                            playerStep = 3
                        }
                    }
                )
                2 -> QuizStep(
                    questions = lesson.quizQuestions,
                    currentIndex = currentQuizIndex,
                    selectedIndex = selectedOptionIndex,
                    isSubmitted = isAnswerSubmitted,
                    showBangla = showBangla,
                    onSelectOption = { if (!isAnswerSubmitted) selectedOptionIndex = it },
                    onSubmitAnswer = {
                        isAnswerSubmitted = true
                    },
                    onNextQuestion = {
                        if (currentQuizIndex < lesson.quizQuestions.size - 1) {
                            currentQuizIndex++
                            selectedOptionIndex = null
                            isAnswerSubmitted = false
                        } else {
                            viewModel.completeLesson(lesson.id, lesson.xpReward)
                            showConfetti = true
                            playerStep = 3
                        }
                    }
                )
                3 -> LessonCompleteStep(
                    lesson = lesson,
                    onReturn = onFinish
                )
            }

            if (showConfetti) {
                ConfettiEffect(
                    modifier = Modifier.fillMaxSize(),
                    onFinished = { showConfetti = false }
                )
            }
        }
    }
}

@Composable
private fun DialogueStep(
    lesson: LessonItem,
    showBangla: Boolean,
    onPlayAudio: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            NoraAvatar(
                expression = NoraExpression.SPEAKING,
                size = 56.dp,
                showExpressionBadge = false
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Listen & Follow Along",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap the speaker on any sentence to hear natural audio.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (showBangla && lesson.banglaTip.isNotBlank()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .clip(RoundedCornerShape(14.dp)),
                color = NoraPrimaryPurple.copy(alpha = 0.08f)
            ) {
                Text(
                    text = "💡 Nora's Tip: ${lesson.banglaTip}",
                    fontSize = 13.sp,
                    color = NoraDarkNavy,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 18.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lesson.dialogueLines) { line ->
                DialogueLineCard(
                    line = line,
                    showBangla = showBangla,
                    onPlay = { onPlayAudio(line.englishText) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Review Key Phrases",
            onClick = onNext,
            testTag = "next_to_phrases_button"
        )
    }
}

@Composable
private fun DialogueLineCard(
    line: DialogueLine,
    showBangla: Boolean,
    onPlay: () -> Unit
) {
    val isNora = line.speaker.equals("nora", ignoreCase = true)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNora) NoraSoftLavender.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isNora) NoraPrimaryPurple else NoraSkyCyan),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isNora) "N" else "You",
                    color = NoraWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = line.speaker,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isNora) NoraPrimaryPurple else Color(0xFF0284C7)
                )
                Text(
                    text = line.englishText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
                if (showBangla && line.banglaTranslation.isNotBlank()) {
                    Text(
                        text = line.banglaTranslation,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            IconButton(onClick = onPlay) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play Audio",
                    tint = NoraPrimaryPurple
                )
            }
        }
    }
}

@Composable
private fun KeyPhrasesStep(
    keyPhrases: List<KeyPhrase>,
    onPlayAudio: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Key Vocabulary & Phrases",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Master these essential building blocks for natural expression.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(keyPhrases) { phrase ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = phrase.phrase,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = NoraPrimaryPurple
                            )
                            IconButton(
                                onClick = { onPlayAudio(phrase.phrase) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Pronounce",
                                    tint = NoraPrimaryPurple
                                )
                            }
                        }

                        Text(
                            text = phrase.meaning,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "Example: \"${phrase.example}\"",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Take Practice Quiz",
            onClick = onNext,
            testTag = "start_quiz_button"
        )
    }
}

@Composable
private fun QuizStep(
    questions: List<QuizItem>,
    currentIndex: Int,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    showBangla: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val q = questions.getOrNull(currentIndex) ?: return
    val isCorrect = selectedIndex == q.correctIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${currentIndex + 1} of ${questions.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NoraElectricViolet
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = NoraSunshineYellow.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "+15 XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = q.question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            lineHeight = 26.sp
        )

        if (showBangla && q.questionBangla.isNotBlank()) {
            Text(
                text = q.questionBangla,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(q.options.indices.toList()) { index ->
                val optionText = q.options[index]
                val isSelected = selectedIndex == index

                val (borderColor, bgColor) = when {
                    isSubmitted && index == q.correctIndex -> Pair(NoraMintGreen, NoraMintGreen.copy(alpha = 0.15f))
                    isSubmitted && isSelected && !isCorrect -> Pair(NoraCoralPink, NoraCoralPink.copy(alpha = 0.15f))
                    isSelected -> Pair(NoraPrimaryPurple, NoraPrimaryPurple.copy(alpha = 0.1f))
                    else -> Pair(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.colorScheme.surface)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectOption(index) }
                        .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
                    color = bgColor
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${('A' + index)}. ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = borderColor
                        )
                        Text(
                            text = optionText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && index == q.correctIndex) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = NoraMintGreen
                            )
                        } else if (isSubmitted && isSelected && !isCorrect) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Incorrect",
                                tint = NoraCoralPink
                            )
                        }
                    }
                }
            }
        }

        // Explanation reveal
        if (isSubmitted) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(14.dp)),
                color = if (isCorrect) NoraMintGreen.copy(alpha = 0.12f) else NoraCoralPink.copy(alpha = 0.12f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isCorrect) "Excellent job! 🎉" else "Not quite, here's why:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isCorrect) NoraMintGreen else NoraCoralPink
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = q.explanation,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (!isSubmitted) {
            PrimaryGradientButton(
                text = "Check Answer",
                enabled = selectedIndex != null,
                onClick = onSubmitAnswer,
                testTag = "check_answer_button"
            )
        } else {
            PrimaryGradientButton(
                text = if (currentIndex < questions.size - 1) "Next Question" else "Complete Lesson! 🌟",
                onClick = onNextQuestion,
                testTag = "next_question_button"
            )
        }
    }
}

@Composable
private fun LessonCompleteStep(
    lesson: LessonItem,
    onReturn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NoraAvatar(
            expression = NoraExpression.CELEBRATING,
            size = 130.dp,
            showExpressionBadge = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lesson Completed! 🎉",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You've successfully mastered '${lesson.title}'",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // XP Award Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NoraSunshineYellow.copy(alpha = 0.15f),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "+${lesson.xpReward} XP Earned!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        PrimaryGradientButton(
            text = "Back to Learning Roadmap",
            onClick = onReturn,
            testTag = "return_roadmap_button"
        )
    }
}
