package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

// Conustant for storing the pending operation
private const val STATE_PENDING_OPERATION = "PendingOperation"

// Consttant for storing the first operand value
private const val STATE_OPERAND1 = "Operand1"

// Boolean flag to Check if operand1 was saved
private const val STATE_OPERAND1_STROED = "Operand1_Stroed"

class MainActivity : AppCompatActivity() {
    private lateinit var bindinig: ActivityMainBinding
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { bindinig.operation }


    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendinigOperation = "="

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindinig = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(bindinig.root)
        bindinig.imageView?.setImageResource(R.drawable.yazi)

        // Operation buttons
        val listener = View.OnClickListener { v ->
            val b = v as Button
            bindinig.newNumber.append(b.text)
        }
        bindinig.button0.setOnClickListener(listener)
        bindinig.button1.setOnClickListener(listener)
        bindinig.button2.setOnClickListener(listener)
        bindinig.button3.setOnClickListener(listener)
        bindinig.button4.setOnClickListener(listener)
        bindinig.button5.setOnClickListener(listener)
        bindinig.button6.setOnClickListener(listener)
        bindinig.button7.setOnClickListener(listener)
        bindinig.button8.setOnClickListener(listener)
        bindinig.button9.setOnClickListener(listener)
        bindinig.buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = bindinig.newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                bindinig.newNumber.setText("")
            }
            pendinigOperation = op
            displayOperation.text = pendinigOperation
        }
        bindinig.btnClear!!.setOnClickListener {
            bindinig.result.setText("")
            operand1 = null
            pendinigOperation = "="
            displayOperation.text = pendinigOperation
        }

        bindinig.btnDelete!!.setOnClickListener {
            val text = bindinig.newNumber.text.toString()
            if (text.isNotEmpty()) {
                bindinig.newNumber.setText(
                    text.substring(
                        0,
                        text.length - 1
                    )
                )
                bindinig.newNumber.setSelection(bindinig.newNumber.text.length)  // (29) Kursoru sona qoyur
            }
        }
        bindinig.buttonEquals.setOnClickListener(opListener)
        bindinig.buttonDivide.setOnClickListener(opListener)
        bindinig.buttonMultiply.setOnClickListener(opListener)
        bindinig.buttonMinus.setOnClickListener(opListener)
        bindinig.buttonPlus.setOnClickListener(opListener)

        bindinig.buttonNeg?.setOnClickListener {
            val value = bindinig.newNumber.text.toString()
            if (value.isEmpty()) {
                bindinig.newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    bindinig.newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    //newNumber was "-" or ".", so clear it
                    bindinig.newNumber.setText("")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendinigOperation == "=") {
                pendinigOperation = operation
            }
            when (pendinigOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN  // handle attemp to divide by zero
                } else {
                    operand1!! / value
                }

                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }

        bindinig.result.setText(operand1.toString())
        bindinig.newNumber.setText("")
    }

    // metod - Saves the users input values and the selected operation.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STROED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendinigOperation)
    }

    // method - Restores saved values
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 =
            if (savedInstanceState.getBoolean(
                    STATE_OPERAND1_STROED,
                    false
                )
            ) savedInstanceState.getDouble(STATE_OPERAND1) else null
        pendinigOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text = pendinigOperation
    }
}