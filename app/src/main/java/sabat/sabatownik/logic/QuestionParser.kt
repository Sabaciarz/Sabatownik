package sabat.sabatownik.logic

import android.content.Context
import androidx.annotation.RawRes
import sabat.sabatownik.data.model.QuestionWithAnswers
import sabat.sabatownik.data.room.AnswerEntity
import sabat.sabatownik.data.room.QuestionEntity

class QuestionParser(private val context: Context) {

    fun parseFromAssets(
        assetPath: String,
        repeatLimit: Int
    ): List<QuestionWithAnswers> {

        val text = context.assets
            .open(assetPath)
            .bufferedReader()
            .readText()
            .trim()

        if (text.isBlank()) return emptyList()

        val blocks = text.split(Regex("\\n\\s*\\n"))
        val result = mutableListOf<QuestionWithAnswers>()

        for (block in blocks) {
            val lines = block
                .lines()
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            // ⛔ za mało linii na pytanie
            if (lines.size < 3) continue

            // ⛔ brak maski
            if (!lines[0].startsWith("X")) continue

            val mask = lines[0].removePrefix("X")

            // ⛔ maska vs ilość odpowiedzi
            if (mask.length != lines.size - 2) continue

            val questionText = lines[1]

            val answers = lines.drop(2).mapIndexed { index, answerText ->
                AnswerEntity(
                    questionId = 0,
                    text = answerText,
                    isCorrect = mask[index] == '1'
                )
            }

            result.add(
                QuestionWithAnswers(
                    question = QuestionEntity(
                        question = questionText,
                        correctMask = mask,
                        remainingRepeats = repeatLimit
                    ),
                    answers = answers
                )
            )
        }

        return result
    }
}
