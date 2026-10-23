package com.example.data.repository

import com.example.data.model.AchievementItem
import com.example.data.model.DailyMission
import com.example.data.model.DialogueLine
import com.example.data.model.GrammarLesson
import com.example.data.model.KeyPhrase
import com.example.data.model.LessonItem
import com.example.data.model.PronunciationExercise
import com.example.data.model.QuizItem
import com.example.data.model.VocabWord

object CurriculumRepository {

    val courses = listOf(
        Pair("everyday", "Everyday English"),
        Pair("spoken", "Spoken English & Fluency"),
        Pair("grammar", "Grammar Mastery"),
        Pair("vocab", "Power Vocabulary"),
        Pair("listening", "Listening Practice"),
        Pair("pronunciation", "Pronunciation Lab"),
        Pair("ielts", "IELTS & TOEFL Prep")
    )

    val sampleLessons = listOf(
        LessonItem(
            id = "lesson_1",
            categoryId = "everyday",
            title = "Meeting Someone for the First Time",
            subtitle = "Introduce yourself with natural confidence",
            durationMinutes = 5,
            xpReward = 30,
            isLocked = false,
            isCompleted = true,
            level = "Beginner",
            overview = "Learn natural greetings, small talk openers, and polite follow-up questions.",
            banglaTip = "কারো সাথে প্রথম দেখার সময় 'Nice to meet you' বলা খুবই স্বাভাবিক ও ভদ্র আচরণ।",
            dialogueLines = listOf(
                DialogueLine("Nora", "Hi there! I'm Nora. What's your name?", "হাই! আমি নোরা। তোমার নাম কী?"),
                DialogueLine("You", "Hello Nora! My name is Alex. Nice to meet you.", "হ্যালো নোরা! আমার নাম অ্যালেক্স। আপনার সাথে পরিচিত হয়ে ভালো লাগল।"),
                DialogueLine("Nora", "The pleasure is mine! How are you enjoying your day so far?", "আমারও ভালো লাগল! তোমার দিনটি কেমন কাটছে?"),
                DialogueLine("You", "It's going great, thank you! I'm excited to practice English with you.", "খুব ভালো কাটছে, ধন্যবাদ! আপনার সাথে ইংরেজি অনুশীলন করতে পেরে আমি আনন্দিত।")
            ),
            keyPhrases = listOf(
                KeyPhrase("Nice to meet you", "Used politely when meeting someone for the first time", "Nice to meet you, Mr. Smith."),
                KeyPhrase("How's it going?", "Casual greeting asking how someone is doing", "Hey Sarah, how's it going?"),
                KeyPhrase("The pleasure is mine", "Warm response to 'Nice to meet you'", "Pleasure is mine, welcome aboard!")
            ),
            quizQuestions = listOf(
                QuizItem(
                    question = "Which response is most polite when introduced to someone new?",
                    questionBangla = "নতুন কারো সাথে পরিচিত হলে সবচেয়ে মার্জিত উত্তর কোনটি?",
                    options = listOf("Nice to meet you!", "What do you want?", "I don't know you.", "Who are you?"),
                    correctIndex = 0,
                    explanation = "'Nice to meet you' is standard polite English for first introductions.",
                    explanationBangla = "'Nice to meet you' পরিচিত হওয়ার জন্য আন্তর্জাতিকভাবে মার্জিত অভিবাদন।"
                ),
                QuizItem(
                    question = "How do you respond politely to 'Nice to meet you'?",
                    questionBangla = "'Nice to meet you'-এর সুন্দর প্রতিউত্তর কী?",
                    options = listOf("Same to you / Pleasure is mine", "No thanks", "Whatever", "Good bye"),
                    correctIndex = 0,
                    explanation = "'Pleasure is mine' or 'Nice to meet you too' shows warmth and courtesy.",
                    explanationBangla = "'Pleasure is mine' অথবা 'Nice to meet you too' বলা অত্যন্ত শোভন।"
                )
            )
        ),
        LessonItem(
            id = "lesson_2",
            categoryId = "everyday",
            title = "Ordering Coffee & Snacks at a Café",
            subtitle = "Polite request phrasing in casual settings",
            durationMinutes = 7,
            xpReward = 35,
            isLocked = false,
            isCompleted = false,
            level = "Elementary",
            overview = "Master using 'Could I please have...' and 'I'd like...' instead of 'I want...'",
            banglaTip = "রেস্তোরাঁ বা ক্যাফেতে অর্ডার করার সময় 'I want' এর পরিবর্তে 'Could I please have' বা 'I would like' ব্যবহার করা বেশি ভদ্র।",
            dialogueLines = listOf(
                DialogueLine("Nora", "Hi! Welcome to Nora's Café. What can I get started for you?", "স্বাগতম! আপনার জন্য কী নিতে পারি?"),
                DialogueLine("You", "Hi! Could I please have a medium cappuccino to go?", "হাই! একটি মিডিয়াম ক্যাপুচিনো পার্সেল পেতে পারি?"),
                DialogueLine("Nora", "Certainly! Would you like whole milk, oat milk, or almond milk?", "অবশ্যই! আপনি সাধারণ দুধ, ওট মিল্ক না কি বাদামের দুধ পছন্দ করবেন?"),
                DialogueLine("You", "Oat milk, please. And could you also add a blueberry muffin?", "ওট মিল্ক দিন দয়া করে। সাথে একটি ব্লুবেরি মাফিনও দেওয়া যাবে?")
            ),
            keyPhrases = listOf(
                KeyPhrase("Could I please have...", "Polite way to order food or drinks", "Could I please have a glass of water?"),
                KeyPhrase("To go / Takeaway", "Ordering food to eat outside the venue", "Two coffees to go, please."),
                KeyPhrase("Keep the change", "Polite way to let the server keep remaining money", "Thank you, keep the change!")
            ),
            quizQuestions = listOf(
                QuizItem(
                    question = "Which sentence is the most natural way to order at a counter?",
                    options = listOf("Could I get an iced latte, please?", "Give me iced latte right now", "I want an iced latte", "Latte! Quick!"),
                    correctIndex = 0,
                    explanation = "Using 'Could I get... please?' softens the request and sounds polite to service staff.",
                    explanationBangla = "'Could I get... please?' অনুরোধকে কোমল এবং ভদ্র করে তোলে।"
                ),
                QuizItem(
                    question = "What does 'To go' mean when ordering food?",
                    options = listOf("Pack it for takeout", "Eat here inside the restaurant", "Cancel the order", "Pay later"),
                    correctIndex = 0,
                    explanation = "'To go' means packaged to take away rather than dining in.",
                    explanationBangla = "'To go' মানে খাবার সাথে নিয়ে যাওয়া (পার্সেল)।"
                )
            )
        ),
        LessonItem(
            id = "lesson_3",
            categoryId = "spoken",
            title = "Expressing Opinions & Agreeing Gracefully",
            subtitle = "Sound thoughtful and articulate in discussions",
            durationMinutes = 8,
            xpReward = 40,
            isLocked = false,
            isCompleted = false,
            level = "Intermediate",
            overview = "Learn phrases like 'In my view...', 'I see your point, but...', and avoid saying 'I am agree'.",
            banglaTip = "'I am agree' ভুল ব্যাকরণ। সঠিক রূপ হলো 'I agree' বা 'I completely agree'।",
            dialogueLines = listOf(
                DialogueLine("Nora", "Do you think working remotely is more productive than in-office?", "তোমার কি মনে হয় ঘরে বসে কাজ করা অফিসের চেয়ে বেশি কার্যকর?"),
                DialogueLine("You", "In my opinion, it depends on the project, but I generally agree.", "আমার মতে, এটি কাজের ওপর নির্ভর করে, তবে সামগ্রিকভাবে আমি একমত।"),
                DialogueLine("Nora", "I see your point! Remote work offers great focus, but teamwork can suffer.", "আমি তোমার যুক্তি বুঝতে পারছি! রিমোট কাজে মনোযোগ ভালো হয়, তবে টিমওয়ার্কে ঘাটতি হতে পারে।"),
                DialogueLine("You", "Exactly! That's why a hybrid schedule seems ideal.", "ঠিক তাই! এজন্য হাইব্রিড মডেলটি সবচেয়ে উপযোগী মনে হয়।")
            ),
            keyPhrases = listOf(
                KeyPhrase("In my opinion / From my perspective", "Standard phrase to introduce personal thoughts", "From my perspective, quality matters most."),
                KeyPhrase("I see your point", "Acknowledges someone else's argument respectfully", "I see your point, but consider the cost."),
                KeyPhrase("I completely agree", "Strong positive agreement without using 'am agree'", "I completely agree with your proposal.")
            ),
            quizQuestions = listOf(
                QuizItem(
                    question = "Which sentence is grammatically correct?",
                    options = listOf("I completely agree with you.", "I am totally agree with you.", "I am agreeing 100%.", "I has agree with you."),
                    correctIndex = 0,
                    explanation = "'Agree' is already a verb, so do not add 'am'.",
                    explanationBangla = "'Agree' নিজেই একটি ক্রিয়াপদ, তাই 'am' ব্যবহার করা ব্যাকরণগত ভুল।"
                )
            )
        ),
        LessonItem(
            id = "lesson_4",
            categoryId = "grammar",
            title = "Present Perfect vs Past Simple",
            subtitle = "Master 'have been' vs 'went' once and for all",
            durationMinutes = 6,
            xpReward = 35,
            isLocked = false,
            isCompleted = false,
            level = "Intermediate",
            overview = "Use Past Simple for finished specific times ('yesterday', 'in 2021') and Present Perfect for life experiences.",
            banglaTip = "অতীতে নির্দিষ্ট সময় উল্লেখ থাকলে (যেমন yesterday, last year) Past Simple (went, saw) ব্যবহার করুন।",
            dialogueLines = emptyList(),
            keyPhrases = listOf(
                KeyPhrase("I have visited Paris twice", "Experience at unspecified time in life", "She has seen that movie before."),
                KeyPhrase("I visited Paris last summer", "Finished action at a specific time in past", "They arrived yesterday afternoon.")
            ),
            quizQuestions = listOf(
                QuizItem(
                    question = "Choose the correct sentence for yesterday:",
                    options = listOf("Yesterday, I went to the library.", "Yesterday, I have gone to the library.", "Yesterday, I go to the library.", "Yesterday, I am gone to library."),
                    correctIndex = 0,
                    explanation = "'Yesterday' requires Past Simple tense: 'went'.",
                    explanationBangla = "নির্দিষ্ট অতীত সময় 'Yesterday' থাকলে Past Simple (went) হবে।"
                ),
                QuizItem(
                    question = "Which sentence asks about life experience?",
                    options = listOf("Have you ever been to Japan?", "Did you ever been to Japan?", "Are you went to Japan?", "Have you go to Japan?"),
                    correctIndex = 0,
                    explanation = "'Have you ever been to...?' is used for life experiences.",
                    explanationBangla = "জীবনের অভিজ্ঞতার কথা জানতে 'Have you ever been to...?' ব্যবহৃত হয়।"
                )
            )
        ),
        LessonItem(
            id = "lesson_5",
            categoryId = "ielts",
            title = "IELTS Speaking Part 1: Hobbies & Leisure",
            subtitle = "Answer fluently with descriptive vocabulary",
            durationMinutes = 10,
            xpReward = 50,
            isLocked = false,
            isCompleted = false,
            level = "Upper-Intermediate",
            overview = "Avoid 1-word answers. Learn to extend your answers using reason + example.",
            banglaTip = "আইইএলটিএস স্পিকিং-এ 'Yes' বা 'No' বলে থেমে যাবেন না, কেন এবং একটি উদাহরণ দিয়ে বাক্য বাড়ান।",
            dialogueLines = listOf(
                DialogueLine("Nora", "Examiner: Alex, what do you usually do in your spare time?", "পরীক্ষক: অবসর সময়ে সাধারণত কী করতে ভালোবাসেন?"),
                DialogueLine("You", "Well, whenever I get a breather, I'm quite keen on photography.", "সত্যি বলতে, সুযোগ পেলেই আমি ছবি তুলতে পছন্দ করি।"),
                DialogueLine("Nora", "Excellent vocabulary! 'Keen on' and 'get a breather' boost your lexical score.", "চমৎকার শব্দচয়ন! 'Keen on' শব্দটি আপনার স্কোর বাড়িয়ে দেবে।")
            ),
            keyPhrases = listOf(
                KeyPhrase("To be keen on sth", "To really like or be passionate about something", "I'm keen on reading thrillers."),
                KeyPhrase("Whenever I get a breather", "When I get free time to relax", "Whenever I get a breather, I call my family.")
            ),
            quizQuestions = listOf(
                QuizItem(
                    question = "What does 'I am keen on photography' mean?",
                    options = listOf("I am very interested in photography", "I hate photography", "I am bad at photography", "I teach photography"),
                    correctIndex = 0,
                    explanation = "'Keen on' means enthusiastic about or deeply fond of something.",
                    explanationBangla = "'Keen on' অর্থ কোনো বিষয়ে অত্যন্ত আগ্রহী বা পছন্দ করা।"
                )
            )
        )
    )

