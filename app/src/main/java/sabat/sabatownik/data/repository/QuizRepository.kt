package sabat.sabatownik.data.repository

import android.content.Context
import androidx.annotation.RawRes
import sabat.sabatownik.data.model.QuestionWithAnswers
import sabat.sabatownik.data.room.AppDatabase
import sabat.sabatownik.data.room.QuestionEntity
import sabat.sabatownik.logic.QuestionParser

class QuizRepository(
    private val db: AppDatabase,
    private val context: Context
) {

    suspend fun loadBaseFromAssets(
        baseName: String,
        repeatLimit: Int
    ) {
        db.questionDao().clearAll()

        val parser = QuestionParser(context)
        val basePath = "bazy/$baseName"

        val files = context.assets.list(basePath) ?: emptyArray()

        val allQuestions = mutableListOf<QuestionWithAnswers>()

        files.forEach { file ->
            val parsed = parser.parseFromAssets(
                "$basePath/$file",
                repeatLimit
            )
            allQuestions.addAll(parsed)
        }

        val questionIds = db.questionDao()
            .insertQuestions(allQuestions.map { it.question })

        val answers = allQuestions.flatMapIndexed { index, q ->
            q.answers.map {
                it.copy(questionId = questionIds[index])
            }
        }

        db.questionDao().insertAnswers(answers)
    }

    suspend fun getNextQuestion(): QuestionWithAnswers? =
        db.questionDao().getRandomQuestion()

    suspend fun updateQuestion(question: QuestionEntity) =
        db.questionDao().updateQuestion(question)

    suspend fun getTotalQuestions(): Int =
        db.questionDao().getTotalCount()

    suspend fun getMasteredQuestions(): Int =
        db.questionDao().getMasteredCount()

}
