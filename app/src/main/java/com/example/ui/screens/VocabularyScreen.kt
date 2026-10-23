package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VocabWord
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
fun VocabularyScreen(
    viewModel: NoraViewModel,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val vocabList by viewModel.vocabList.collectAsState()
    val explanationLang by viewModel.explanationLanguage.collectAsState()
    val showBangla = explanationLang.contains("Bangla")

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Flashcards, 1: Word Bank
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Favorites", "Speaking & Writing", "Professional English", "Mindset & Growth", "Everyday Idioms", "Power Words")

    val filteredList = vocabList.filter { word ->
        val matchesCategory = when (selectedFilterCategory) {
            "All" -> true
            "Favorites" -> word.isFavorite
            else -> word.category.contains(selectedFilterCategory, ignoreCase = true)
        }
        val matchesSearch = searchQuery.isBlank() ||
                word.word.contains(searchQuery, ignoreCase = true) ||
                word.definition.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NoraTopBar(
                userName = profile.name,
                level = profile.englishLevel,
                streak = profile.streakDays,
                xp = profile.xp,
                onAvatarClick = onNavigateToProfile
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Vocabulary Vault",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Master powerful expressions with active recall and audio.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Mode Tab Row (Flashcard Deck vs Word Bank)
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("🃏 3D Flashcards", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("📚 Word Bank (${vocabList.size})", fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (selectedTab == 0) {
            // Flashcard Deck Mode
            item {
                if (vocabList.isNotEmpty()) {
                    val safeIndex = currentCardIndex.coerceIn(0, vocabList.size - 1)
                    val currentWord = vocabList[safeIndex]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Card ${safeIndex + 1} of ${vocabList.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NoraElectricViolet,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // 3D Flip Card
                        InteractiveFlipCard(
                            word = currentWord,
                            isFlipped = isFlipped,
                            showBangla = showBangla,
                            onCardClick = { isFlipped = !isFlipped },
                            onPlayAudio = { viewModel.speakText(currentWord.word) },
                            onToggleFavorite = { viewModel.toggleVocabFavorite(currentWord.id) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Flashcard Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = NoraCoralPink.copy(alpha = 0.12f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.updateVocabMastery(currentWord.id, 2)
                                        isFlipped = false
                                        currentCardIndex = (safeIndex + 1) % vocabList.size
                                    }
                            ) {
                                Text(
                                    text = "Still Learning",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NoraCoralPink,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = NoraMintGreen.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.updateVocabMastery(currentWord.id, 3)
                                        isFlipped = false
                                        currentCardIndex = (safeIndex + 1) % vocabList.size
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 14.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NoraMintGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Mastered (+10 XP)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NoraDarkNavy
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Word Bank Mode with Filter & Search
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search words or meanings...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NoraPrimaryPurple,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            val isSelected = selectedFilterCategory == cat
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) NoraPrimaryPurple else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clickable { selectedFilterCategory = cat }
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) NoraPrimaryPurple else MaterialTheme.colorScheme.outlineVariant,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NoraWhite else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            items(filteredList) { word ->
                VocabListItem(
                    word = word,
                    showBangla = showBangla,
                    onPlay = { viewModel.speakText(word.word) },
                    onToggleFavorite = { viewModel.toggleVocabFavorite(word.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun InteractiveFlipCard(
    word: VocabWord,
    isFlipped: Boolean,
    showBangla: Boolean,
    onCardClick: () -> Unit,
    onPlayAudio: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "card_flip"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { onCardClick() }
            .testTag("flip_flashcard"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) NoraDarkNavy else NoraWhite
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        if (rotation <= 90f) {
            // FRONT OF CARD
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NoraSoftLavender
                    ) {
                        Text(
                            text = word.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NoraElectricViolet,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (word.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Favorite",
                            tint = if (word.isFavorite) NoraCoralPink else Color.Gray
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = word.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NoraPrimaryPurple
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${word.phonetic} • ${word.partOfSpeech}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    IconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NoraPrimaryPurple.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Listen",
                            tint = NoraPrimaryPurple
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flip,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tap to reveal definition",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            // BACK OF CARD (Rotated 180 so it appears normal)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEANING & CONTEXT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NoraSkyCyan,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onPlayAudio) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = NoraWhite
                        )
                    }
                }

                Column {
                    Text(
                        text = word.definition,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NoraWhite,
                        lineHeight = 22.sp
                    )

                    if (showBangla && word.definitionBangla.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "বাংলা: ${word.definitionBangla}",
                            fontSize = 13.sp,
                            color = NoraSunshineYellow,
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NoraWhite.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Example: \"${word.exampleSentence}\"",
                            fontSize = 13.sp,
                            color = NoraWhite.copy(alpha = 0.9f),
                            modifier = Modifier.padding(10.dp),
                            lineHeight = 18.sp
                        )
                    }
                }

                Text(
                    text = "Tap to flip back",
                    fontSize = 11.sp,
                    color = NoraWhite.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun VocabListItem(
    word: VocabWord,
    showBangla: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.word,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NoraPrimaryPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = word.phonetic,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPlay, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Listen",
                            tint = NoraPrimaryPurple
                        )
                    }
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (word.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Favorite",
                            tint = if (word.isFavorite) NoraCoralPink else Color.Gray
                        )
                    }
                }
            }

            Text(
                text = word.definition,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp)
            )

            if (showBangla && word.definitionBangla.isNotBlank()) {
                Text(
                    text = word.definitionBangla,
                    fontSize = 12.sp,
                    color = NoraElectricViolet,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "\"${word.exampleSentence}\"",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