    val sampleVocabWords = listOf(
        VocabWord(
            id = "v1",
            word = "Eloquent",
            phonetic = "/ˈel.ə.kwənt/",
            partOfSpeech = "Adjective",
            definition = "Fluent or persuasive in speaking or writing.",
            definitionBangla = "সাবলীল ও হৃদয়গ্রাহী বক্তা বা লেখনী।",
            exampleSentence = "She delivered an eloquent speech that moved the entire audience.",
            category = "Speaking & Writing",
            isFavorite = true,
            masteryLevel = 2
        ),
        VocabWord(
            id = "v2",
            word = "Articulate",
            phonetic = "/ɑːˈtɪk.jə.lət/",
            partOfSpeech = "Adjective / Verb",
            definition = "Having or showing the ability to speak fluently and coherently.",
            definitionBangla = "স্পষ্ট ও সুন্দরভাবে মনের ভাব প্রকাশ করতে সক্ষম।",
            exampleSentence = "He was articulate and expressed his team's goals with total clarity.",
            category = "Professional English",
            isFavorite = false,
            masteryLevel = 3
        ),
        VocabWord(
            id = "v3",
            word = "Persevere",
            phonetic = "/ˌpɜː.sɪˈvɪər/",
            partOfSpeech = "Verb",
            definition = "Continue in a course of action even in the face of difficulty.",
            definitionBangla = "প্রতিকূলতা সত্ত্বেও অধ্যবসায় চালিয়ে যাওয়া।",
            exampleSentence = "If you persevere with daily speaking practice, fluency will come naturally.",
            category = "Mindset & Growth",
            isFavorite = true,
            masteryLevel = 2
        ),
        VocabWord(
            id = "v4",
            word = "Meticulous",
            phonetic = "/məˈtɪk.jə.ləs/",
            partOfSpeech = "Adjective",
            definition = "Showing great attention to detail; very careful and precise.",
            definitionBangla = "খুঁটিনাটি বিষয়ে অত্যন্ত সতর্ক ও নিখুঁত।",
            exampleSentence = "Nora commended his meticulous attention to English grammar rules.",
            category = "Academic & IELTS",
            isFavorite = false,
            masteryLevel = 1
        ),
        VocabWord(
            id = "v5",
            word = "Break the ice",
            phonetic = "/breɪk ðiː aɪs/",
            partOfSpeech = "Idiom",
            definition = "Do or say something to relieve tension or get conversation started.",
            definitionBangla = "কথোপকথনের জড়তা বা অস্বস্তি দূর করা।",
            exampleSentence = "Telling a light-hearted joke helped break the ice at the job interview.",
            category = "Everyday Idioms",
            isFavorite = true,
            masteryLevel = 3
        ),
        VocabWord(
            id = "v6",
            word = "Serendipity",
            phonetic = "/ˌser.ənˈdɪp.ə.ti/",
            partOfSpeech = "Noun",
            definition = "The occurrence and development of events by chance in a happy way.",
            definitionBangla = "হঠাৎ অপ্রত্যাশিতভাবে ঘটে যাওয়া চমৎকার বা আনন্দদায়ক ঘটনা।",
            exampleSentence = "Meeting my English study partner at the airport bookstore was pure serendipity.",
            category = "Advanced Vocabulary",
            isFavorite = false,
            masteryLevel = 1
        ),
        VocabWord(
            id = "v7",
            word = "Pragmatic",
            phonetic = "/præɡˈmæt.ɪk/",
            partOfSpeech = "Adjective",
            definition = "Dealing with things sensibly and realistically in a practical way.",
            definitionBangla = "বাস্তবধর্মী বা প্রায়োগিক দৃষ্টিভঙ্গিসম্পন্ন।",
            exampleSentence = "Nora takes a pragmatic approach: practice speaking rather than memorizing dry rules.",
            category = "Business & Career",
            isFavorite = true,
            masteryLevel = 2
        ),
        VocabWord(
            id = "v8",
            word = "Versatile",
            phonetic = "/ˈvɜː.sə.taɪl/",
            partOfSpeech = "Adjective",
            definition = "Able to adapt or be adapted to many different functions or activities.",
            definitionBangla = "বহুমুখী প্রতিভাধর বা নানামুখী কাজে উপযোগী।",
            exampleSentence = "English is an exceptionally versatile language spoken worldwide.",
            category = "Power Words",
            isFavorite = false,
            masteryLevel = 2
        )
    )

