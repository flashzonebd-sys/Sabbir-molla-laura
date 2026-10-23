package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GrammarLesson
import com.example.data.model.NoraExpression
import com.example.data.model.QuizItem
import com.example.data.repository.CurriculumRepository
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
fun GrammarQuizScreen(
    viewModel: NoraViewModel,
    onBack: () -> Unit
) {
    val grammarLessons = CurriculumRepository.grammarLessons
    var currentLessonIndex by remember { mutableIntStateOf(0) }
    val currentLesson = grammarLessons[currentLessonIndex]

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Academy Rules, 1: Practice Quiz
    var currentQuizIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    val explanationLang by viewModel.explanationLanguage.collectAsState()
    val showBangla = explanationLang.contains("Bangla")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grammar Academy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Lesson Selector Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(grammarLessons.indices.toList()) { i ->
                        val isSel = i == currentLessonIndex
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSel) NoraPrimaryPurple else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .clickable {
                                    currentLessonIndex = i
                                    currentQuizIndex = 0
                                    selectedOptionIndex = null
                                    isSubmitted = false
                                }
                                .border(
                                    width = 1.dp,
                                    color = if (isSel) NoraPrimaryPurple else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(14.dp)
                                )
                        ) {
                            Text(
                                text = grammarLessons[i].title.take(24) + "...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) NoraWhite else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Tab Row (Rules vs Quiz)
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp)),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("📖 Lesson & Rules", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("⚡ Practice Quiz", fontWeight = FontWeight.Bold) }
                    )
                }

                if (selectedTab == 0) {
                    // RULES MODE
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = NoraElectricViolet.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = currentLesson.tag,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NoraElectricViolet,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = currentLesson.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = currentLesson.explanation,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    )

                                    if (showBangla && currentLesson.banglaExplanation.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "বাংলা সারসংক্ষেপ: ${currentLesson.banglaExplanation}",
                                            fontSize = 13.sp,
                                            color = NoraPrimaryPurple,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Rules breakdown
                        item {
                            Text(
                                text = "Key Rules to Remember",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        items(currentLesson.rules) { rule ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = NoraSoftLavender.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NoraPrimaryPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = rule,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Common Mistake Alert
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = NoraCoralPink.copy(alpha = 0.1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NoraCoralPink.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "⚠️ Watch Out: Common Mistake",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NoraCoralPink
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "❌ Don't say: \"${currentLesson.commonMistake.first}\"",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "✅ Say: \"${currentLesson.commonMistake.second}\"",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NoraMintGreen
                                    )
                                }
                            }
                        }

                        item {
                            PrimaryGradientButton(
                                text = "Start Practice Quiz ⚡",
                                onClick = { selectedTab = 1 },
                                testTag = "start_grammar_quiz_button"
                            )
                        }
                    }
                } else {
                    // QUIZ MODE
                    val q = currentLesson.questions.getOrNull(currentQuizIndex)
                    if (q != null) {
                        val isCorrect = selectedOptionIndex == q.correctIndex

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Challenge ${currentQuizIndex + 1} of ${currentLesson.questions.size}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NoraElectricViolet
                                    )
                                    Text(
                                        text = "+25 XP",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = q.question,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 26.sp
                                )
                            }

                            items(q.options.indices.toList()) { idx ->
                                val option = q.options[idx]
                                val isSelected = selectedOptionIndex == idx

                                val (borderCol, bgCol) = when {
                                    isSubmitted && idx == q.correctIndex -> Pair(NoraMintGreen, NoraMintGreen.copy(alpha = 0.15f))
                                    isSubmitted && isSelected && !isCorrect -> Pair(NoraCoralPink, NoraCoralPink.copy(alpha = 0.15f))
                                    isSelected -> Pair(NoraPrimaryPurple, NoraPrimaryPurple.copy(alpha = 0.1f))
                                    else -> Pair(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.colorScheme.surface)
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = bgCol,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderCol),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!isSubmitted) selectedOptionIndex = idx
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + idx)}. ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = borderCol
                                        )
                                        Text(
                                            text = option,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isSubmitted && idx == q.correctIndex) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NoraMintGreen)
                                        } else if (isSubmitted && isSelected && !isCorrect) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = NoraCoralPink)
                                        }
                                    }
                                }
                            }

                            if (isSubmitted) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isCorrect) NoraMintGreen.copy(alpha = 0.12f) else NoraCoralPink.copy(alpha = 0.12f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = if (isCorrect) "Correct! 🎉" else "Helpful Explanation:",
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
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                if (!isSubmitted) {
                                    PrimaryGradientButton(
                                        text = "Submit Answer",
                                        enabled = selectedOptionIndex != null,
                                        onClick = {
                                            isSubmitted = true
                                            if (selectedOptionIndex == q.correctIndex) {
                                                showConfetti = true
                                            }
                                        },
                                        testTag = "submit_quiz_answer"
                                    )
                                } else {
                                    PrimaryGradientButton(
                                        text = if (currentQuizIndex < currentLesson.questions.size - 1) "Next Challenge" else "Complete Academy Lesson! 🌟",
                                        onClick = {
                                            if (currentQuizIndex < currentLesson.questions.size - 1) {
                                                currentQuizIndex++
                                                selectedOptionIndex = null
                                                isSubmitted = false
                                            } else {
                                                viewModel.completeLesson(currentLesson.id, 40)
                                                showConfetti = true
                                                selectedTab = 0
                                            }
                                        },
                                        testTag = "next_challenge_button"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showConfetti) {
                ConfettiEffect(modifier = Modifier.fillMaxSize(), onFinished = { showConfetti = false })
            }
        }
    }
}
