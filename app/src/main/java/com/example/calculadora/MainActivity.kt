package com.example.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.mvel2.MVEL


class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private var expresionMatematica = ""
    private var lastResult = ""
    private var hasPerformedOperation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        val buttonClickListener = View.OnClickListener { v ->
            val button = v as Button
            val buttonText = button.text.toString()
            expresionMatematica += buttonText
            updateTextView()
        }

        val botones = arrayOf(
            R.id.boton_0, R.id.boton_1, R.id.boton_2, R.id.boton_3,
            R.id.boton_4, R.id.boton_5, R.id.boton_6, R.id.boton_7,
            R.id.boton_8, R.id.boton_9, R.id.boton_coma, R.id.boton_C,
            R.id.boton_del, R.id.boton_dividir, R.id.boton_multiplicar, R.id.boton_sumar,
            R.id.boton_restar, R.id.boton_igual, R.id.boton_elevado
        )

        for (buttonId in botones) {
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener(buttonClickListener)
        }

        val clearButton = findViewById<Button>(R.id.boton_C)
        clearButton.setOnClickListener {
            expresionMatematica = ""
            updateTextView()
        }

        val equalButton = findViewById<Button>(R.id.boton_igual)
        equalButton.setOnClickListener {
            try {
                val result = calculateResult()
                lastResult = result
                expresionMatematica = result
                hasPerformedOperation = true
                updateTextView(result)
            } catch (e: Exception) {
                updateTextView("Error")
            }
        }
        val delButton = findViewById<Button>(R.id.boton_del)
        delButton.setOnClickListener {
            if (expresionMatematica.isNotEmpty()) {
                expresionMatematica = expresionMatematica.substring(0, expresionMatematica.length - 1)
                updateTextView()
            }
        }
    }

    private fun updateTextView(text: String = "") {
        textView.text = if (text.isNotEmpty()) text else expresionMatematica
    }

    private fun calculateResult(): String {
        try {
            val compiledExpression = MVEL.compileExpression(expresionMatematica)
            val result = MVEL.executeExpression(compiledExpression)
            return result.toString()
        } catch (e: Exception) {
            throw e
        }
    }
}
