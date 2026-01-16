package sabat.sabatownik.data.model

import androidx.room.Embedded
import androidx.room.Relation
import sabat.sabatownik.data.room.AnswerEntity
import sabat.sabatownik.data.room.QuestionEntity

data class QuestionWithAnswers(
    @Embedded val question: QuestionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val answers: List<AnswerEntity>
)
