package br.com.hadja_hotel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.model.Reservation
import br.com.hadja_hotel.model.RoomType
import br.com.hadja_hotel.service.ReservationService
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    viewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)

    var currentStep by remember { mutableStateOf(0) }
    val steps = listOf("Dados", "Quarto", "Confirmar")

    var dailyRate by remember { mutableStateOf("") }
    var nights by remember { mutableStateOf("") }
    var guestName by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf(RoomType.STANDARD) }
    var selectedRoom by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val roomGrid = remember(viewModel.reservations) { viewModel.getRoomGrid() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFAF9))
            )
        },
        containerColor = Color(0xFFFAFAF9)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, label ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (index <= currentStep) hadjaGreen else Color.LightGray,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (index < currentStep) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    "${index + 1}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            label,
                            fontSize = 12.sp,
                            color = if (index <= currentStep) hadjaGreen else Color.Gray,
                            fontWeight = if (index == currentStep) FontWeight.Medium else FontWeight.Normal
                        )
                        if (index < steps.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .width(24.dp)
                                    .height(1.dp)
                                    .background(if (index < currentStep) hadjaGreen else Color.LightGray)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(hadjaGold.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (currentStep) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = guestName,
                            onValueChange = { guestName = it },
                            label = { Text("Nome do hóspede") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )
                        OutlinedTextField(
                            value = dailyRate,
                            onValueChange = { dailyRate = it },
                            label = { Text("Valor da diária (R$)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )
                        OutlinedTextField(
                            value = nights,
                            onValueChange = { nights = it },
                            label = { Text("Quantidade de diárias (1-30)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )

                        // Tipo de quarto
                        Text("Tipo de quarto", fontSize = 13.sp, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(
                                RoomType.STANDARD to "Standard",
                                RoomType.EXECUTIVE to "Executivo",
                                RoomType.LUXURY to "Luxo"
                            ).forEach { (type, label) ->
                                FilterChip(
                                    selected = roomType == type,
                                    onClick = { roomType = type },
                                    label = { Text(label, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = hadjaGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = {
                            val rate = dailyRate.toDoubleOrNull()
                            val n = nights.toIntOrNull()
                            when {
                                guestName.isBlank() -> errorMessage = "Informe o nome do hóspede."
                                rate == null || !ReservationService.validateDailyRate(rate) -> errorMessage = "Valor da diária inválido."
                                n == null || !ReservationService.validateNigths(n) -> errorMessage = "Quantidade de diárias deve ser entre 1 e 30."
                                else -> { errorMessage = ""; currentStep = 1 }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                    ) {
                        Text("Próximo")
                    }
                }

                // Etapa 2 — Quarto
                1 -> {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Selecione um quarto",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Legenda
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).background(hadjaGreen, RoundedCornerShape(3.dp)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Livre", fontSize = 11.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).background(Color(0xFFE24B4A), RoundedCornerShape(3.dp)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ocupado", fontSize = 11.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).background(hadjaGold, RoundedCornerShape(3.dp)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Selecionado", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        // Grid 4x5
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            roomGrid.forEachIndexed { rowIndex, row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEachIndexed { colIndex, status ->
                                        val roomNumber = rowIndex * 5 + colIndex + 1
                                        val isOccupied = status == "O"
                                        val isSelected = selectedRoom == roomNumber
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .background(
                                                    when {
                                                        isSelected -> hadjaGold
                                                        isOccupied -> Color(0xFFE24B4A)
                                                        else -> hadjaGreen
                                                    },
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable(enabled = !isOccupied) {
                                                    selectedRoom = roomNumber
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                "$roomNumber",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { currentStep = 0 },
                            modifier = Modifier.weight(1f)
                        ) { Text("Voltar") }

                        Button(
                            onClick = {
                                if (selectedRoom == null) {
                                    errorMessage = "Selecione um quarto."
                                } else {
                                    errorMessage = ""
                                    currentStep = 2
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                        ) { Text("Próximo") }
                    }
                }

                // Etapa 3 — Confirmação
                2 -> {
                    val rate = dailyRate.toDoubleOrNull() ?: 0.0
                    val n = nights.toIntOrNull() ?: 0
                    val (subtotal, fee, total) = ReservationService.calculateTotals(rate, n, roomType)

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            "Hóspede" to guestName,
                            "Quarto" to "Quarto $selectedRoom",
                            "Tipo" to when (roomType) {
                                RoomType.STANDARD -> "Standard"
                                RoomType.EXECUTIVE -> "Executivo"
                                RoomType.LUXURY -> "Luxo"
                            },
                            "Diárias" to "$n noite(s)",
                            "Subtotal" to Formatter.currency(subtotal),
                            "Taxa de serviço" to Formatter.currency(fee)
                        ).forEach { (label, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(label, fontSize = 14.sp, color = Color.Gray)
                                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text(
                                Formatter.currency(total),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = hadjaGreen
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { currentStep = 1 },
                            modifier = Modifier.weight(1f)
                        ) { Text("Voltar") }

                        Button(
                            onClick = {
                                val reservation = Reservation(
                                    guestName = guestName,
                                    roomNumber = selectedRoom!!,
                                    roomType = roomType,
                                    dailyRate = rate,
                                    nights = n,
                                    subtotal = subtotal,
                                    serviceFee = fee,
                                    total = total
                                )
                                viewModel.confirmReservation(reservation)
                                onBack()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                        ) { Text("Confirmar") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReservationScreenPreview() {
        ReservationScreen(
            viewModel = HotelViewModel(),
            onBack = {}
        )
}