package com.ganda.mitrainformatikatechincaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ganda.mitrainformatikatechincaltest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var inputChar : EditText
    private lateinit var inputOperator : EditText
    private lateinit var resultText : TextView
    private lateinit var transformButton : Button

    private val keyboardChar = arrayOf(
        charArrayOf('1','2','3','4','5','6','7','8','9','0'),
        charArrayOf('q','w','e','r','t','y','u','i','o','p'),
        charArrayOf('a','s','d','f','g','h','j','k','l',';'),
        charArrayOf('z','x','c','v','b','n','m',',','.','/'),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        inputChar = binding.editTextInputChar
        inputOperator = binding.editTextInputOperator
        resultText = binding.result
        transformButton = binding.transformButton
        transformButton.setOnClickListener {
            transform()
        }
    }

//    Transform Text
    private fun transform() {
        initializePositionMaps(keyboardChar)

        val input = inputChar.text.toString().trim().toLowerCase()
        val operator = inputOperator.text.toString().trim().toUpperCase()

        val inputArray = input.split(",").filter { it.isNotEmpty() }.map { it[0] }.toCharArray()
        val operatorArray = operator.split(",").filter { it.isNotEmpty() }.map { it[0] }.toCharArray()

        var result = arrayOf("")

//        input array and operator array each

        inputArray.forEach {value ->
            operatorArray.forEach {
                var output = ""
                if (operator == "H") {
                    output = transformHorizontal(char = value, layout = keyboardChar).toString()
                } else if (operator == "V") {
                    output = transformVertical(char = value, layout = keyboardChar).toString()
                } else if (operator.toIntOrNull() != null && operator.toInt() < 10) {
                    output = shift(char = value, layout = keyboardChar, shiftStep = operator.toInt()).toString()
                }
                result += output
            }
        }
    var text = ""
    result.forEach { string ->
        text += string
    }

    resultText.text = text
    }


    val rowMap: MutableMap<Char, Int> = mutableMapOf()
    val colMap: MutableMap<Char, Int> = mutableMapOf()

    private fun initializePositionMaps(layout: Array<CharArray>) {
        for (rowIndex in layout.indices) {
            for (colIndex in layout[rowIndex].indices) {
                val char = layout[rowIndex][colIndex]
                rowMap[char] = rowIndex
                colMap[char] = colIndex
            }
        }
    }

    private fun shift(char: Char, layout: Array<CharArray>, shiftStep: Int): Char {

        var row = rowMap[char]
        var col = colMap[char]?.plus(shiftStep)

        if (row != null) {
            if (col.toString().toInt() > 9) {
                row += 1
                col = col?.plus(-10)
            }
        }

        if (row!! > 3) {
            row -= 4
        }

        return layout[row][col!!]
    }

    private fun transformHorizontal( char: Char, layout: Array<CharArray>) : Char {
        val row = rowMap[char]
        val col = colMap[char]

        return layout[3 - row.toString().toInt()][col.toString().toInt()]
    }

    private fun transformVertical (char: Char, layout: Array<CharArray>) : Char {
        val row = rowMap[char] ?: return char
        val col = colMap[char] ?: return char

        return layout[row.toString().toInt()][9 - col.toString().toInt()]
    }


}