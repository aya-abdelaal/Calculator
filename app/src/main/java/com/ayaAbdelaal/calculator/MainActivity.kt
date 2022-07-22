package com.ayaAbdelaal.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ayaAbdelaal.calculator.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var expr: String
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expr = ""

        binding.number0.setOnClickListener { updateExpression('0') }
        binding.number1.setOnClickListener { updateExpression('1') }
        binding.number2.setOnClickListener { updateExpression('2') }
        binding.number3.setOnClickListener { updateExpression('3') }
        binding.number4.setOnClickListener { updateExpression('4') }
        binding.number5.setOnClickListener { updateExpression('5') }
        binding.number6.setOnClickListener { updateExpression('6') }
        binding.number7.setOnClickListener { updateExpression('7') }
        binding.number8.setOnClickListener { updateExpression('8') }
        binding.number9.setOnClickListener { updateExpression('9') }
        binding.add.setOnClickListener { updateExpression('+') }
        binding.subtract.setOnClickListener { updateExpression('-') }
        binding.multiply.setOnClickListener { updateExpression('*') }
        binding.divide.setOnClickListener { updateExpression('/') }
        binding.leftBracket.setOnClickListener { updateExpression('(') }
        binding.rightBracket.setOnClickListener { updateExpression(')') }
        binding.clear.setOnClickListener {
            expr = ""
            updateResult("")
        }
        binding.evaluate.setOnClickListener {
            try {
                val res = evaluate(toPostFix(expr))
                updateResult(res.toString())
                expr = res.toString()
            }catch(e : Exception){
                updateResult("Error : Invalid expression")
            }
        }

    }

    fun toPostFix(expr: String): ArrayList<String> {
        val res = ArrayList<String>()
        var temp = ""
        val stack = Stack<Char>()

        //TODO: validate brackets

        for (i in (0 until expr.length)) {
            var x = expr[i]
            if (x.isDigit())
                temp += x
            else
                if (x == '(')
                    stack.push(x)
                else {
                    res.add(temp)
                    temp = ""
                    if (x == ')') {
                        while (!stack.isEmpty() && stack.peek() != '(')
                            res.add(stack.pop().toString())
                        stack.pop()
                    } else {
                        while (!stack.isEmpty() && operatorPrecedence(x, stack.peek()))
                            res.add(stack.pop().toString())
                        stack.push(x)
                    }
                }
        }
        if (temp != "")
            res.add(temp)
        while (!stack.isEmpty())
            res.add(stack.pop().toString())
        Log.d(TAG, "POSTFIX DONE :" + res.toString())
        return res
    }

    fun operatorPrecedence(x: Char, top: Char): Boolean {
        return ((x == '+' || x == '-') && (top == '*' || top == '/'))

    }

    fun evaluate(expr: ArrayList<String>): Int {
        val res: Int
        val stack = Stack<Int>()
        for (i in (0 until expr.size)) {
            var x = expr[i]
            if (x.toIntOrNull() != null)
                stack.push(x.toInt())
            else when (x) {
                "+" -> stack.push(stack.pop() + stack.pop())
                "-" -> {
                    val temp = stack.pop()
                    stack.push(stack.pop() - temp)
                }
                "*" -> stack.push(stack.pop() * stack.pop())
                "/" -> {
                    val temp = stack.pop()
                    if (temp == 0)
                        throw Exception("Invalid expression")
                    stack.push(stack.pop() / temp)
                }
            }
        }
        res = stack.pop()
        Log.d(TAG , "STACK :" + stack.toString())
        if(!stack.isEmpty())
            throw Exception("Invalid expression")
        return res
    }

    fun updateExpression(x: Char) {
        expr += x
        updateResult(expr)
    }

    fun updateResult(res: String) {
        binding.result.text = res
    }
}

