package com.paz.android.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paz.android.data.HeadlineItem
import com.paz.android.data.PazUiState
import com.paz.android.ui.components.HeroRateCard
import com.paz.android.ui.components.PazBadge
import com.paz.android.ui.components.PazSectionHeader
import com.paz.android.ui.theme.*

@Composable
fun PulseTab(
    state: PazUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val usdRate = state.fxRates.rates["USD"] ?: 163.29
    val goldRate = state.goldData.karats["24K"]?.per_gram_etb ?: state.fxRates.gold_per_gram_etb

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Rate Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroRateCard(
                    label = "USD / ETB Benchmark",
                    tag = "Floating FX",
                    number = String.format("%.2f", usdRate),
                    currency = "ETB",
                    footnote = "Interbank Floating FX",
                    accentColor = CyanAccent,
                    modifier = Modifier.weight(1f)
                )

                HeroRateCard(
                    label = "Physical Gold 24K",
                    tag = "Spot $2,748/oz",
                    number = String.format("%,.0f", goldRate),
                    currency = "ETB/g",
                    footnote = "Pure 99.9% Bullion",
                    accentColor = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // AI Market Sentiment Card
        item {
            val sentimentColor = when (state.aiInsight.sentiment.uppercase()) {
                "BULLISH" -> GreenBullish
                "BEARISH" -> RedBearish
                else -> GoldAccent
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardSurface)
                    .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "AI MACRO INSIGHT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        PazBadge(
                            text = state.aiInsight.sentiment.uppercase(),
                            color = sentimentColor
                        )
                    }

                    Text(
                        text = state.aiInsight.summary.ifEmpty {
                            "Ethiopia's floating FX regime demonstrates stabilizing interbank liquidity. Commercial banks report rising foreign currency deposits while NBE 15.2% T-bills and physical gold provide institutional inflation hedging."
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )

                    HorizontalDivider(color = BorderStroke, thickness = 0.8.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Scanned: ${state.aiInsight.headline_count ?: 14} Macro Sources",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "Updated: ${state.aiInsight.last_scanned ?: "Real-time"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Live Ethiopian Macro News Section Header
        item {
            PazSectionHeader(
                title = "Live Macro News & Dispatches",
                subtitle = "Automated scanner across verified Ethiopian financial outlets"
            )
        }

        // News Items
        items(state.news) { item: HeadlineItem ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSurface)
                    .border(1.dp, BorderStroke, RoundedCornerShape(12.dp))
                    .clickable {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                            context.startActivity(browserIntent)
                        } catch (_: Exception) {}
                    }
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.source.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = item.pubDate,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            lineHeight = 17.sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Read Full Dispatch",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