    val grammarLessons = listOf(
        GrammarLesson(
            id = "g1",
            title = "Mastering Articles: A, An, & The",
            tag = "Essential Grammar",
            level = "Beginner to Intermediate",
            explanation = "Use 'a/an' for general singular countable nouns. Use 'the' when both speaker and listener know which specific item is meant.",
            banglaExplanation = "সাধারণ অনির্দিষ্ট কোনো একবচন বস্তুর আগে 'a/an' বসে। কিন্তু নির্দিষ্ট কোনো বস্তু বোঝাতে 'the' ব্যবহৃত হয়।",
            rules = listOf(
                "Use 'an' before vowel sounds (an hour, an apple, an umbrella).",
                "Use 'a' before consonant sounds (a university, a cat, a European country).",
                "Use 'the' for unique entities (the sun, the moon, the internet)."
            ),
            examples = listOf(
                Pair("I saw a dog in the park.", "The dog was playing with a red ball."),
                Pair("She is an honest person.", "'Honest' starts with a silent 'h', hence 'an'.")
            ),
            commonMistake = Pair("She is a honest worker.", "She is an honest worker. (Honest starts with vowel sound 'o')"),
            questions = listOf(
                QuizItem(
                    question = "He has been waiting for ______ hour.",
                    options = listOf("an", "a", "the", "none"),
                    correctIndex = 0,
                    explanation = "'Hour' begins with a silent 'h' and a vowel sound 'ow-er', requiring 'an'.",
                    explanationBangla = "'Hour'-এর উচ্চারণ স্বরবর্ণের মতো শুরু হয়, তাই 'an' হবে।"
                ),
                QuizItem(
                    question = "She goes to ______ university in London.",
                    options = listOf("a", "an", "the", "none"),
                    correctIndex = 0,
                    explanation = "'University' begins with a consonant 'y' sound (/juː/), requiring 'a'.",
                    explanationBangla = "'University'-এর উচ্চারণ 'ইউ' দিয়ে শুরু হয় (ব্যঞ্জনধ্বনি), তাই 'a' হবে।"
                )
            )
        ),
        GrammarLesson(
            id = "g2",
            title = "Modal Verbs: Can, Could, Would, & Should",
            tag = "Fluency & Politeness",
            level = "Intermediate",
            explanation = "Modal verbs convey politeness, possibility, ability, and advice. 'Could' and 'Would' make your English instantly sound courteous and professional.",
            banglaExplanation = "ভদ্রভাবে কোনো প্রস্তাব বা অনুরোধ জানাতে 'Could' ও 'Would' ব্যবহার করা সবচেয়ে সুন্দর উপায়।",
            rules = listOf(
                "Use 'Could you...' for polite requests.",
                "Use 'Would you like...' to offer something kindly.",
                "Use 'Should' to give constructive advice.",
                "Never add '-s' or 'to' directly after a modal verb (e.g. He can swim, NOT He cans to swim)."
            ),
            examples = listOf(
                Pair("Could you please repeat that?", "Much more courteous than 'Repeat that!'"),
                Pair("Would you care for a cup of tea?", "Warm and hospitable way to offer.")
            ),
            commonMistake = Pair("He must to go now.", "He must go now. (No 'to' after modal verbs)"),
            questions = listOf(
                QuizItem(
                    question = "Which request sounds most polite in a business meeting?",
                    options = listOf("Could you possibly clarify that point?", "Clarify that point right now.", "You must clarify that.", "Tell me again."),
                    correctIndex = 0,
                    explanation = "'Could you possibly...' is high-level polite business English.",
                    explanationBangla = "'Could you possibly...' পেশাদার আলোচনার জন্য সর্বোচ্চ শালীন বাক্য।"
                )
            )
        )
    )

