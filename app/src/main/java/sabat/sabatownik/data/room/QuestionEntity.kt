package sabat.sabatownik.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val correctMask: String,
    var remainingRepeats: Int,
    var mastered: Boolean = false
)
