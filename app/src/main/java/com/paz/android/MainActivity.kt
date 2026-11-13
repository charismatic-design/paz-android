package com.paz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paz.android.data.PazApiService
import com.paz.android.data.PazUiState
import com.paz.android.ui.screens.*
import com.paz.android.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val apiService = PazApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PazTheme {
                PazMainScreen(apiService = apiService)
            }
        }
    }
}

enum class PazTab(val title: String) {
    PULSE("Pulse"),
    FX("FX"),
    GOLD("Gold"),
    ESX("ESX")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PazMainScreen(
    apiService: PazApiService
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(PazTab.PULSE) }
    var uiState by remember { mutableStateOf(apiService.getFallbackUiState()) }
    var showAlertsSheet by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }

    // Load live data on launch
    LaunchedEffect(Unit) {
        uiState = uiState.copy(isLoading = true)
        val loaded = apiService.loadAllData()
        uiState = loaded
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PAZ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GreenBullish.copy(alpha = 0.15f))
                                .border(0.5.dp, GreenBullish.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ADDIS LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GreenBullish,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Sync Button
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isSyncing = true
                                apiService.triggerSync()
                                val refreshed = apiService.loadAllData()
                                uiState = refreshed
                                isSyncing = false
                            }
                        }
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = CyanAccent,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Sync",
                                tint = CyanAccent
                            )
                        }
                    }

                    // Alerts Sheet Trigger
                    IconButton(onClick = { showAlertsSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alerts",
                            tint = GoldAccent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianBg,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CardSurface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == PazTab.PULSE,
                    onClick = { selectedTab = PazTab.PULSE },
                    icon = { Icon(Icons.Default.ShowChart, contentDescription = "Pulse") },
                    label = { Text("Pulse") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == PazTab.FX,
                    onClick = { selectedTab = PazTab.FX },
                    icon = { Icon(Icons.Default.CurrencyExchange, contentDescription = "FX") },
                    label = { Text("FX") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == PazTab.GOLD,
                    onClick = { selectedTab = PazTab.GOLD },
                    icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = "Gold") },
                    label = { Text("Gold") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == PazTab.ESX,
                    onClick = { selectedTab = PazTab.ESX },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "ESX") },
                    label = { Text("ESX") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Crossfade(targetState = selectedTab, label = "TabSwitch") { tab ->
                when (tab) {
                    PazTab.PULSE -> PulseTab(state = uiState)
                    PazTab.FX -> FxTab(state = uiState)
                    PazTab.GOLD -> GoldTab(state = uiState)
                    PazTab.ESX -> EsxTab()
                }
            }

            if (showAlertsSheet) {
                PriceAlertsBottomSheet(
                    alerts = uiState.alerts,
                    onAddAlert = { ticker, targetPrice ->
                        coroutineScope.launch {
                            val newAlert = apiService.createAlert(ticker, targetPrice)
                            if (newAlert != null) {
                                uiState = uiState.copy(alerts = listOf(newAlert) + uiState.alerts)
                            }
                        }
                    },
                    onDeleteAlert = { alertId ->
                        coroutineScope.launch {
                            apiService.deleteAlert(alertId)
                            uiState = uiState.copy(alerts = uiState.alerts.filter { it.id != alertId })
                        }
                    },
                    onDismiss = { showAlertsSheet = false }
                )
            }
        }
    }
}
