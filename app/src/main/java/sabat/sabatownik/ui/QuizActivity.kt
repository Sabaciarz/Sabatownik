package sabat.sabatownik.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import sabat.sabatownik.R
import sabat.sabatownik.data.room.AnswerEntity
import sabat.sabatownik.data.room.QuestionEntity
import sabat.sabatownik.logic.QuizSessionManager
import sabat.sabatownik.ui.adapter.AnswerAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import sabat.sabatownik.data.repository.QuizRepository
import sabat.sabatownik.data.room.AppDatabase

enum class ScreenState {
    ANSWERING,
    REVIEW
}
class QuizActivity : AppCompatActivity() {

    private lateinit var adapter: AnswerAdapter
    private lateinit var questionText: TextView
    private lateinit var checkButton: Button

    private lateinit var session: QuizSessionManager
    private lateinit var currentQuestion: QuestionEntity
    private lateinit var answers: List<AnswerEntity>
    private lateinit var correctCounter: TextView
    private lateinit var wrongCounter: TextView
    private lateinit var repository: QuizRepository
    private lateinit var answersRecycler: RecyclerView

    private lateinit var masteredCounter: TextView
    private var totalQuestions = 0




    private var state = ScreenState.ANSWERING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        answersRecycler = findViewById(R.id.answersRecycler)
        questionText = findViewById(R.id.questionText)
        checkButton = findViewById(R.id.checkButton)

        correctCounter = findViewById(R.id.correctCounter)
        wrongCounter = findViewById(R.id.wrongCounter)
        masteredCounter = findViewById(R.id.masteredCounter)


        answersRecycler.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.exitButton).setOnClickListener {
            finish()
        }

        session = QuizSessionManager()
        state = ScreenState.ANSWERING

        checkButton.setOnClickListener {
            when (state) {
                ScreenState.ANSWERING -> showReview()
                ScreenState.REVIEW -> loadQuestion()
            }
        }

        val baseName = intent.getStringExtra("BASE_NAME") ?: return
        val limit = intent.getIntExtra("LIMIT", 1)

        val db = AppDatabase.get(this)
        repository = QuizRepository(db, this)

        lifecycleScope.launch {
            repository.loadBaseFromAssets(baseName, limit)

            totalQuestions = repository.getTotalQuestions()
            updateMasteredCounter()

            loadQuestion()
        }


    }

    private fun showReview() {
        adapter.enableReviewMode()

        val correct = session.evaluate(
            currentQuestion,
            answers,
            adapter.getSelectedPositions()
        )

        lifecycleScope.launch {
            repository.updateQuestion(currentQuestion)
            updateMasteredCounter()
        }

        correctCounter.text = "✔ ${session.correctCount}"
        wrongCounter.text = "✖ ${session.wrongCount}"

        state = ScreenState.REVIEW
        checkButton.text = "Dalej"
    }


    private fun loadQuestion() {
        lifecycleScope.launch {
            val qwa = repository.getNextQuestion()

            if (qwa == null) {
                Toast.makeText(
                    this@QuizActivity,
                    "Koniec sesji",
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return@launch
            }

            currentQuestion = qwa.question
            answers = qwa.answers

            adapter = AnswerAdapter(answers)

            answersRecycler.adapter = adapter
            questionText.text = currentQuestion.question

            state = ScreenState.ANSWERING
            checkButton.text = "Sprawdź"
        }

    }
    private suspend fun updateMasteredCounter() {
        val mastered = repository.getMasteredQuestions()
        masteredCounter.text = "Opanowane: $mastered / $totalQuestions"
    }




}
