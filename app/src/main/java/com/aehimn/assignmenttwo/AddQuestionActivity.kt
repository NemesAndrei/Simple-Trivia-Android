package com.aehimn.assignmenttwo

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aehimn.assignmenttwo.databinding.ActivityAddQuestionBinding
import java.util.*

class AddQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddQuestionBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuestionBinding.inflate(layoutInflater);
        setContentView(binding.root)
        questionFocusListener();
        answerFocusListener();
        valueFocusListener();
        categoryFocusListener();
    }

    private fun questionFocusListener() {
        binding.questionEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.errorQuestion.text = validQuestion();
            }
        }
    }

    private fun validQuestion(): String? {
        val questionText = binding.questionEditText.text.toString();
        if (questionText.isEmpty()) {
            return "Necesar";
        }
        if (questionText.length <= 5) {
            return "Intrebarea trebuie sa contina mai mult de 5 caractere";
        }
        return "";
    }

    private fun answerFocusListener() {
        binding.questionAnswerEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.errorAnswer.text = validAnswer();
            }
        }
    }

    private fun validAnswer(): String? {
        val answerText = binding.questionAnswerEditText.text.toString();
        if (answerText.isEmpty()) {
            return "Necesar";
        }
        if (answerText.length <= 5) {
            return "Raspunsul trebuie sa contina mai mult de 5 caractere";
        }
        return "";
    }

    private fun valueFocusListener() {
        binding.questionValueEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.errorValue.text = validValue();
            }
        }
    }

    private fun validValue(): String? {
        val valueText = binding.questionValueEditText.text.toString();
        if (valueText.isNotEmpty()) {
            val number = valueText.toInt();
            if (number < 50 || number > 150) {
                return "Valoarea introdusa trebuie sa fie intre 50 si 150";
            }
        } else {
            return "Necesar";
        }
        return "";
    }

    private fun categoryFocusListener() {
        binding.questionCategoryEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.errorCategory.text = validCategory();
            }
        }
    }

    private fun validCategory(): String? {
        val categoryText = binding.questionCategoryEditText.text.toString();
        if (categoryText.isNotEmpty()) {
            if (categoryText != "Muzica" && categoryText != "Geografie" && categoryText != "Istorie" && categoryText != "Personalitati") {
                return "Categoria trebuie sa fie Muzica, Geografie, Istorie sau Personalitati";
            }
        } else {
            return "Necesar";
        }
        return "";
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salvati intrebarea?");
        builder.setMessage("Sunteti pe cale sa parasiti pagina de creare a unei noi intrebari. Doriti sa o salvati?");
        builder.setPositiveButton("DA", DialogInterface.OnClickListener() { dialogInterface, i ->
            val validQuestion = binding.errorQuestion.text == "";
            val validAnswer = binding.errorAnswer.text == "";
            val validValue = binding.errorValue.text == "";
            val validCategory = binding.errorCategory.text == "";
            if (validQuestion && validAnswer && validValue && validCategory) {
                val intent = Intent();
                intent.putExtra("question", binding.questionEditText.text.toString());
                intent.putExtra("answer", binding.questionAnswerEditText.text.toString());
                intent.putExtra("value", binding.questionValueEditText.text.toString().toInt());
                intent.putExtra("category", binding.questionCategoryEditText.text.toString());
                intent.putExtra("created", Date().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(
                    this,
                    "Intrebarea nu poate fi salvata deoarece exista erori.",
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
        builder.setNegativeButton("NU", DialogInterface.OnClickListener() { dialogInterface, i ->
            val intent = Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        })
        builder.create().show();
    }
}