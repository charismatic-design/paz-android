package com.paz.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
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
import com.paz.android.data.PazUiState
import com.paz.android.ui.components.HeroRateCard
import com.paz.android.ui.components.PazBadge
import com.paz.android.ui.components.PazSectionHeader
import com.paz.android.ui.components.PazSegmentedToggle
import com.paz.android.ui.theme.*
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun GoldTab(
    state: PazUiState,
    modifier: Modifier = Modifier
) {
    var selectedSubMode by remember { mutableIntStateOf(0) } // 0 = Vault & Karats, 1 = Simulator

    // Vault Calculator States
    var gramsInput by remember { mutableStateOf("25") }
    var selectedKarat by remember { mutableStateOf("24K") }

    // Simulator States
    var simPrincipalInput by remember { mutableStateOf("500000") }
    var simYears by remember { mutableFloatStateOf(3f) }

    val karatRates = mapOf(
        "24K" to (state.goldData.karats["24K"]?.per_gram_etb ?: 21315.0),
        "22K" to (state.goldData.karats["22K"]?.per_gram_etb ?: 19538.0),
        "21K" to (state.goldData.karats["21K"]?.per_gram_etb ?: 18650.0),
        "18K" to (state.goldData.karats["18K"]?.per_gram_etb ?: 15986.0)
    )

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
                options = listOf("Gold Vault & Karats", "Wealth Simulator"),
                selectedIndex = selectedSubMode,
                onSelect = { selectedSubMode = it }
            )
        }

        if (selectedSubMode == 0) {
            // Gold Spot Metrics Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeroRateCard(
                        label = "Spot Gold (London PM)",
                        tag = "24h ${state.goldData.change_24h ?: "+0.85%"}",
                        number = String.format("$%,.1f", state.goldData.spot_usd_per_oz),
                        currency = "USD/oz",
                        footnote = "Benchmark Troy Ounce",
                        accentColor = GoldAccent,
                        modifier = Modifier.weight(1f)
                    )

                    HeroRateCard(
                        label = "USD / ETB Conversion",
                        tag = "NBE Floating",
                        number = String.format("%.2f", state.goldData.usd_etb_rate),
                        currency = "ETB",
                        footnote = "Live Parity Anchor",
                        accentColor = CyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Live Karat Cards Section
            item {
                PazSectionHeader(
                    title = "Live Karat Purity Cards",
                    subtitle = "Calibrated against Addis Ababa Bole bullion & jewelry standards"
                )
            }

            // Karat 2x2 Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val karatsList = listOf("24K", "22K", "21K", "18K")
                    karatsList.chunked(2).forEach { rowKarats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowKarats.forEach { karatKey ->
                                val rateEtb = karatRates[karatKey] ?: 21315.0
                                val rateUsd = rateEtb / (state.goldData.usd_etb_rate)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CardSurface)
                                        .border(1.dp, BorderStroke, RoundedCornerShape(12.dp))
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = karatKey,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = GoldAccent,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                )
                                            )
                                            PazBadge(
                                                text = when (karatKey) {
                                                    "24K" -> "99.9%"
                                                    "22K" -> "91.6%"
                                                    "21K" -> "87.5%"
                                                    else -> "75.0%"
                                                },
                                                color = TextSecondary
                                            )
                                        }

                                        Text(
                                            text = String.format("%,.0f ETB/g", rateEtb),
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 15.sp
                                            )
                                        )

                                        Text(
                                            text = String.format("$%.2f USD/g", rateUsd),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextMuted,
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

            // My Gold Vault Stack Calculator
            item {
                PazSectionHeader(
                    title = "My Gold Vault Stack Calculator",
                    subtitle = "Track real-time portfolio net worth in Ethiopian Birr and USD"
                )
            }

            item {
                val grams = gramsInput.toDoubleOrNull() ?: 25.0
                val activeRate = karatRates[selectedKarat] ?: 21315.0
                val totalValueEtb = grams * activeRate
                val totalValueUsd = totalValueEtb / (state.goldData.usd_etb_rate)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = gramsInput,
                                onValueChange = { gramsInput = it },
                                label = { Text("Weight (Grams)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldAccent,
                                    unfocusedBorderColor = BorderStroke,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedLabelColor = GoldAccent,
                                    unfocusedLabelColor = TextSecondary
                                ),
                                modifier = Modifier.weight(1.2f)
                            )

                            // Karat Selector
                            Row(
                                modifier = Modifier.weight(1.5f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("24K", "22K", "21K", "18K").forEach { k ->
                                    val isSelected = k == selectedKarat
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSelected) GoldAccent else CardSurfaceElevated)
                                            .clickable { selectedKarat = k }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = k,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) ObsidianBg else TextSecondary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Calculated Valuation Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardSurfaceElevated)
                                .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "ESTIMATED STACK VALUATION",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GoldLight,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                    Text(
                                        text = String.format("%,.0f ETB", totalValueEtb),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            color = GoldAccent,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 22.sp
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "USD EQUIVALENT",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontSize = 9.sp
                                        )
                                    )
                                    Text(
                                        text = String.format("$%,.2f", totalValueUsd),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Wealth & Inflation Simulator
            item {
                val principal = simPrincipalInput.toDoubleOrNull() ?: 500000.0
                val years = simYears.roundToInt()

                // Projections:
                // 1. Cash eroded by inflation (assuming 28% inflation)
                val cashRealValue = principal * (1.0 - 0.28).pow(years.toDouble())
                // 2. NBE Treasury Bill (15.2% coupon compounded)
                val tbillValue = principal * (1.0 + 0.152).pow(years.toDouble())
                // 3. Physical Gold Hedge (estimated ~24% historical CAGR in ETB terms)
                val goldValue = principal * (1.0 + 0.24).pow(years.toDouble())

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "INFLATION VS. NBE T-BILL (15.2%) & GOLD HEDGE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )

                        OutlinedTextField(
                            value = simPrincipalInput,
                            onValueChange = { simPrincipalInput = it },
                            label = { Text("Initial Investment Capital (ETB)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = BorderStroke,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedLabelColor = GoldAccent,
                                unfocusedLabelColor = TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Holding Period Horizon",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "$years ${if (years == 1) "Year" else "Years"}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Slider(
                                value = simYears,
                                onValueChange = { simYears = it },
                                valueRange = 1f..5f,
                                steps = 3,
                                colors = SliderDefaults.colors(
                                    thumbColor = GoldAccent,
                                    activeTrackColor = GoldAccent,
                                    inactiveTrackColor = CardSurfaceElevated
                                )
                            )
                        }
                    }
                }
            }

            item {
                PazSectionHeader(
                    title = "Compound Projections after ${simYears.roundToInt()} Years",
                    subtitle = "Comparing purchasing power preservation across asset classes"
                )
            }

            val principal = simPrincipalInput.toDoubleOrNull() ?: 500000.0
            val years = simYears.roundToInt()
            val cashRealValue = principal * (1.0 - 0.28).pow(years.toDouble())
            val tbillValue = principal * (1.0 + 0.152).pow(years.toDouble())
            val goldValue = principal * (1.0 + 0.24).pow(years.toDouble())

            // Projection Card 1: Physical Gold
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurfaceElevated)
                        .border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PHYSICAL GOLD BULLION HEDGE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            PazBadge(
                                text = "HISTORICAL TOP HEDGE",
                                color = GoldAccent
                            )
                        }

                        Text(
                            text = String.format("%,.0f ETB", goldValue),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 20.sp
                            )
                        )

                        Text(
                            text = "Estimated +${String.format("%,.0f", goldValue - principal)} ETB gain (~24% historical annual ETB hedge).",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Projection Card 2: NBE Treasury Bills
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                        .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NBE TREASURY BILLS (15.2% YIELD)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            PazBadge(
                                text = "GUARANTEED YIELD",
                                color = CyanAccent
                            )
                        }

                        Text(
                            text = String.format("%,.0f ETB", tbillValue),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = CyanAccent,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 20.sp
                            )
                        )

                        Text(
                            text = "Compounded fixed income return yields +${String.format("%,.0f", tbillValue - principal)} ETB interest.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Projection Card 3: Uninvested Cash
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                        .border(1.dp, RedBearish.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "UNINVESTED CASH IN SAVINGS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RedBearish,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            PazBadge(
                                text = "INFLATION EROSION",
                                color = RedBearish
                            )
                        }

                        Text(
                            text = String.format("%,.0f ETB Real Value", cashRealValue),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = RedBearish,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 18.sp
                            )
                        )

                        Text(
                            text = "Purchasing power loses ~${String.format("%.0f%%", (1.0 - cashRealValue / principal) * 100)} against living expenses over ${years}yr.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
