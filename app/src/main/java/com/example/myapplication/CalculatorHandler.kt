package com.example.myapplication

class CalculatorStack {
    private val stack = mutableListOf<String>()

    fun push(value: String) {
        stack.add(value)
    }

    fun pop(): String {
        return stack.removeAt(stack.size - 1)
    }

    fun peek(): String {
        return stack[stack.size - 1]
    }

    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }

    fun clear() {
        stack.clear()
    }

    fun size(): Int {
        return stack.size
    }
}

object CalculatorHandler {
    private var expression: String = ""
    private val calculatorStack = CalculatorStack()
    private var tempComponent: String = ""

    fun getExpression(): String {
        return expression
    }

    private fun addDigit(digit: String) {
        expression += digit
    }

    private fun removeLastDigit() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1)
            if (tempComponent.isNotEmpty()) {
                tempComponent = tempComponent.dropLast(1)
            } else {
                tempComponent = calculatorStack.pop()
            }
        }


    }

    fun onButtonPressed(button: String) {
        if (button == "CLR") {
            expression = ""
        } else if (button == "DEL") {
            removeLastDigit()
        } else if (button == "=") {
            calculate()
        } else {
            if (
                button == "0" ||
                button == "1" ||
                button == "2" ||
                button == "3" ||
                button == "4" ||
                button == "5" ||
                button == "6" ||
                button == "7" ||
                button == "8" ||
                button == "9" ||
                button == "."
            ) {
                tempComponent += button
            }
            else if (
                button == "+" ||
                button == "-" ||
                button == "x" ||
                button == "/" ||
                button == "(" ||
                button == ")" ||
                button == "sin" ||
                button == "cos" ||
                button == "tan"
            ) {
                this.calculatorStack.push(tempComponent)
                this.calculatorStack.push(button)
                tempComponent = ""
            }
            if (button == "sin" || button == "cos" || button == "tan") {
                addDigit(button + "(")
            }
            else addDigit(button)
        }
        print(expression)
    }

    private fun calculate() {}
}
