package com.aehimn.assignmenttwo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aehimn.assignmenttwo.R
import com.aehimn.assignmenttwo.model.Question

class QuestionAdapter(private val questionsList: ArrayList<Question>) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById<TextView>(R.id.questionTitle);
        val questionAnswer: TextView = itemView.findViewById<TextView>(R.id.questionAnswer);
        val questionValue: TextView = itemView.findViewById<TextView>(R.id.questionValue);
        val questionCreated: TextView = itemView.findViewById<TextView>(R.id.questionCreated);
        val questionCategoryTitle: TextView = itemView.findViewById<TextView>(R.id.questionCategoryTitle);
    }

    override fun getItemCount(): Int {
        return questionsList.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context);
        val questionView = inflater.inflate(R.layout.question_list_element, parent, false);
        return ViewHolder(questionView);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val questionElement: Question = questionsList[position];

        val question = viewHolder.question;
        "INTREBARE: ${questionElement.question}".also { question.text = it };

        val questionAnswer = viewHolder.questionAnswer;
        "RASPUNS: ${questionElement.answer}".also { questionAnswer.text = it };

        val questionValue = viewHolder.questionValue;
        "VALOARE: ${questionElement.value}".also { questionValue.text = it };

        val questionCreated = viewHolder.questionCreated;
        questionCreated.text = "CREATA LA: " + questionElement.created_at;

        val questionCategoryTitle = viewHolder.questionCategoryTitle;
        questionCategoryTitle.text = "CATEGORIE: ${questionElement.categoryTitle}";
    }
}