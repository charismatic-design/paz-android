package com.paz.android.data

import kotlinx.serialization.Serializable

@Serializable
data class HeadlineItem(
    val title: String,
    val source: String,
    val link: String,
    val pubDate: String
)

@Serializable
data class AIInsight(
    val sentiment: String = "NEUTRAL",
    val summary: String = "",
    val headline_count: Int? = null,
    val last_scanned: String? = null
)

@Serializable
data class MacroNewsResponse(
    val headlines: List<HeadlineItem> = emptyList(),
    val ai_insight: AIInsight = AIInsight()
)

@Serializable
data class BankCurrencyRate(
    val buying: Double = 0.0,
    val selling: Double = 0.0,
    val spread: Double = 0.0,
    val spread_pct: Double = 0.0
)

@Serializable
data class BankComparisonItem(
    val bank_code: String,
    val bank_name: String,
    val bank_type: String = "Commercial",
    val currencies: Map<String, BankCurrencyRate> = emptyMap()
)

@Serializable
data class BestRateInfo(
    val bank: String,
    val rate: Double,
    val code: String? = null
)

@Serializable
data class BankRatesResponse(
    val timestamp: String = "",
    val currencies: List<String> = listOf("USD", "EUR", "GBP", "AED"),
    val nbe_treasury_yield_pct: Double? = 15.2,
    val best_to_sell_fx: Map<String, BestRateInfo>? = null,
    val best_to_buy_fx: Map<String, BestRateInfo>? = null,
    val banks: List<BankComparisonItem> = emptyList()
)

@Serializable
data class GoldKaratInfo(
    val name: String,
    val per_gram_etb: Double? = null,
    val per_gram_usd: Double? = null,
    val total_etb: Double? = null
)

@Serializable
data class LiveGoldResponse(
    val spot_usd_per_oz: Double = 2748.50,
    val usd_etb_rate: Double = 163.29,
    val timestamp: String = "",
    val karats: Map<String, GoldKaratInfo> = emptyMap(),
    val trend_7d: List<Double>? = null,
    val change_24h: String? = "+0.85%"
)

@Serializable
data class FxRatesResponse(
    val base: String = "ETB",
    val rates: Map<String, Double> = mapOf("USD" to 163.29, "EUR" to 176.45, "GBP" to 212.10, "AED" to 44.46),
    val gold_per_gram_etb: Double = 21315.0,
    val last_updated: String = ""
)

@Serializable
data class PriceAlert(
    val id: Int,
    val ticker: String,
    val target_price: Double,
    val direction: String = "ABOVE",
    val created_at: String = ""
)

@Serializable
data class AlertsResponse(
    val alerts: List<PriceAlert> = emptyList()
)

@Serializable
data class RemittanceChannelItem(
    val id: String,
    val name: String,
    val subtitle: String = "",
    val badge: String = "",
    val is_best: Boolean = false,
    val transfer_fee_source: Double = 0.0,
    val effective_exchange_rate: Double = 0.0,
    val gross_etb: Double = 0.0,
    val net_etb_received: Double = 0.0,
    val delivery_time: String = "Instant",
    val channels_supported: List<String> = emptyList(),
    val description: String = "",
    val diff_vs_best_etb: Double? = null
)

@Serializable
data class RemittanceResponse(
    val timestamp: String = "",
    val source_amount: Double = 1000.0,
    val source_currency: String = "USD",
    val base_market_rate: Double = 163.29,
    val channels: List<RemittanceChannelItem> = emptyList()
)

data class PazUiState(
    val news: List<HeadlineItem> = emptyList(),
    val aiInsight: AIInsight = AIInsight(),
    val bankData: BankRatesResponse = BankRatesResponse(),
    val remittanceData: RemittanceResponse = RemittanceResponse(),
    val goldData: LiveGoldResponse = LiveGoldResponse(),
    val fxRates: FxRatesResponse = FxRatesResponse(),
    val alerts: List<PriceAlert> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val activeHost: String = "http://10.0.2.2:8000",
    val errorMessage: String? = null
)
