package sabat.sabatownik.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import sabat.sabatownik.data.model.QuestionWithAnswers

@Dao
interface QuestionDao {

    @Transaction
    @Query("""
        SELECT * FROM questions
        WHERE mastered = 0 AND remainingRepeats > 0
        ORDER BY RANDOM()
        LIMIT 1
    """)
    suspend fun getRandomQuestion(): QuestionWithAnswers?

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Insert
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

    @Insert
    suspend fun insertAnswers(answers: List<AnswerEntity>)

    @Query("DELETE FROM questions")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE remainingRepeats = 0")
    suspend fun getMasteredCount(): Int
}
