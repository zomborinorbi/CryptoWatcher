package com.example.cryptowatcher.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptowatcher.R
import com.example.cryptowatcher.model.Crypto
import com.example.cryptowatcher.ui.theme.KingBlue
import com.example.cryptowatcher.ui.theme.CryptoWatcherTheme
import com.example.cryptowatcher.ui.theme.DarkGray
import com.example.cryptowatcher.ui.theme.Dimensions.dp16
import com.example.cryptowatcher.ui.theme.Dimensions.dp24
import com.example.cryptowatcher.ui.theme.Dimensions.dp8
import com.example.cryptowatcher.ui.theme.LightGreen
import com.example.cryptowatcher.ui.theme.Purple
import com.example.cryptowatcher.ui.theme.StrawberryRed
import com.example.cryptowatcher.ui.theme.SoftPurple
import com.example.cryptowatcher.ui.theme.SoftWhitePurple
import com.example.cryptowatcher.ui.theme.Typography
import com.example.cryptowatcher.ui.theme.White
import com.example.cryptowatcher.ui.util.FormattingUtil.formatPrice
import com.example.cryptowatcher.ui.util.FormattingUtil.roundPercentage
import com.example.cryptowatcher.ui.viewmodels.CryptoDetailsScreenViewModel

@Composable
fun CryptoDetailsScreen(
    navigateBackToHomeScreen: () -> Unit,
    viewModel: CryptoDetailsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState?.let { state ->
        CryptoDetailsContent(uiState = state, navigateBackToHomeScreen = navigateBackToHomeScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailsContent(
    uiState: CryptoDetailsScreenViewModel.UiState,
    navigateBackToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple,
                    titleContentColor = DarkGray,
                ),
                title = {
                    Text(text = uiState.details.name)
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBackToHomeScreen() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow_16x16),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(SoftPurple)
                .fillMaxSize()
        ) {
            Content(uiState)
        }
    }
}

@Composable
private fun Content(
    uiState: CryptoDetailsScreenViewModel.UiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = dp16, horizontal = dp24)
            .clip(RoundedCornerShape(dp16))
            .background(if (uiState.isDataRefreshing) White else SoftWhitePurple),
    ) {
        Column(
            modifier = Modifier
                .padding(dp24)
                .graphicsLayer { if (uiState.isDataRefreshing) alpha = 0.29f },
            verticalArrangement = Arrangement.spacedBy(dp8)
        ) {
            KeyValuePair(
                keyResourceId = R.string.crypto_details_screen_price_key,
                value = formatPrice(uiState.details.priceUsd)
            )
            KeyValuePair(
                keyResourceId = R.string.crypto_details_screen_change_24h_key,
                value = roundPercentage(uiState.details.changePercent24Hr),
                optionalColor = if (uiState.details.changePercent24Hr > 0) LightGreen else StrawberryRed
            )

            HorizontalDivider(color = KingBlue, modifier = Modifier.padding(vertical = dp16))

            KeyValuePair(
                keyResourceId = R.string.crypto_details_screen_market_cap_key,
                value = formatPrice(uiState.details.marketCapUsd)
            )
            KeyValuePair(
                keyResourceId = R.string.crypto_details_screen_volume_24h_key,
                value = formatPrice(uiState.details.volumeUsd24Hr)
            )
            KeyValuePair(
                keyResourceId = R.string.crypto_details_screen_supply_key,
                value = formatPrice(uiState.details.supply)
            )
        }

        if (uiState.isDataRefreshing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KingBlue)
        }
    }
}

@Composable
private fun KeyValuePair(
    keyResourceId: Int,
    value: String,
    modifier: Modifier = Modifier,
    optionalColor: Color? = null
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = stringResource(id = keyResourceId))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, style = Typography.titleLarge, color = optionalColor ?: Color.Black)
    }
}

@Preview
@Composable
fun CryptoDetailsScreenPreview(
    @PreviewParameter(CryptoDetailsScreenUiStateProvider::class)
    uiState: CryptoDetailsScreenViewModel.UiState
) {
    CryptoWatcherTheme {
        CryptoDetailsContent(uiState, {})
    }
}

private class CryptoDetailsScreenUiStateProvider :
    PreviewParameterProvider<CryptoDetailsScreenViewModel.UiState> {
    override val values = sequenceOf(
        CryptoDetailsScreenViewModel.UiState(
            DEFAULT_CRYPTO, false
        ),
        CryptoDetailsScreenViewModel.UiState(
            DEFAULT_CRYPTO, true
        )
    )

    companion object {
        val DEFAULT_CRYPTO = Crypto(
            id = "id",
            rank = 2,
            symbol = "BTC",
            name = "BITCOIN",
            supply = 256312.0000,
            maxSupply = 64000000.00000,
            marketCapUsd = 32112312.64513125,
            volumeUsd24Hr = 1355.3561321,
            priceUsd = 7584.5321,
            changePercent24Hr = -0.344,
            vwap24Hr = 3512.4322
        )
    }
}