    val pronunciationSentences = listOf(
        PronunciationExercise(
            id = "p1",
            sentence = "The thoughtful teacher thoroughly taught throughout the year.",
            phonetic = "/ðə ˈθɔːt.fəl ˈtiː.tʃər ˈθʌr.ə.li tɔːt θruːˈaʊt ðə jɪər/",
            focusSound = "Voiced vs Unvoiced 'TH' (/θ/ vs /ð/)",
            tip = "Place the tip of your tongue lightly between your teeth and blow gently for /θ/.",
            difficulty = "Challenging"
        ),
        PronunciationExercise(
            id = "p2",
            sentence = "She sells special seashells by the sunny seashore.",
            phonetic = "/ʃiː sɛlz ˈspɛʃ.əl ˈsiː.ʃɛlz baɪ ðə ˈsʌn.i ˈsiː.ʃɔːr/",
            focusSound = "/s/ vs /ʃ/ (S vs SH sound distinction)",
            tip = "For /s/, teeth are close together; for /ʃ/, round your lips slightly outward.",
            difficulty = "Medium"
        ),
        PronunciationExercise(
            id = "p3",
            sentence = "Could you please pass the water bottle?",
            phonetic = "/kʊd juː pliːz pɑːs ðə ˈwɔː.tər ˈbɒt.əl/",
            focusSound = "Natural linking: 'Could you' -> /kʊdʒuː/",
            tip = "Link the 'd' and 'y' smoothly so it sounds like 'Could-jew'.",
            difficulty = "Practical"
        ),
        PronunciationExercise(
            id = "p4",
            sentence = "I am really excited to improve my English fluency with Nora.",
            phonetic = "/aɪ æm ˈrɪə.li ɪkˈsaɪ.tɪd tuː ɪmˈpruːv maɪ ˈɪŋ.ɡlɪʃ ˈfluː.ən.si wɪð ˈnɔː.rə/",
            focusSound = "R rhythm & V vowel clarity",
            tip = "Keep the jaw relaxed and pronounce 'flu-en-cy' with three crisp beats.",
            difficulty = "Confidence Builder"
        )
    )

