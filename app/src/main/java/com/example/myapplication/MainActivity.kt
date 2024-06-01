package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

import com.example.myapplication.CalculatorHandler
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 64.dp),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Calculator()
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun Calculator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 1.dp)
    ) {
        CalculatorScreen()
        CalculatorKeyboard()
    }
}

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, end = 16.dp, bottom = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        LaunchedEffect(expression) {
            while (true) {
                // Wait for changes to expression
                delay(50) // Adjust the delay as needed
                // Trigger recomposition
                expression = CalculatorHandler.getExpression() // This line may look redundant, but it triggers recomposition
                if (expression.length > 10) {
                    expression = expression.substring(expression.length - 10, expression.length)
                }
            }
        }
        Text(
            text = expression,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}


@Composable
fun CalculatorKeyboard() {
    val buttons = arrayOf(
        arrayOf("(", ")", "DEL", "CLR"),
        arrayOf("+", "-", "x", "/"),
        arrayOf("7", "8", "9", "sin"),
        arrayOf("4", "5", "6", "cos"),
        arrayOf("1", "2", "3", "tan"),
        arrayOf("0", ".", "ANS", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in buttons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (button in row) {
                    CalculatorButton(text = button, onClick = {CalculatorHandler.onButtonPressed(button)}, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF224CD6))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(Color(0xFF224CD6)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text, color = Color.White, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyApplicationTheme {
        Calculator()
    }
}