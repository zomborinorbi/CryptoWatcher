package com.example.cryptowatcher.testdata

import com.example.cryptowatcher.model.Crypto

const val LIST_SIZE = 10
const val TEST_CRYPTO_ID = "bitcoin"
const val TEST_ERROR_MESSAGE = "Error message"

val TEST_CRYPTO = Crypto(
    TEST_CRYPTO_ID,
    1,
    "BTC",
    "Bitcoin",
    18000000.0,
    21000000.0,
    600000000000.0,
    50000000000.0,
    30000.0,
    2.0,
    29000.0
)
val TEST_CRYPTO_LIST = listOf(TEST_CRYPTO)

