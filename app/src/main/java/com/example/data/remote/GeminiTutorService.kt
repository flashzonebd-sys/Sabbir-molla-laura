package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class NoraTutorResponse(
    val reply: String,
    val correctedSentence: String? = null,
    val explanation: String? = null,
    val isRealAi: Boolean = true
)

class GeminiTutorService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun chatWithNora(
        userMessage: String,
        scenario: String,
        level: String,
        includeBangla: Boolean,
        conversationHistory: List<Pair<String, String>> // sender to text
    ): NoraTutorResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d("GeminiTutorService", "Gemini API key is not configured or placeholder, using smart native tutor")
            return@withContext generateSmartNativeResponse(userMessage, scenario, includeBangla)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val systemInstruction = """
                You are NORA, an expert, warm, and highly engaging female AI English tutor.
                The learner is practicing English in the scenario: '$scenario'.
                Learner proficiency: '$level'.
                ${if (includeBangla) "Include a short Bangla translation or tip for key words/corrections where helpful." else "Explain purely in clear, accessible English."}
                
                Respond STRICTLY with a valid JSON object with the following fields:
                {
                  "reply": "Your natural, empathetic reply (2-3 sentences max) that answers the learner and asks an engaging follow-up question to keep the conversation going.",
                  "correctedSentence": "If the learner made any grammar, vocabulary, or preposition mistake, write the natural English version here. If their sentence was completely correct, output null.",
                  "explanation": "If correctedSentence is provided, explain the grammar/usage difference gently in 1-2 simple sentences${if (includeBangla) " with a Bangla tip" else ""}. If no mistake, write null."
                }
            """.trimIndent()

            val contentsArray = JSONArray()

            // Include last 4 turns of history for context
            val recentTurns = conversationHistory.takeLast(4)
            for ((sender, text) in recentTurns) {
                val role = if (sender.equals("user", ignoreCase = true)) "user" else "model"
                val turnObj = JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", text))
                    })
                }
                contentsArray.put(turnObj)
            }

            // Current user message
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().put("text", userMessage))
                })
            }
            contentsArray.put(currentTurn)

            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("responseMimeType", "application/json")
                })
            }

            val requestBody = rootJson.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e("GeminiTutorService", "HTTP error ${response.code}: $errBody")
                    return@withContext generateSmartNativeResponse(userMessage, scenario, includeBangla)
                }

                val responseBody = response.body?.string() ?: ""
                val resObj = JSONObject(responseBody)
                val candidates = resObj.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val rawText = parts?.optJSONObject(0)?.optString("text") ?: ""

                parseNoraJsonResponse(rawText)
            }
        } catch (e: Exception) {
            Log.e("GeminiTutorService", "API call exception: ${e.message}", e)
            generateSmartNativeResponse(userMessage, scenario, includeBangla)
        }
    }

    private fun parseNoraJsonResponse(jsonText: String): NoraTutorResponse {
        return try {
            val clean = jsonText.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val parsed = JSONObject(clean)
            val reply = parsed.optString("reply", "That is great! Tell me more about that.")
            val correctedSentence = if (parsed.isNull("correctedSentence") || parsed.optString("correctedSentence").isBlank()) null else parsed.optString("correctedSentence")
            val explanation = if (parsed.isNull("explanation") || parsed.optString("explanation").isBlank()) null else parsed.optString("explanation")

            NoraTutorResponse(
                reply = reply,
                correctedSentence = correctedSentence,
                explanation = explanation,
                isRealAi = true
            )
        } catch (e: Exception) {
            NoraTutorResponse(
                reply = jsonText.take(200),
                correctedSentence = null,
                explanation = null,
                isRealAi = true
            )
        }
    }

    private fun generateSmartNativeResponse(
        userMessage: String,
        scenario: String,
        includeBangla: Boolean
    ): NoraTutorResponse {
        val lower = userMessage.lowercase().trim()

        // Detect common grammar mistakes
        var correction: String? = null
        var explanation: String? = null

        if (lower.contains("yesterday i go") || lower.contains("i go yesterday")) {
            correction = "Yesterday, I went..."
            explanation = "Use 'went' (past simple of 'go') when referring to yesterday or past events." +
                    if (includeBangla) " (অতীতের ঘটনার জন্য 'went' ব্যবহার করতে হয়।)" else ""
        } else if (lower.contains("i am agree") || lower.contains("i am disagree")) {
            correction = "I agree / I disagree"
            explanation = "'Agree' is already a verb, so you don't need 'am'. Simply say 'I agree!'" +
                    if (includeBangla) " ('Agree' নিজেই একটি verb, তাই 'am' দরকার নেই।)" else ""
        } else if (lower.contains("she like") || lower.contains("he like") || lower.contains("he go") || lower.contains("she go")) {
            correction = "He/She likes or goes..."
            explanation = "Remember to add '-s' or '-es' for third-person singular (He/She/It) in the present tense." +
                    if (includeBangla) " (Third person singular হলে verb এর শেষে 's/es' যোগ হয়।)" else ""
        } else if (lower.contains("im good in english") || lower.contains("i am good in english")) {
            correction = "I am good at English."
            explanation = "We use the preposition 'at' with skills and abilities (e.g., good at math, good at English)." +
                    if (includeBangla) " (কোনো দক্ষতায় ভালো বোঝাতে 'good at' বসে।)" else ""
        } else if (lower.contains("i have 20 years old") || lower.contains("i have 25 years old")) {
            correction = "I am 20 years old."
            explanation = "In English, express your age with the verb 'to be' (I am), not 'have'."
        }

        val reply = when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
                "Hello there! It is wonderful to chat with you today! 😊 How has your day been so far?"
            }
            scenario.contains("Restaurant", ignoreCase = true) || scenario.contains("Food", ignoreCase = true) -> {
                if (lower.contains("order") || lower.contains("menu") || lower.contains("want")) {
                    "Excellent choice! That sounds delicious. Would you like anything to drink with that, perhaps a fresh juice or sparkling water?"
                } else {
                    "Welcome to the café! What can I get started for you today?"
                }
            }
            scenario.contains("Job Interview", ignoreCase = true) -> {
                "That's very insightful. Could you give me an example of a challenge you faced in your previous role and how you overcame it?"
            }
            scenario.contains("Travel", ignoreCase = true) || scenario.contains("Airport", ignoreCase = true) -> {
                "Traveling is such an enriching experience! Where are you flying to, and is this your first time visiting?"
            }
            scenario.contains("IELTS", ignoreCase = true) -> {
                "Well articulated! In Part 2, examiners look for fluent sentence connectors. Can you expand on how that affected your personal perspective?"
            }
            else -> {
                "That is fascinating! Practicing like this is the fastest way to gain real confidence. What would you like to explore next?"
            }
        }

        return NoraTutorResponse(
            reply = reply,
            correctedSentence = correction,
            explanation = explanation,
            isRealAi = false
        )
    }
}
