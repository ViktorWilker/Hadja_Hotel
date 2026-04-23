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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.model.RoomType
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestDetailScreen(
    viewModel: HotelViewModel,
    guestName: String,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)

    val guest = remember(guestName, viewModel.guests) {
        viewModel.guests.find { it.name == guestName }
    }

    val guestReservations = remember(guestName, viewModel.reservations) {
        viewModel.reservations.filter { it.guestName == guestName }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do hóspede") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFAF9))
            )
        },
        containerColor = Color(0xFFFAFAF9)
    ) { padding ->
        if (guest == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Hóspede não encontrado.", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(2.dp)
                        .background(hadjaGold.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(hadjaGreen.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                guest.name.first().uppercase(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = hadjaGreen
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                guest.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Cadastrado em ${guest.registeredAt.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                                )}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Text(
                    "Reservas",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                if (guestReservations.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Nenhuma reserva encontrada.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    guestReservations.forEach { reservation ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Quarto ${reservation.roomNumber}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                hadjaGreen.copy(alpha = 0.1f),
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            when (reservation.roomType) {
                                                RoomType.STANDARD -> "Standard"
                                                RoomType.EXECUTIVE -> "Executivo"
                                                RoomType.LUXURY -> "Luxo"
                                            },
                                            fontSize = 11.sp,
                                            color = hadjaGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.4f))
                                listOf(
                                    "Diárias" to "${reservation.nights} noite(s)",
                                    "Subtotal" to Formatter.currency(reservation.subtotal),
                                    "Taxa de serviço" to Formatter.currency(reservation.serviceFee),
                                    "Total" to Formatter.currency(reservation.total)
                                ).forEach { (label, value) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(label, fontSize = 13.sp, color = Color.Gray)
                                        Text(
                                            value,
                                            fontSize = 13.sp,
                                            fontWeight = if (label == "Total") FontWeight.Medium else FontWeight.Normal,
                                            color = if (label == "Total") hadjaGreen else Color(0xFF2C2C2A)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

