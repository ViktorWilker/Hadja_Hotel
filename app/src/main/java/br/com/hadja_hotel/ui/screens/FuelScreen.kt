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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import br.com.hadja_hotel.model.FuelResult
import br.com.hadja_hotel.service.FuelService
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelScreen(
    viewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)
    val hadjaRed = Color(0xFFE24B4A)

    var wayneEthanol by remember { mutableStateOf("") }
    var wayneGasoline by remember { mutableStateOf("") }
    var starkEthanol by remember { mutableStateOf("") }
    var starkGasoline by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<FuelResult>?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Abastecimento") },
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FuelStationCard(
                    modifier = Modifier.weight(1f),
                    stationName = "Wayne Oil",
                    ethanol = wayneEthanol,
                    gasoline = wayneGasoline,
                    accentColor = hadjaGreen,
                    onEthanolChange = { wayneEthanol = it; results = null },
                    onGasolineChange = { wayneGasoline = it; results = null }
                )
                FuelStationCard(
                    modifier = Modifier.weight(1f),
                    stationName = "Stark Petrol",
                    ethanol = starkEthanol,
                    gasoline = starkGasoline,
                    accentColor = hadjaGreen,
                    onEthanolChange = { starkEthanol = it; results = null },
                    onGasolineChange = { starkGasoline = it; results = null }
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = hadjaRed, fontSize = 13.sp)
            }

            Button(
                onClick = {
                    val wEthanol = wayneEthanol.toDoubleOrNull()
                    val wGasoline = wayneGasoline.toDoubleOrNull()
                    val sEthanol = starkEthanol.toDoubleOrNull()
                    val sGasoline = starkGasoline.toDoubleOrNull()

                    when {
                        wEthanol == null || wGasoline == null -> errorMessage = "Preencha os preços do Wayne Oil."
                        sEthanol == null || sGasoline == null -> errorMessage = "Preencha os preços do Stark Petrol."
                        wEthanol <= 0 || wGasoline <= 0 || sEthanol <= 0 || sGasoline <= 0 -> errorMessage = "Todos os preços devem ser maiores que zero."
                        else -> {
                            errorMessage = ""
                            val wayneResult = FuelService.getBestOption(
                                FuelResult("Wayne Oil", wEthanol, wGasoline)
                            )
                            val starkResult = FuelService.getBestOption(
                                FuelResult("Stark Petrol", sEthanol, sGasoline)
                            )
                            results = listOf(wayneResult, starkResult)
                                .sortedBy { it.totalCost }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
            ) {
                Text("Calcular")
            }

            results?.let { resultList ->
                val cheapest = FuelService.getCheapest(resultList)

                Text("Resultado", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)

                resultList.forEachIndexed { index, result ->
                    val isCheapest = result.stationName == cheapest.stationName
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            width = if (isCheapest) 2.dp else 0.5.dp,
                            color = if (isCheapest) hadjaGreen else Color.LightGray.copy(alpha = 0.5f)
                        )
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
                                Text(result.stationName, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                if (isCheapest) {
                                    Box(
                                        modifier = Modifier
                                            .background(hadjaGreen.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Mais barato", fontSize = 11.sp, color = hadjaGreen, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                            Divider(color = Color.LightGray.copy(alpha = 0.4f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Melhor combustível", fontSize = 13.sp, color = Color.Gray)
                                Text(result.bestFuel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total (42L)", fontSize = 13.sp, color = Color.Gray)
                                Text(
                                    Formatter.currency(result.totalCost),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isCheapest) hadjaGreen else Color(0xFF2C2C2A)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun FuelStationCard(
    modifier: Modifier = Modifier,
    stationName: String,
    ethanol: String,
    gasoline: String,
    accentColor: Color,
    onEthanolChange: (String) -> Unit,
    onGasolineChange: (String) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stationName, fontSize = 13.sp, fontWeight = FontWeight.Medium)

            OutlinedTextField(
                value = ethanol,
                onValueChange = onEthanolChange,
                label = { Text("Etanol", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$", fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    focusedLabelColor = accentColor,
                    cursorColor = accentColor
                )
            )

            OutlinedTextField(
                value = gasoline,
                onValueChange = onGasolineChange,
                label = { Text("Gasolina", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$", fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    focusedLabelColor = accentColor,
                    cursorColor = accentColor
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FuelScreenPreview() {
        FuelScreen(
            viewModel = HotelViewModel(),
            onBack = {}
        )
}