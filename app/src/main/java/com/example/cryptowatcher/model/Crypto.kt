package com.example.cryptowatcher.model

data class Crypto(
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: Double,
    val maxSupply: Double,
    val marketCapUsd: Double,
    val volumeUsd24Hr: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val vwap24Hr: Double
)
