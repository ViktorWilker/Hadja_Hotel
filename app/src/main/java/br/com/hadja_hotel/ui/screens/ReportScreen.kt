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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)
    val report = remember(
        viewModel.reservations,
        viewModel.guests,
        viewModel.events
    ) { viewModel.generateReport() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatórios Operacionais") },
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

            Spacer(modifier = Modifier.height(4.dp))

            Text("Operacional", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                item {
                    MetricCard(
                        label = "Reservas confirmadas",
                        value = "${report.totalReservations}",
                        accentColor = hadjaGreen
                    )
                }
                item {
                    MetricCard(
                        label = "Taxa de ocupação",
                        value = Formatter.percentage(report.occupancyRate),
                        accentColor = hadjaGreen
                    )
                }
                item {
                    MetricCard(
                        label = "Hóspedes cadastrados",
                        value = "${report.totalGuests}",
                        accentColor = hadjaGreen
                    )
                }
                item {
                    MetricCard(
                        label = "Eventos realizados",
                        value = "${report.totalEvents}",
                        accentColor = hadjaGreen
                    )
                }
            }

            Text("Receita", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RevenueRow(
                        label = "Hospedagem",
                        value = Formatter.currency(report.hospitalityRevenue),
                        valueColor = Color(0xFF2C2C2A)
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    RevenueRow(
                        label = "Eventos",
                        value = Formatter.currency(report.eventsRevenue),
                        valueColor = Color(0xFF2C2C2A)
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    RevenueRow(
                        label = "Total geral",
                        value = Formatter.currency(report.grandTotal),
                        valueColor = hadjaGreen,
                        isBold = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = accentColor
            )
        }
    }
}

@Composable
fun RevenueRow(
    label: String,
    value: String,
    valueColor: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = if (isBold) Color(0xFF2C2C2A) else Color.Gray,
            fontWeight = if (isBold) FontWeight.Medium else FontWeight.Normal
        )
        Text(
            value,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Medium else FontWeight.Normal,
            color = valueColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportScreenPreview() {
        ReportScreen(
            viewModel = HotelViewModel(),
            onBack = {}
        )
}