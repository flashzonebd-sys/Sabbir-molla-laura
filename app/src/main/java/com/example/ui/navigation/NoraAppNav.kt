package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LessonItem
import com.example.ui.screens.GrammarQuizScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonPlayerScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.PronunciationLabScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TutorChatScreen
import com.example.ui.screens.VocabularyScreen
import com.example.ui.theme.NoraDarkNavy
import com.example.ui.theme.NoraElectricViolet
import com.example.ui.theme.NoraPrimaryPurple
import com.example.ui.theme.NoraWhite
import com.example.viewmodel.NoraViewModel

enum class MainTab(val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    LEARN("Learn", Icons.Filled.School, Icons.Outlined.School),
    PRACTICE("Practice", Icons.Filled.Mic, Icons.Outlined.Mic),
    PROGRESS("Progress", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

enum class SubScreen {
    NONE,
    TUTOR_CHAT,
    VOCABULARY,
    LESSON_PLAYER,
    GRAMMAR_QUIZ,
    PRONUNCIATION_LAB,
    ONBOARDING
}

@Composable
fun NoraAppNav(
    viewModel: NoraViewModel
) {
    val profile by viewModel.userProfile.collectAsState()
    var isSplashDone by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var activeSubScreen by remember { mutableStateOf(SubScreen.NONE) }
    var activeLessonToPlay by remember { mutableStateOf<LessonItem?>(null) }

    if (!isSplashDone) {
        SplashScreen(onSplashFinished = { isSplashDone = true })
        return
    }

    if (!profile.isOnboarded || activeSubScreen == SubScreen.ONBOARDING) {
        OnboardingScreen(
            onFinishOnboarding = { name, level, goal, explanationLang, dailyTarget, difficulty ->
                viewModel.completeOnboarding(
                    name = name,
                    level = level,
                    goal = goal,
                    explanationLang = explanationLang,
                    dailyTarget = dailyTarget,
                    biggestDifficulty = difficulty
                )
                activeSubScreen = SubScreen.NONE
            }
        )
        return
    }

    // Handle Sub-Screens with explicit BackHandler
    when (activeSubScreen) {
        SubScreen.TUTOR_CHAT -> {
            BackHandler { activeSubScreen = SubScreen.NONE }
            TutorChatScreen(
                viewModel = viewModel,
                onBack = { activeSubScreen = SubScreen.NONE }
            )
            return
        }
        SubScreen.VOCABULARY -> {
            BackHandler { activeSubScreen = SubScreen.NONE }
            VocabularyScreen(
                viewModel = viewModel,
                onNavigateToProfile = {
                    activeSubScreen = SubScreen.NONE
                    currentTab = MainTab.PROFILE
                }
            )
            return
        }
        SubScreen.LESSON_PLAYER -> {
            BackHandler {
                activeLessonToPlay = null
                activeSubScreen = SubScreen.NONE
            }
            if (activeLessonToPlay != null) {
                LessonPlayerScreen(
                    lesson = activeLessonToPlay!!,
                    viewModel = viewModel,
                    onFinish = {
                        activeLessonToPlay = null
                        activeSubScreen = SubScreen.NONE
                    }
                )
            }
            return
        }
        SubScreen.GRAMMAR_QUIZ -> {
            BackHandler { activeSubScreen = SubScreen.NONE }
            GrammarQuizScreen(
                viewModel = viewModel,
                onBack = { activeSubScreen = SubScreen.NONE }
            )
            return
        }
        SubScreen.PRONUNCIATION_LAB -> {
            BackHandler { activeSubScreen = SubScreen.NONE }
            PronunciationLabScreen(
                viewModel = viewModel,
                onBack = { activeSubScreen = SubScreen.NONE }
            )
            return
        }
        else -> Unit
    }

    // Main Tab Navigation with 5 destinations
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NoraWhite,
                            selectedTextColor = NoraPrimaryPurple,
                            indicatorColor = NoraPrimaryPurple,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = currentTab, label = "tab_fade") { tab ->
                when (tab) {
                    MainTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToTutor = { activeSubScreen = SubScreen.TUTOR_CHAT },
                        onNavigateToLearn = { currentTab = MainTab.LEARN },
                        onNavigateToVocab = { activeSubScreen = SubScreen.VOCABULARY },
                        onNavigateToPronunciation = { activeSubScreen = SubScreen.PRONUNCIATION_LAB },
                        onNavigateToGrammar = { activeSubScreen = SubScreen.GRAMMAR_QUIZ },
                        onNavigateToProfile = { currentTab = MainTab.PROFILE }
                    )
                    MainTab.LEARN -> LearnScreen(
                        viewModel = viewModel,
                        onSelectLesson = { lesson ->
                            activeLessonToPlay = lesson
                            activeSubScreen = SubScreen.LESSON_PLAYER
                        },
                        onNavigateToProfile = { currentTab = MainTab.PROFILE }
                    )
                    MainTab.PRACTICE -> PronunciationLabScreen(
                        viewModel = viewModel,
                        onBack = { currentTab = MainTab.HOME }
                    )
                    MainTab.PROGRESS -> ProgressScreen(
                        viewModel = viewModel,
                        onNavigateToProfile = { currentTab = MainTab.PROFILE }
                    )
                    MainTab.PROFILE -> ProfileScreen(
                        viewModel = viewModel,
                        onResetOnboarding = { activeSubScreen = SubScreen.ONBOARDING }
                    )
                }
            }
        }
    }
}
