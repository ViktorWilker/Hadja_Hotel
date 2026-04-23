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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.model.AcQuote
import br.com.hadja_hotel.service.AirConditioningService
import br.com.hadja_hotel.until.Formatter
import br.com.hadja_hotel.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditioningScreen(
    viewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)
    val hadjaRed = Color(0xFFE24B4A)

    val quotes = viewModel.acQuotes

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var company by remember { mutableStateOf("") }
    var pricePerUnit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var discountPct by remember { mutableStateOf("") }
    var minForDiscount by remember { mutableStateOf("") }
    var travelFee by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun clearForm() {
        company = ""; pricePerUnit = ""; quantity = ""
        discountPct = ""; minForDiscount = ""; travelFee = ""; errorMessage = ""
    }

    val cheapest = remember(quotes) {
        if (quotes.size >= 2) AirConditioningService.findCheapest(quotes) else null
    }
    val mostExpensive = remember(quotes) {
        if (quotes.size >= 2) AirConditioningService.findMostExpensive(quotes) else null
    }
    val diffPct = remember(quotes) {
        if (cheapest != null && mostExpensive != null)
            AirConditioningService.priceDiffPercentage(cheapest.finalTotal, mostExpensive.finalTotal)
        else null
    }

    val comparativeCardColors = CardDefaults.cardColors(
        containerColor = hadjaGreen.copy(alpha = 0.06f)
    )
    val quoteCardColors = CardDefaults.cardColors(containerColor = Color.White)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ar-Condicionado") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (quotes.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearQuotes() }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Limpar", tint = hadjaRed)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFAF9))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { clearForm(); showBottomSheet = true },
                containerColor = hadjaGreen
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar", tint = Color.White)
            }
        },
        containerColor = Color(0xFFFAFAF9)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(hadjaGold.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (quotes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Nenhum orçamento adicionado.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Adicione ao menos 2 para comparar.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (quotes.size >= 2 && cheapest != null && mostExpensive != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = comparativeCardColors,
                                border = BorderStroke(0.5.dp, hadjaGreen.copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Comparativo",
                                        fontSize = 13.sp,
                                        color = hadjaGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Melhor orçamento", fontSize = 13.sp, color = Color.Gray)
                                        Text(
                                            "${cheapest.company} — ${Formatter.currency(cheapest.finalTotal)}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = hadjaGreen
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Pior orçamento", fontSize = 13.sp, color = Color.Gray)
                                        Text(
                                            "${mostExpensive.company} — ${Formatter.currency(mostExpensive.finalTotal)}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    diffPct?.let {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Diferença", fontSize = 13.sp, color = Color.Gray)
                                            Text(
                                                Formatter.percentage(it / 100),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = hadjaGold
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    itemsIndexed(quotes) { _, quote ->
                        val isCheapest = cheapest?.company == quote.company
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = quoteCardColors,
                            border = BorderStroke(
                                width = if (isCheapest && quotes.size >= 2) 2.dp else 0.5.dp,
                                color = if (isCheapest && quotes.size >= 2) hadjaGreen
                                else Color.LightGray.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(quote.company, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                    if (isCheapest && quotes.size >= 2) {
                                        Box(
                                            modifier = Modifier
                                                .background(hadjaGreen.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                "Mais barato",
                                                fontSize = 11.sp,
                                                color = hadjaGreen,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.4f))
                                listOf(
                                    "Qtd. aparelhos" to "${quote.quantity} un",
                                    "Valor por aparelho" to Formatter.currency(quote.pricePerUnit),
                                    "Desconto" to "${quote.discountPct}% (mín. ${quote.minForDiscount} aparelhos)",
                                    "Deslocamento" to Formatter.currency(quote.travelFee),
                                    "Total" to Formatter.currency(quote.finalTotal)
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

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Novo orçamento", fontSize = 18.sp, fontWeight = FontWeight.Medium)

                listOf(
                    Triple("Nome da empresa", company, { v: String -> company = v }),
                    Triple("Valor por aparelho (R$)", pricePerUnit, { v: String -> pricePerUnit = v }),
                    Triple("Quantidade de aparelhos", quantity, { v: String -> quantity = v }),
                    Triple("Desconto (%)", discountPct, { v: String -> discountPct = v }),
                    Triple("Mínimo para desconto (aparelhos)", minForDiscount, { v: String -> minForDiscount = v }),
                    Triple("Valor de deslocamento (R$)", travelFee, { v: String -> travelFee = v })
                ).forEach { (label, value, onChange) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { onChange(it); errorMessage = "" },
                        label = { Text(label) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (label == "Nome da empresa") KeyboardType.Text
                            else KeyboardType.Decimal
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = hadjaGreen,
                            focusedLabelColor = hadjaGreen,
                            cursorColor = hadjaGreen
                        )
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = hadjaRed, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        val price = pricePerUnit.toDoubleOrNull()
                        val qty = quantity.toIntOrNull()
                        val disc = discountPct.toDoubleOrNull()
                        val minDisc = minForDiscount.toIntOrNull()
                        val travel = travelFee.toDoubleOrNull()

                        when {
                            company.isBlank() -> errorMessage = "Informe o nome da empresa."
                            price == null || price <= 0 -> errorMessage = "Valor por aparelho inválido."
                            qty == null || qty <= 0 -> errorMessage = "Quantidade inválida."
                            disc == null || disc < 0 -> errorMessage = "Percentual de desconto inválido."
                            minDisc == null || minDisc < 0 -> errorMessage = "Mínimo para desconto inválido."
                            travel == null || travel < 0 -> errorMessage = "Valor de deslocamento inválido."
                            else -> {
                                val input = AcQuote(
                                    company = company,
                                    pricePerUnit = price,
                                    quantity = qty,
                                    discountPct = disc,
                                    minForDiscount = minDisc,
                                    travelFee = travel
                                )
                                val result = AirConditioningService.calculateQuote(input)
                                viewModel.addQuote(result)
                                showBottomSheet = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                ) {
                    Text("Adicionar orçamento")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AirConditioningScreenPreview() {
        AirConditioningScreen(
            viewModel = HotelViewModel(),
            onBack = {}
        )
}