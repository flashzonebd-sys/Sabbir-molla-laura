package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val englishLevel: String,
    val primaryGoal: String,
    val explanationLanguage: String,
    val dailyGoalMinutes: Int,
    val biggestDifficulty: String,
    val xp: Int,
    val streakDays: Int,
    val completedLessonsCount: Int,
    val wordsLearnedCount: Int,
    val speakingMinutes: Int,
    val isOnboarded: Boolean,
    val lastActiveDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_vocabulary")
data class SavedVocabEntity(
    @PrimaryKey val id: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val definitionBangla: String,
    val exampleSentence: String,
    val category: String,
    val isFavorite: Boolean,
    val masteryLevel: Int,
    val lastReviewed: Long = System.currentTimeMillis()
)

@Entity(tableName = "completed_lessons")
data class CompletedLessonEntity(
    @PrimaryKey val lessonId: String,
    val completedAt: Long = System.currentTimeMillis(),
    val scorePercent: Int = 100,
    val xpEarned: Int = 20
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" or "nora"
    val text: String,
    val originalSentence: String? = null,
    val correctedSentence: String? = null,
    val correctionExplanation: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
