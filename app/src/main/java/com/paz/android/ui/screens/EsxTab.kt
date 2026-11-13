package com.paz.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paz.android.ui.components.PazBadge
import com.paz.android.ui.components.PazSectionHeader
import com.paz.android.ui.theme.*

data class ChecklistItem(
    val title: String,
    val subtitle: String,
    val isComplete: Boolean
)

@Composable
fun EsxTab(
    modifier: Modifier = Modifier
) {
    var emailInput by remember { mutableStateOf("") }
    var isSubscribed by remember { mutableStateOf(false) }

    val checklist = listOf(
        ChecklistItem("ECMA Regulatory Framework", "Directives 1001 & 1002 enacted by capital market authority", true),
        ChecklistItem("Trading Engine & Clearinghouse Integration", "Matching engine calibrated with multi-bank settlement", true),
        ChecklistItem("Licensed Broker-Dealer Onboarding", "Commercial bank investment arms certified for floor trading", true),
        ChecklistItem("Ethio Telecom Anchor Listing (10%)", "Flagship institutional retail tranche awaiting opening bell", false),
        ChecklistItem("Commercial Bank Secondary Listings", "Top private lenders preparing dual listing prospectuses", false)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Launching Soon Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardSurface)
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INSTITUTIONAL EQUITIES",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        PazBadge(text = "LAUNCHING SOON", color = GoldAccent)
                    }

                    Text(
                        text = "Ethiopian Securities Exchange (ESX)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    )

                    Text(
                        text = "The historic introduction of Ethiopia's modern capital market. PAZ will deliver real-time order books, live equity quotes, and automated execution telemetry direct to this terminal upon market opening.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Readiness Checklist Section
        item {
            PazSectionHeader(
                title = "Launch Readiness & Milestones",
                subtitle = "Tracking institutional infrastructure rollout ahead of market open"
            )
        }

        // Checklist Items
        items(checklist.size) { index ->
            val item = checklist[index]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSurface)
                    .border(1.dp, BorderStroke, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (item.isComplete) GreenBullish.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.06f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (item.isComplete) Icons.Default.Check else Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = if (item.isComplete) GreenBullish else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }

                    PazBadge(
                        text = if (item.isComplete) "COMPLETE" else "IN PROGRESS",
                        color = if (item.isComplete) GreenBullish else GoldAccent
                    )
                }
            }
        }

        // 1-Click Waitlist Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardSurfaceElevated)
                    .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "1-CLICK PRIORITY WAITLIST",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    Text(
                        text = "Get notified the instant the ESX opening bell sounds with instant order routing and live ticker feeds.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    if (!isSubscribed) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                placeholder = { Text("investor@domain.com") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.EmailAddress),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = BorderStroke,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedPlaceholderColor = TextMuted,
                                    unfocusedPlaceholderColor = TextMuted
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = {
                                    if (emailInput.contains("@") || emailInput.isNotBlank()) {
                                        isSubscribed = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = ObsidianBg
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Text(
                                    text = "Join",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(GreenBullish.copy(alpha = 0.12f))
                                .border(1.dp, GreenBullish.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GreenBullish,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "✓ Priority Access Confirmed. You are subscribed to PAZ ESX market open dispatches.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GreenBullish,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
