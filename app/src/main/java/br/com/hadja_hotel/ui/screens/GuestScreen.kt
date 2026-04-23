package br.com.hadja_hotel.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hadja_hotel.model.Guest
import br.com.hadja_hotel.model.OperationResult
import br.com.hadja_hotel.viewmodel.HotelViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestScreen(
    viewModel: HotelViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val hadjaGreen = Color(0xFF1D9E75)
    val hadjaGold = Color(0xFFBA7517)

    val guestList = viewModel.guests

    var searchQuery by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var editingGuest by remember { mutableStateOf<String?>(null) }
    var bottomSheetName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    val filteredList = remember(searchQuery, guestList) {
        if (searchQuery.isBlank()) guestList
        else guestList.filter { it.name.startsWith(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hóspedes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFAF9))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingGuest = null
                    bottomSheetName = ""
                    errorMessage = ""
                    showBottomSheet = true
                },
                containerColor = hadjaGreen
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Cadastrar", tint = Color.White)
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por nome ou prefixo...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Limpar", tint = Color.Gray)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = hadjaGreen,
                    cursorColor = hadjaGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(hadjaGold.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "${filteredList.size} hóspede(s)",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (searchQuery.isBlank()) "Nenhum hóspede cadastrado."
                        else "Nenhum resultado para \"$searchQuery\".",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(filteredList) { index, guest ->
                        GuestItem(
                            index = index + 1,
                            guest = guest,
                            accentColor = hadjaGreen,
                            onDetail = { onNavigate("guest_detail/${guest.name}") },
                            onEdit = {
                                editingGuest = guest.name
                                bottomSheetName = guest.name
                                errorMessage = ""
                                showBottomSheet = true
                            },
                            onRemove = { viewModel.removeGuest(guest.name) }
                        )
                    }
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
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (editingGuest == null) "Cadastrar hóspede" else "Editar hóspede",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = bottomSheetName,
                    onValueChange = { bottomSheetName = it; errorMessage = "" },
                    label = { Text("Nome completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
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

                Button(
                    onClick = {
                        if (bottomSheetName.isBlank()) {
                            errorMessage = "Informe o nome do hóspede."
                            return@Button
                        }
                        val result = if (editingGuest == null) {
                            viewModel.registerGuest(bottomSheetName)
                        } else {
                            viewModel.updateGuest(editingGuest!!, bottomSheetName)
                        }
                        when (result) {
                            OperationResult.SUCCESS -> showBottomSheet = false
                            OperationResult.DUPLICATE -> errorMessage = "Hóspede já cadastrado."
                            OperationResult.LIMIT_REACHED -> errorMessage = "Máximo de 15 hóspedes atingido."
                            else -> errorMessage = "Operação não realizada."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = hadjaGreen)
                ) {
                    Text(if (editingGuest == null) "Cadastrar" else "Salvar alterações")
                }
            }
        }
    }
}

@Composable
fun GuestItem(
    index: Int,
    guest: Guest,
    accentColor: Color,
    onDetail: () -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetail() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(accentColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$index",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentColor
                    )
                }
                Column {
                    Text(guest.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(
                        guest.registeredAt.format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        ),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.Gray)
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Remover",
                        tint = Color(0xFFE24B4A)
                    )
                }
            }
        }
    }
}