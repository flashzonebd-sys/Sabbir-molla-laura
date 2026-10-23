package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoraExpression
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

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (name: String, level: String, goal: String, explanationLang: String, dailyTarget: Int, difficulty: String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    var userName by remember { mutableStateOf("Alex") }
    var selectedLevel by remember { mutableStateOf("Intermediate") }
    var selectedGoal by remember { mutableStateOf("Everyday Conversation") }
    var selectedLang by remember { mutableStateOf("English + Bangla Tips") }
    var selectedDailyTarget by remember { mutableIntStateOf(15) }
    var selectedDifficulty by remember { mutableStateOf("Speaking Confidence") }

    val totalSteps = 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Top navigation bar for steps
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (step > 0) {
                IconButton(onClick = { step-- }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            LinearProgressIndicator(
                progress = { ((step + 1).toFloat() / totalSteps.toFloat()) },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NoraPrimaryPurple,
                trackColor = NoraSoftLavender
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "onboarding_step"
        ) { currentStep ->
            when (currentStep) {
                0 -> WelcomeStep(
                    userName = userName,
                    onNameChange = { userName = it },
                    onNext = { step++ }
                )
                1 -> LevelStep(
                    selectedLevel = selectedLevel,
                    onSelect = { selectedLevel = it },
                    onNext = { step++ }
                )
                2 -> GoalStep(
                    selectedGoal = selectedGoal,
                    onSelect = { selectedGoal = it },
                    onNext = { step++ }
                )
                3 -> LanguageAndDifficultyStep(
                    selectedLang = selectedLang,
                    onLangSelect = { selectedLang = it },
                    selectedDifficulty = selectedDifficulty,
                    onDifficultySelect = { selectedDifficulty = it },
                    onNext = { step++ }
                )
                4 -> DailyTargetStep(
                    selectedDailyTarget = selectedDailyTarget,
                    onTargetSelect = { selectedDailyTarget = it },
                    onFinish = {
                        onFinishOnboarding(
                            userName,
                            selectedLevel,
                            selectedGoal,
                            selectedLang,
                            selectedDailyTarget,
                            selectedDifficulty
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun WelcomeStep(
    userName: String,
    onNameChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NoraAvatar(
            expression = NoraExpression.WELCOME,
            size = 130.dp,
            showExpressionBadge = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hey there! I'm NORA.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your personal AI English coach. Let's make English easy, fun, and natural!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = onNameChange,
            label = { Text("What should I call you?") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("name_input"),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryGradientButton(
            text = "Let's Get Started ✨",
            onClick = onNext,
            testTag = "start_button"
        )
    }
}

@Composable
private fun LevelStep(
    selectedLevel: String,
    onSelect: (String) -> Unit,
    onNext: () -> Unit
) {
    val levels = listOf(
        Triple("Beginner", "A1", "I know only a few basic English words"),
        Triple("Elementary", "A2", "I can build simple sentences for daily needs"),
        Triple("Intermediate", "B1", "I can have basic conversations with some hesitation"),
        Triple("Upper-Intermediate", "B2", "I speak relatively fluently on common topics"),
        Triple("Advanced", "C1", "I express ideas effortlessly and want to polish nuance")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What is your English level?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Nora will adapt vocabulary and speaking speed to match your pace.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(levels) { (title, code, desc) ->
                val isSelected = selectedLevel == title
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelect(title) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NoraPrimaryPurple else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    color = if (isSelected) NoraPrimaryPurple.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) NoraPrimaryPurple else NoraSoftLavender),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = code,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NoraWhite else NoraDarkNavy,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = desc,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NoraPrimaryPurple
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Continue",
            onClick = onNext,
            icon = Icons.AutoMirrored.Filled.ArrowForward
        )
    }
}

@Composable
private fun GoalStep(
    selectedGoal: String,
    onSelect: (String) -> Unit,
    onNext: () -> Unit
) {
    val goals = listOf(
        Pair("Everyday Conversation", "🗣️"),
        Pair("Speaking Confidently", "🚀"),
        Pair("Power Vocabulary", "📚"),
        Pair("Grammar Made Easy", "✍️"),
        Pair("Pronunciation & Accent", "🎙️"),
        Pair("Job Interview Preparation", "💼"),
        Pair("IELTS & TOEFL Prep", "🎓")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What is your primary goal?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Pick the skill you'd like to unlock first.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(goals) { (goal, emoji) ->
                val isSelected = selectedGoal == goal
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelect(goal) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NoraSkyCyan else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    color = if (isSelected) NoraSkyCyan.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = goal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NoraSkyCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Continue",
            onClick = onNext,
            icon = Icons.AutoMirrored.Filled.ArrowForward
        )
    }
}

@Composable
private fun LanguageAndDifficultyStep(
    selectedLang: String,
    onLangSelect: (String) -> Unit,
    selectedDifficulty: String,
    onDifficultySelect: (String) -> Unit,
    onNext: () -> Unit
) {
    val languages = listOf("English Only", "English + Bangla Tips")
    val difficulties = listOf(
        "Speaking Confidence (fear of making mistakes)",
        "Remembering Vocabulary in real time",
        "Grammar Rules & Tense confusion",
        "Fast Native Listening comprehension"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Learning Preferences",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Choose your explanation style and biggest challenge.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Text(
            text = "Explanation Language:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (lang in languages) {
                val isSelected = selectedLang == lang
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onLangSelect(lang) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NoraElectricViolet else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = if (isSelected) NoraElectricViolet.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = lang,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Biggest Difficulty in English:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(difficulties) { diff ->
                val isSelected = selectedDifficulty == diff
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onDifficultySelect(diff) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NoraCoralPink else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = if (isSelected) NoraCoralPink.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = diff,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NoraCoralPink
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Continue",
            onClick = onNext,
            icon = Icons.AutoMirrored.Filled.ArrowForward
        )
    }
}

@Composable
private fun DailyTargetStep(
    selectedDailyTarget: Int,
    onTargetSelect: (Int) -> Unit,
    onFinish: () -> Unit
) {
    val targets = listOf(
        Pair(5, "Casual: 5 mins / day (Quick practice)"),
        Pair(10, "Regular: 10 mins / day (Steady progress)"),
        Pair(15, "Committed: 15 mins / day (Recommended)"),
        Pair(20, "Intense: 20 mins / day (Fast fluency)")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoraAvatar(
            expression = NoraExpression.CELEBRATING,
            size = 100.dp,
            showExpressionBadge = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Commit to your daily target",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Consistency creates fluency! Nora will keep you accountable.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(targets) { (mins, label) ->
                val isSelected = selectedDailyTarget == mins
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTargetSelect(mins) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NoraMintGreen else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    color = if (isSelected) NoraMintGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱️ $mins min",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NoraMintGreen
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NoraMintGreen
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryGradientButton(
            text = "Launch My Learning Journey! 🚀",
            onClick = onFinish,
            testTag = "finish_onboarding_button"
        )
    }
}
