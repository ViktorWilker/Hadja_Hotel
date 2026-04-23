package br.com.hadja_hotel.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import br.com.hadja_hotel.viewmodel.HotelViewModel

@Composable
fun LoginScreen(
    viewModel: HotelViewModel,
    onLoginSuccess: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)
    val hadjaRed = Color(0xFFE24B4A)

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var attempts by remember { mutableStateOf(0) }
    var isBlocked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAF9))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(hadjaGreen, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("H", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Medium)
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(hadjaGold, CircleShape)
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Hadja ", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Text("Hotel", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = hadjaGold)
        }

        Text(
            "Sistema interno de gestão",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp)
        )

        Box(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .width(32.dp)
                .height(2.dp)
                .background(hadjaGold.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isBlocked,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = hadjaGreen,
                focusedLabelColor = hadjaGreen,
                cursorColor = hadjaGreen
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isBlocked,
            isError = errorMessage.isNotEmpty(),
            supportingText = {
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = hadjaGreen,
                focusedLabelColor = hadjaGreen,
                cursorColor = hadjaGreen
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isBlocked) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = hadjaRed.copy(alpha = 0.08f)),
                border = BorderStroke(0.5.dp, hadjaRed.copy(alpha = 0.3f))
            ) {
                Text(
                    "Sistema bloqueado após 3 tentativas.\nContate o administrador.",
                    color = hadjaRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        Button(
            onClick = {
                when {
                    name.isBlank() -> errorMessage = "Informe seu nome."
                    viewModel.validatePassword(password) -> {
                        viewModel.startSession(name)
                        onLoginSuccess()
                    }
                    else -> {
                        attempts = viewModel.recordAttempt()
                        password = ""
                        if (viewModel.isBlocked()) {
                            isBlocked = true
                            errorMessage = ""
                        } else {
                            val remaining = 3 - attempts
                            errorMessage = "Senha incorreta. $remaining tentativa(s) restante(s)."
                        }
                    }
                }
            },
            enabled = !isBlocked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
        ) {
            Text("Entrar", fontWeight = FontWeight.Medium)
        }

        if (attempts > 0 && !isBlocked) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                buildAnnotatedString {
                    append("Tentativa ")
                    withStyle(SpanStyle(color = hadjaGold, fontWeight = FontWeight.Medium)) {
                        append("$attempts")
                    }
                    append(" de 3")
                },
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
