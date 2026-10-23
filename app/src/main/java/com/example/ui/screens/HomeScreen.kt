package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoraExpression
import com.example.ui.components.DailyGoalCard
import com.example.ui.components.NoraAvatar
import com.example.ui.components.NoraTopBar
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

@Composable
fun HomeScreen(
    viewModel: NoraViewModel,
    onNavigateToTutor: () -> Unit,
    onNavigateToLearn: () -> Unit,
    onNavigateToVocab: () -> Unit,
    onNavigateToPronunciation: () -> Unit,
    onNavigateToGrammar: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val expression by viewModel.noraExpression.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val welcomeGreeting = "Good morning, ${profile.name}! Ready to make your English better today?"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar with level & streak
        item {
            NoraTopBar(
                userName = profile.name,
                level = profile.englishLevel,
                streak = profile.streakDays,
                xp = profile.xp,
                onAvatarClick = onNavigateToProfile
            )
        }

        // Nora Interactive Welcome Banner
        item {
            NoraWelcomeBanner(
                greeting = welcomeGreeting,
                expression = expression,
                isSpeaking = isSpeaking,
                onSpeakGreeting = { viewModel.speakText(welcomeGreeting) },
                onChatClick = onNavigateToTutor
            )
        }

        // Daily Progress Card with animated progress
        item {
            DailyGoalCard(
                currentMinutes = profile.speakingMinutes,
                targetMinutes = profile.dailyGoalMinutes,
                completedLessons = profile.completedLessonsCount,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Weekly Activity Row
        item {
            WeeklyActivityRow(modifier = Modifier.padding(horizontal = 16.dp))
        }

        // Continue Learning Hero Card
        item {
            ContinueLearningCard(
                lessonTitle = "Ordering Coffee & Snacks at a Café",
                category = "Everyday English • 7 mins left",
                xpReward = 35,
                onResume = onNavigateToLearn,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Section Title: Learning Adventures
        item {
            Text(
                text = "Explore Learning Hub",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // 10 Distinct Feature Cards
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Speak with Nora (Hero card)
                LearningHubCard(
                    title = "Speak with NORA",
                    subtitle = "Real-time AI conversation with voice & instant correction",
                    icon = Icons.Default.ChatBubble,
                    badge = "AI Tutor",
                    gradient = Brush.horizontalGradient(listOf(NoraPrimaryPurple, NoraElectricViolet)),
                    tagColor = NoraSkyCyan,
                    onClick = onNavigateToTutor,
                    testTag = "card_speak_with_nora"
                )

                // 2. Today's Learning Path
                LearningHubCard(
                    title = "Today's Learning Path",
                    subtitle = "Structured sequential lessons from Beginner to Fluency",
                    icon = Icons.Default.School,
                    badge = "Core Curriculum",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF38BDF8))),
                    tagColor = NoraWhite,
                    onClick = onNavigateToLearn,
                    testTag = "card_learning_path"
                )

                // 3. Vocabulary Builder
                LearningHubCard(
                    title = "Vocabulary Builder",
                    subtitle = "Interactive 3D flashcards, definitions & spaced repetition",
                    icon = Icons.Default.Book,
                    badge = "${profile.wordsLearnedCount} Saved Words",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF0D9488), NoraMintGreen)),
                    tagColor = NoraSunshineYellow,
                    onClick = onNavigateToVocab,
                    testTag = "card_vocab_builder"
                )

                // 4. Grammar Made Easy
                LearningHubCard(
                    title = "Grammar Made Easy",
                    subtitle = "Master tenses, articles & prepositions without boring drills",
                    icon = Icons.Default.Edit,
                    badge = "Academy",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFFEA580C), NoraSunshineYellow)),
                    tagColor = NoraWhite,
                    onClick = onNavigateToGrammar,
                    testTag = "card_grammar_easy"
                )

                // 5. Pronunciation Lab
                LearningHubCard(
                    title = "Pronunciation Lab",
                    subtitle = "Record speech & compare phonetic rhythm with native AI",
                    icon = Icons.Default.Mic,
                    badge = "Speech AI",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFFDB2777), NoraCoralPink)),
                    tagColor = NoraWhite,
                    onClick = onNavigateToPronunciation,
                    testTag = "card_pronunciation_lab"
                )

                // 6. Listening Practice
                LearningHubCard(
                    title = "Listening Practice",
                    subtitle = "Native audio dialogues with slow playback & comprehension",
                    icon = Icons.Default.Headphones,
                    badge = "Audio Drills",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF4F46E5), NoraElectricViolet)),
                    tagColor = NoraSkyCyan,
                    onClick = onNavigateToLearn,
                    testTag = "card_listening_practice"
                )

                // 7. IELTS & TOEFL Preparation
                LearningHubCard(
                    title = "IELTS & TOEFL Prep",
                    subtitle = "Speaking Part 1-3 simulator, Band 7+ vocabulary & tips",
                    icon = Icons.Default.AutoAwesome,
                    badge = "Exam Ready",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFC084FC))),
                    tagColor = NoraWhite,
                    onClick = onNavigateToLearn,
                    testTag = "card_ielts_toefl"
                )

                // 8. Daily Challenge
                LearningHubCard(
                    title = "Daily Challenge",
                    subtitle = "Test your daily reflexes with a 2-minute rapid English quiz",
                    icon = Icons.Default.Star,
                    badge = "+50 XP Bonus",
                    gradient = Brush.horizontalGradient(listOf(Color(0xFFD97706), Color(0xFFFBBF24))),
                    tagColor = NoraDarkNavy,
                    onClick = onNavigateToGrammar,
                    testTag = "card_daily_challenge"
                )
            }
        }
    }
}

@Composable
private fun NoraWelcomeBanner(
    greeting: String,
    expression: NoraExpression,
    isSpeaking: Boolean,
    onSpeakGreeting: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clickable { onChatClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NoraDarkNavy)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NoraPrimaryPurple.copy(alpha = 0.45f),
                            Color.Transparent
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NoraAvatar(
                    expression = expression,
                    size = 80.dp,
                    showExpressionBadge = true
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "NORA TUTOR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NoraSkyCyan,
                            letterSpacing = 1.sp
                        )
                        IconButton(
                            onClick = onSpeakGreeting,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play Greeting",
                                tint = if (isSpeaking) NoraMintGreen else NoraWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = greeting,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = NoraWhite,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NoraPrimaryPurple
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Start Chatting",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NoraWhite
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = NoraWhite,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContinueLearningCard(
    lessonTitle: String,
    category: String,
    xpReward: Int,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onResume() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(NoraPrimaryPurple.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = NoraPrimaryPurple,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "CONTINUE LESSON",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NoraElectricViolet,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = lessonTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = category,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NoraSunshineYellow.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "+$xpReward XP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun WeeklyActivityRow(
    modifier: Modifier = Modifier
) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    val activeDays = listOf(true, true, true, false, false, false, false)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Weekly Activity Streak",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in days.indices) {
                    val isActive = activeDays[i]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isActive) NoraMintGreen else NoraSoftLavender),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isActive) "✓" else "•",
                                color = if (isActive) NoraDarkNavy else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = days[i],
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearningHubCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String,
    gradient: Brush,
    tagColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NoraWhite.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NoraWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NoraWhite.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = tagColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NoraWhite
                    )

                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = NoraWhite.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = NoraWhite.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
