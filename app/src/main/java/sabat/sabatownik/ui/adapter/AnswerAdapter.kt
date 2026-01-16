package sabat.sabatownik.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sabat.sabatownik.R
import sabat.sabatownik.data.room.AnswerEntity

class AnswerAdapter(
    private val answers: List<AnswerEntity>
) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()
    private var reviewMode = false

    fun enableReviewMode() {
        reviewMode = true
        notifyDataSetChanged()
    }

    fun getSelectedPositions(): Set<Int> = selectedPositions

    inner class AnswerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.answerText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun getItemCount() = answers.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers[position]
        holder.text.text = answer.text

        val isSelected = selectedPositions.contains(position)

        // --- KLIK ---
        holder.itemView.setOnClickListener {
            if (reviewMode) return@setOnClickListener

            if (isSelected) {
                selectedPositions.remove(position)
            } else {
                selectedPositions.add(position)
            }
            notifyItemChanged(position)
        }

        // --- KOLORY ---
        val color = getAnswerColor(
            isSelected = isSelected,
            isCorrect = answer.isCorrect,
            reviewMode = reviewMode
        )

        holder.itemView.setBackgroundColor(color)
    }

    private fun getAnswerColor(
        isSelected: Boolean,
        isCorrect: Boolean,
        reviewMode: Boolean
    ): Int {
        if (!reviewMode) {
            return if (isSelected) Color.LTGRAY else Color.TRANSPARENT
        }

        return when {
            isSelected && isCorrect -> Color.GREEN
            !isSelected && isCorrect -> Color.YELLOW
            isSelected && !isCorrect -> Color.RED
            else -> Color.TRANSPARENT
        }
    }
}
