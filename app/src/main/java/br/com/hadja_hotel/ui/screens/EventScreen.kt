package br.com.hadja_hotel.ui.screens

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.camera.core.Preview
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.model.BuffetResult
import br.com.hadja_hotel.model.Event
import br.com.hadja_hotel.service.EventService
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    viewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)
    val hadjaRed = Color(0xFFE24B4A)

    var currentStep by remember { mutableStateOf(0) }
    val steps = listOf("Auditório", "Agenda", "Resumo")

    var guestsInput by remember { mutableStateOf("") }
    var auditoriumResult by remember { mutableStateOf<Pair<String, Int>?>(null) }

    var company by remember { mutableStateOf("") }
    var weekday by remember { mutableStateOf("") }
    var startHour by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    var waiters by remember { mutableStateOf(0) }
    var waiterCost by remember { mutableStateOf(0.0) }
    var buffetResult by remember { mutableStateOf<BuffetResult?>(null) }

    var errorMessage by remember { mutableStateOf("") }

    val weekdays = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    val weekdayLabels = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eventos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
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
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = guestsInput,
                            onValueChange = {
                                guestsInput = it
                                auditoriumResult = null
                            },
                            label = { Text("Número de convidados") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )

                        auditoriumResult?.let { (name, extraChairs) ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = hadjaGreen.copy(alpha = 0.08f)
                                ),
                                border = BorderStroke(0.5.dp, hadjaGreen.copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        "Auditório selecionado",
                                        fontSize = 11.sp,
                                        color = hadjaGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (extraChairs > 0) {
                                        Text(
                                            "$extraChairs cadeiras extras necessárias",
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = hadjaRed, fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = {
                            val guests = guestsInput.toIntOrNull()
                            when {
                                guests == null -> errorMessage = "Informe o número de convidados."
                                guests < 0 || guests > 350 -> errorMessage = "Número de convidados inválido (0-350)."
                                else -> {
                                    errorMessage = ""
                                    auditoriumResult = EventService.selectAuditorium(guests)
                                    currentStep = 1
                                }
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

                1 -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = company,
                            onValueChange = { company = it },
                            label = { Text("Nome da empresa") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )

                        Text("Dia da semana", fontSize = 13.sp, color = Color.Gray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            weekdays.forEachIndexed { index, day ->
                                FilterChip(
                                    selected = weekday == day,
                                    onClick = { weekday = day },
                                    label = {
                                        Text(weekdayLabels[index], fontSize = 11.sp)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = hadjaGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = startHour,
                            onValueChange = { startHour = it },
                            label = { Text("Hora de início (7-23)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )

                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Duração (1-12 horas)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = hadjaGreen,
                                focusedLabelColor = hadjaGreen,
                                cursorColor = hadjaGreen
                            )
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = hadjaRed, fontSize = 13.sp)
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
                                val start = startHour.toIntOrNull()
                                val dur = duration.toIntOrNull()
                                val guests = guestsInput.toIntOrNull() ?: 0
                                when {
                                    company.isBlank() -> errorMessage = "Informe o nome da empresa."
                                    weekday.isBlank() -> errorMessage = "Selecione o dia da semana."
                                    start == null -> errorMessage = "Informe a hora de início."
                                    dur == null || dur !in 1..12 -> errorMessage = "Duração deve ser entre 1 e 12 horas."
                                    !EventService.isAvailable(weekday, start, start + dur) -> errorMessage = "Auditório indisponível nesse horário."
                                    else -> {
                                        errorMessage = ""
                                        waiters = EventService.calculateWaiters(guests, dur)
                                        waiterCost = EventService.calculateWaitersCost(waiters, dur)
                                        buffetResult = EventService.calculateBuffet(guests)
                                        currentStep = 2
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                        ) { Text("Próximo") }
                    }
                }

                2 -> {
                    val guests = guestsInput.toIntOrNull() ?: 0
                    val dur = duration.toIntOrNull() ?: 0
                    val start = startHour.toIntOrNull() ?: 0
                    val buffet = buffetResult
                    val totalCost = waiterCost + (buffet?.totalCost ?: 0.0)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Evento", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        listOf(
                            "Empresa" to company,
                            "Auditório" to (auditoriumResult?.first ?: ""),
                            "Convidados" to "$guests pessoas",
                            "Dia" to weekday.replaceFirstChar { it.uppercase() },
                            "Horário" to "${Formatter.hour(start)} às ${Formatter.hour(start + dur)}"
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

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Garçons", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        listOf(
                            "Quantidade" to "$waiters garçons",
                            "Custo" to Formatter.currency(waiterCost)
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

                        Spacer(modifier = Modifier.height(4.dp))

                        buffet?.let {
                            Text("Buffet", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            listOf(
                                "Café" to "${it.coffeeLiters}L",
                                "Água" to "${it.waterLiters}L",
                                "Salgados" to "${it.snacksCount} unidades",
                                "Custo buffet" to Formatter.currency(it.totalCost)
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
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total do evento", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text(
                                Formatter.currency(totalCost),
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
                                val event = Event(
                                    company = company,
                                    auditorium = auditoriumResult?.first ?: "",
                                    weekday = weekday,
                                    startHour = startHour.toIntOrNull() ?: 0,
                                    duration = dur,
                                    guests = guests,
                                    waiters = waiters,
                                    waiterCost = waiterCost,
                                    buffetCost = buffetResult?.totalCost ?: 0.0,
                                    totalCost = totalCost
                                )
                                viewModel.confirmEvent(event)
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
