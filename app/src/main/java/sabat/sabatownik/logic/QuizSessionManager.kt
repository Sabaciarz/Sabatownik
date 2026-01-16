package sabat.sabatownik.logic

import sabat.sabatownik.data.room.AnswerEntity
import sabat.sabatownik.data.room.QuestionEntity

class QuizSessionManager {

    var correctCount = 0
    var wrongCount = 0
    var masteredCount = 0

    fun evaluate(
        question: QuestionEntity,
        answers: List<AnswerEntity>,
        selectedIndexes: Set<Int>
    ): Boolean {

        var anyError = false

        answers.forEachIndexed { index, answer ->
            if (answer.isCorrect && index !in selectedIndexes) anyError = true
            if (!answer.isCorrect && index in selectedIndexes) anyError = true
        }

        if (anyError) {
            wrongCount++
        } else {
            correctCount++
            question.remainingRepeats--
            if (question.remainingRepeats == 0) {
                question.mastered = true
                masteredCount++
            }
        }
        return !anyError
    }
}
