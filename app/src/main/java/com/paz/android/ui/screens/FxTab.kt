package com.paz.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paz.android.data.BankComparisonItem
import com.paz.android.data.PazUiState
import com.paz.android.data.RemittanceChannelItem
import com.paz.android.ui.components.PazBadge
import com.paz.android.ui.components.PazSectionHeader
import com.paz.android.ui.components.PazSegmentedToggle
import com.paz.android.ui.theme.*

@Composable
fun FxTab(
    state: PazUiState,
    modifier: Modifier = Modifier
) {
    var selectedSubMode by remember { mutableIntStateOf(0) } // 0 = Banks, 1 = Remittance
    var selectedCurrency by remember { mutableStateOf("USD") }
    var remittanceAmount by remember { mutableStateOf("1000") }

    val currencies = listOf("USD", "EUR", "GBP", "AED")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sub-mode Segmented Toggle
        item {
            PazSegmentedToggle(
                options = listOf("Commercial Bank Rates", "Diaspora Remittance"),
                selectedIndex = selectedSubMode,
                onSelect = { selectedSubMode = it }
            )
        }

        if (selectedSubMode == 0) {
            // Currency Selector Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currencies.forEach { curr ->
                        val isSelected = curr == selectedCurrency
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyanAccent.copy(alpha = 0.15f) else CardSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) CyanAccent else BorderStroke,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCurrency = curr }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = curr,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) CyanAccent else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // Best Rate Indicators
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val bestSell = state.bankData.best_to_sell_fx?.get(selectedCurrency)
                    val bestBuy = state.bankData.best_to_buy_fx?.get(selectedCurrency)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardSurface)
                            .border(1.dp, BorderStroke, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "BEST TO SELL FX",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GreenBullish,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = bestSell?.bank ?: "National Bank (NBE)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = String.format("%.2f ETB", bestSell?.rate ?: 162.95),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = GreenBullish,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardSurface)
                            .border(1.dp, BorderStroke, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "BEST TO BUY FX",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyanAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = bestBuy?.bank ?: "Commercial Bank (CBE)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = String.format("%.2f ETB", bestBuy?.rate ?: 164.80),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
            }

            // Commercial Bank Rates Table Header
            item {
                PazSectionHeader(
                    title = "Commercial Bank Rates ($selectedCurrency)",
                    subtitle = "Official foreign exchange board rates updated every morning"
                )
            }

            // Bank Rows
            items(state.bankData.banks) { bank: BankComparisonItem ->
                val rate = bank.currencies[selectedCurrency]
                val buying = rate?.buying ?: 162.50
                val selling = rate?.selling ?: 165.00
                val spread = rate?.spread ?: (selling - buying)
                val spreadPct = rate?.spread_pct ?: (spread / buying * 100)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderStroke, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldAccent.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = bank.bank_code,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GoldAccent,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                                Column {
                                    Text(
                                        text = bank.bank_name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Text(
                                        text = bank.bank_type,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            PazBadge(
                                text = String.format("Spread %.1f%%", spreadPct),
                                color = TextSecondary
                            )
                        }

                        HorizontalDivider(color = BorderStroke, thickness = 0.6.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "BUYING (WE BUY)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = String.format("%.2f ETB", buying),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = GreenBullish,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "SPREAD",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = String.format("+%.2f", spread),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 13.sp
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "SELLING (WE SELL)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = String.format("%.2f ETB", selling),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = CyanAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Remittance Optimizer Mode
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderStroke, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "DIASPORA REMITTANCE SIMULATOR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = remittanceAmount,
                                onValueChange = { remittanceAmount = it },
                                label = { Text("Sending Amount") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldAccent,
                                    unfocusedBorderColor = BorderStroke,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedLabelColor = GoldAccent,
                                    unfocusedLabelColor = TextSecondary
                                ),
                                modifier = Modifier.weight(1.5f)
                            )

                            // Currency Chips
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("USD", "EUR", "GBP").forEach { curr ->
                                    val isSelected = curr == selectedCurrency
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSelected) GoldAccent else CardSurfaceElevated)
                                            .clickable { selectedCurrency = curr }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = curr,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) ObsidianBg else TextSecondary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                PazSectionHeader(
                    title = "Remittance Channels Comparison",
                    subtitle = "Comparing net ETB payout after all fees and FX spreads"
                )
            }

            val amountNum = remittanceAmount.toDoubleOrNull() ?: 1000.0
            val baseRate = when (selectedCurrency) {
                "EUR" -> 176.45
                "GBP" -> 212.10
                "AED" -> 44.46
                else -> 163.29
            }

            items(state.remittanceData.channels) { ch: RemittanceChannelItem ->
                val netEtb = (amountNum - ch.transfer_fee_source) * (baseRate * 0.993)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (ch.is_best) CardSurfaceElevated else CardSurface)
                        .border(
                            1.dp,
                            if (ch.is_best) GoldAccent else BorderStroke,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = ch.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = ch.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            if (ch.badge.isNotEmpty()) {
                                PazBadge(
                                    text = ch.badge,
                                    color = if (ch.is_best) GoldAccent else CyanAccent
                                )
                            }
                        }

                        HorizontalDivider(color = BorderStroke, thickness = 0.6.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "NET ETB RECEIVED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = String.format("%,.0f ETB", netEtb),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        color = if (ch.is_best) GoldAccent else TextPrimary,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 18.sp
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Fee: $${String.format("%.2f", ch.transfer_fee_source)}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "Speed: ${ch.delivery_time}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = CyanAccent,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
