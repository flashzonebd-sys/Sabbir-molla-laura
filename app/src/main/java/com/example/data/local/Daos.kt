package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET xp = xp + :xpEarned, completedLessonsCount = completedLessonsCount + 1 WHERE id = 1")
    suspend fun addLessonCompletion(xpEarned: Int)

    @Query("UPDATE user_profile SET wordsLearnedCount = wordsLearnedCount + 1 WHERE id = 1")
    suspend fun incrementWordsCount()

    @Query("UPDATE user_profile SET speakingMinutes = speakingMinutes + :minutes WHERE id = 1")
    suspend fun addSpeakingMinutes(minutes: Int)
}

@Dao
interface VocabDao {
    @Query("SELECT * FROM saved_vocabulary ORDER BY word ASC")
    fun getAllVocabFlow(): Flow<List<SavedVocabEntity>>

    @Query("SELECT * FROM saved_vocabulary WHERE isFavorite = 1")
    fun getFavoriteVocabFlow(): Flow<List<SavedVocabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(vocab: SavedVocabEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vocabList: List<SavedVocabEntity>)

    @Update
    suspend fun updateVocab(vocab: SavedVocabEntity)

    @Query("UPDATE saved_vocabulary SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: String)

    @Query("UPDATE saved_vocabulary SET masteryLevel = :level, lastReviewed = :time WHERE id = :id")
    suspend fun updateMastery(id: String, level: Int, time: Long = System.currentTimeMillis())
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM completed_lessons")
    fun getCompletedLessonsFlow(): Flow<List<CompletedLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markLessonCompleted(completed: CompletedLessonEntity)

    @Query("SELECT COUNT(*) FROM completed_lessons WHERE lessonId = :lessonId")
    suspend fun isLessonCompleted(lessonId: String): Int
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessagesFlow(): Flow<List<ChatMessageEntity>>

    @Insert
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}
