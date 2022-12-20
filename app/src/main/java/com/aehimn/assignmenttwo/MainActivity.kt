package com.aehimn.assignmenttwo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aehimn.assignmenttwo.adapter.QuestionAdapter
import com.aehimn.assignmenttwo.model.Question
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val questionsList: ArrayList<Question> = ArrayList();
    private val questionAdapter = QuestionAdapter(questionsList);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadButton = findViewById<Button>(R.id.questionButton);
        downloadButton.setOnClickListener(this);


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions);
        recyclerView.adapter = questionAdapter;
        recyclerView.layoutManager = LinearLayoutManager(this);
    }


    override fun onClick(v: View?) {

        val noOfQuestionsEditText = findViewById<EditText>(R.id.editTextQuestionNumber);
        if (noOfQuestionsEditText.text.isEmpty()) {
            Toast.makeText(this, "Numarul de intrebari nu poate fi gol!", Toast.LENGTH_LONG).show();
        } else {
            val noOfQuestions = noOfQuestionsEditText.text.toString().toInt();
            if (noOfQuestions < 1 || noOfQuestions > 100) {
                Toast.makeText(
                    this,
                    "Valoarea introdusa trebuie sa fie intre 1 si 100!",
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                val loadingAnimation = findViewById<ImageView>(R.id.loadingImageView);
                loadingAnimation.visibility = View.VISIBLE;

                questionAdapter.notifyItemRangeRemoved(0, questionsList.size);
                questionsList.removeAll(questionsList.toSet());

                showLoadingAnimation(loadingAnimation, noOfQuestions, 3000);
            }
        }
    }

    private fun showLoadingAnimation(
        loadingAnimation: ImageView,
        noOfQuestions: Int,
        duration: Long
    ) {
        object : CountDownTimer(duration, 2000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                loadingAnimation.visibility = View.INVISIBLE;
                getQuestions(noOfQuestions);
            }
        }.start()
    }

    fun getQuestions(noOfQuestions: Int) {

        val url = "https://jservice.io/api/random?count=$noOfQuestions";

        val requestQueue = Volley.newRequestQueue(this);

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->

            for (i in 0 until response.length()) {
                val questionObject = response.getJSONObject(i);

                val question = questionObject.getString("question");

                val questionAnswer = questionObject.getString("answer");

                var questionValue = 0;
                if (questionObject.getString("value") != "null") {
                    questionValue = questionObject.getString("value").toInt();
                }

                val questionCreated = questionObject.getString("created_at");

                val questionCategoryTitle = questionObject.getJSONObject("category").getString("title");

                questionsList.add(
                    Question(
                        question,
                        questionAnswer,
                        questionValue,
                        questionCreated,
                        questionCategoryTitle
                    )
                );

                questionAdapter.notifyItemInserted(i);
            }
        }, { })
        requestQueue.add(jsonArrayRequest);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.addQuestionMenuItem) {
            val intent = Intent(this, AddQuestionActivity::class.java);
            activityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item)
    }

    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {

            val intent: Intent? = result.data;

            val question = intent?.getStringExtra("question");

            val answer = intent?.getStringExtra("answer");

            val value = intent?.getIntExtra("value", 0);

            val category = intent?.getStringExtra("category");

            val created = intent?.getStringExtra("created");

            if (question != null && answer != null && value != null && category != null && created != null) {
                questionsList.add(0, Question(question, answer, value, created, category));
                questionAdapter.notifyItemInserted(0);
            }

        }

        if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(
                this,
                "Intrebarea nu a fost salvata deoarece ati selectat varianta NU",
                Toast.LENGTH_SHORT
            ).show();
        }

    }
}