    val achievements = listOf(
        AchievementItem("ach_1", "First Step", "Complete your first English lesson with Nora", "🎯", true, 1.0f, 50),
        AchievementItem("ach_2", "Daily Dedication", "Maintain a 3-day learning streak", "🔥", true, 1.0f, 100),
        AchievementItem("ach_3", "Word Collector", "Add 20 new words to your vocabulary vault", "📚", true, 1.0f, 80),
        AchievementItem("ach_4", "Speaking Star", "Practice 15 minutes of live conversation with AI", "🎙️", true, 1.0f, 120),
        AchievementItem("ach_5", "Grammar Master", "Score 100% on three grammar challenges", "⭐", false, 0.66f, 150),
        AchievementItem("ach_6", "Pronunciation Champ", "Achieve 90%+ match on pronunciation lab", "🎧", false, 0.5f, 100)
    )

    val dailyMissions = listOf(
        DailyMission("m1", "Practice 3 minutes of AI Speaking with Nora", 25, 3, 3, true),
        DailyMission("m2", "Flip & review 5 vocabulary flashcards", 20, 5, 5, true),
        DailyMission("m3", "Score at least 80% on a Grammar Academy quiz", 30, 1, 1, true),
        DailyMission("m4", "Complete 1 speech pronunciation workout", 20, 0, 1, false)
    )
}
