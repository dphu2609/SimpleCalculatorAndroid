package com.example.myapplication

import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.tan

class CalculatorStack {
    class Component {
        enum class Type {
            OPERATOR, NUMBER
        }

        var type: Type? = null
        var op: String? = null
        var num: Double? = null
    }

    private fun postfixTransform(input: MutableList<String>): MutableList<Component> {
        val opStack = mutableListOf<String>()
        val postfixForm = mutableListOf<Component>()

        for (component in input) { // create postfix form
            when (component) {
                "+", "-" -> {
                    while (opStack.isNotEmpty() && (opStack.last() == "+" || opStack.last() == "-" || opStack.last() == "x" || opStack.last() == "/")) {
                        val opTop = opStack.removeAt(opStack.size - 1)
                        val comp = Component().apply {
                            type = Component.Type.OPERATOR
                            op = opTop
                        }
                        postfixForm.add(comp)
                    }
                    opStack.add(component)
                }
                "x", "/" -> {
                    while (opStack.isNotEmpty() && (opStack.last() == "x" || opStack.last() == "/")) {
                        val opTop = opStack.removeAt(opStack.size - 1)
                        val comp = Component().apply {
                            type = Component.Type.OPERATOR
                            op = opTop
                        }
                        postfixForm.add(comp)
                    }
                    opStack.add(component)
                }
                "(" -> {
                    opStack.add(component)
                }
                ")" -> {
                    while (opStack.isNotEmpty() && opStack.last() != "(") {
                        val opTop = opStack.removeAt(opStack.size - 1)
                        val comp = Component().apply {
                            type = Component.Type.OPERATOR
                            op = opTop
                        }
                        postfixForm.add(comp)
                    }
                    if (opStack.isNotEmpty() && opStack.last() == "(") {
                        opStack.removeAt(opStack.size - 1)
                    }
                }
                "sin", "cos", "tan" -> {
                    opStack.add(component)
                }
                else -> {
                    val comp = Component().apply {
                        type = Component.Type.NUMBER
                        num = component.toDouble()
                    }
                    postfixForm.add(comp)
                }
            }
        }

        while (opStack.isNotEmpty()) {
            val opTop = opStack.removeAt(opStack.size - 1)
            val comp = Component().apply {
                type = Component.Type.OPERATOR
                op = opTop
            }
            postfixForm.add(comp)
        }

        return postfixForm
    }

    private fun calculatePostfixForm(postfixForm: MutableList<Component>) : Double {
        val numStack = mutableListOf<Double>()
        for (component in postfixForm) {
            if (component.type == Component.Type.NUMBER) {
                component.num?.let { numStack.add(it) }
            }
            else if (component.type == Component.Type.OPERATOR) {
                when (component.op) {
                    "+" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        val num2 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        numStack.add(num1 + num2)
                    }
                    "-" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        var num2 = 0.0
                        if (numStack.isNotEmpty()) {
                            num2 = numStack.last()
                            numStack.removeAt(numStack.size - 1)
                        }
                        numStack.add(num2 - num1)
                    }
                    "x" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        val num2 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        numStack.add(num1 * num2)
                    }
                    "/" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        var num2 = 1.0
                        if (numStack.isNotEmpty()) {
                            num2 = numStack.last()
                            numStack.removeAt(numStack.size - 1)
                        }
                        numStack.add(num2 / num1)
                    }
                    "sin" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        numStack.add(sin(Math.toRadians(num1)))
                    }
                    "cos" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        numStack.add(cos(Math.toRadians(num1)))
                    }
                    "tan" -> {
                        val num1 = numStack.last()
                        numStack.removeAt(numStack.size - 1)
                        numStack.add(tan(Math.toRadians(num1)))
                    }
                }
            }
        }
        return numStack.last()
    }

    fun calculate(inputStack : MutableList<String>) : Double {
        if (inputStack.last() == "(") {
            return 0.0
        }
        if (inputStack[0] == "x" || inputStack[0] == "/") {
            inputStack.add(0, "1")
        }
        else if (inputStack[0] == "+" || inputStack[0] == "-") {
            inputStack.add(0, "0")
        }
        if (inputStack.last() == "x" || inputStack.last() == "/") {
            inputStack.add("1")
        }
        else if (inputStack.last() == "+" || inputStack.last() == "-") {
            inputStack.add("0")
        }

        var index = 0
        var count = 0
        while (index < inputStack.size) {
            if (inputStack[index] == "-" && inputStack[index + 1] == "-") {
                inputStack.removeAt(index)
                count++
                index--
            }
            else {
                if (count != 0) {
                    if (count % 2 != 0) {
                        inputStack[index] = "+"
                    }
                    count = 0
                }
            }
            index++
        }

        index = 0
        while (index < inputStack.size) {
            if (inputStack[index] == "+" && inputStack[index + 1] == "+") {
                inputStack.removeAt(index)
                index--
            }
            index++
        }

        for (i in 0 until inputStack.size - 1) {
            if (i + 1 < inputStack.size - 1 && inputStack[i] in "0".."9" && inputStack[i+1] == "(") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] == ")" && inputStack[i+1] in "0".."9") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] in "0".."9" && inputStack[i+1] == "sin") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] in "0".."9" && inputStack[i+1] == "cos") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] in "0".."9" && inputStack[i+1] == "tan") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] == ")" && inputStack[i+1] == "(") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] == ")" && inputStack[i+1] in "0".."9") {
                inputStack.add(i + 1, "x")
            }
            else if (inputStack[i] in "0".."9" && inputStack[i+1] == "(") {
                inputStack.add(i + 1, "x")
            }
            else if ((inputStack[i] == "x" || inputStack[i] == "/") && (inputStack[i + 1] == "+" || inputStack[i + 1] == "-")) {
                if (i + 2 < inputStack.size && (inputStack[i + 2] != ")"  || inputStack[i + 2] != "+" || inputStack[i + 2] != "-" || inputStack[i + 2] != "x" || inputStack[i + 2] != "/")) {
                    if (inputStack[i + 1] == "+") {
                        inputStack[i + 1] = "1"
                        inputStack.add(i + 2, "x")
                    }
                    else if (inputStack[i + 1] == "-") {
                        inputStack[i + 1] = "-1"
                        inputStack.add(i + 2, "x")
                    }
                }
                else return 0.0
            }
            else if ((inputStack[i] == "+" || inputStack[i] == "-") && (inputStack[i + 1] == "x" || inputStack[i + 1] == "/")) {
                return 0.0
            }
        }

        val postfixForm = this.postfixTransform(inputStack)
        return this.calculatePostfixForm(postfixForm)
    }
}

object CalculatorHandler {
    private var expression: String = ""
    private var ans: String = ""
    private val calculatorStack = CalculatorStack()

    fun getExpression(): String {
        return expression
    }

    private fun expressionTransform(exp : String) : MutableList<String> {
        var tempNum = ""
        val expList = mutableListOf<String>()
        var i = 0
        while (i < exp.length) {
            if (exp[i] in '0'..'9' || exp[i] == '.') {
                tempNum += exp[i]
            }
            else {
                if (tempNum.isNotEmpty()) {
                    expList.add(tempNum)
                    tempNum = ""
                }
                if (exp[i] == 's' && i + 2 < exp.length && exp.substring(i, i + 3) == "sin") {
                    expList.add("sin")
                    i += 2
                }
                else if (exp[i] == 'c' && i + 2 < exp.length && exp.substring(i, i + 3) == "cos") {
                    expList.add("cos")
                    i += 2
                }
                else if (exp[i] == 't' && i + 2 < exp.length && exp.substring(i, i + 3) == "tan") {
                    expList.add("tan")
                    i += 2
                }
                else {
                    expList.add(exp[i].toString())
                }
            }
            i++
        }
        if (tempNum.isNotEmpty()) {
            expList.add(tempNum)
        }
        return expList
    }

    fun onButtonPressed(button: String) {
        when (button) {
            "CLR" -> {
                expression = ""
            }
            "DEL" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.substring(0, expression.length - 1)
                }
            }
            "sin", "cos", "tan" -> {
                expression += "$button("
            }
            "ANS" -> {
                expression += ans
            }
            "x" -> {
                if (expression != "" && expression.last() == 'x') {
                    return
                }
                expression += button
            }
            "/" -> {
                if (expression != "" && expression.last() == '/') {
                    return
                }
                expression += button
            }
            "=" -> {
                if (expression == "") return
                val expList = expressionTransform(expression)
                val result = calculatorStack.calculate(expList)
                expression = result.toString()
                if (expression.substring(expression.length - 2) == ".0") {
                    expression = expression.substring(0, expression.length - 2)
                }
                if (expression.length > 10) {
                    expression = expression.substring(0, 10)
                }
                ans = expression
            }
            else -> {
                expression += button
            }
        }
    }
}
