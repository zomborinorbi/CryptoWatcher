package com.example.cryptowatcher.ui.composables

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.cryptowatcher.ui.theme.Dimensions.cryptoImageSize
import com.example.cryptowatcher.ui.theme.Dimensions.dp12
import com.example.cryptowatcher.ui.theme.Dimensions.dp16
import com.example.cryptowatcher.ui.theme.Dimensions.dp24
import com.example.cryptowatcher.ui.theme.Dimensions.dp4
import com.example.cryptowatcher.ui.theme.LightGreen
import com.example.cryptowatcher.ui.theme.Purple
import com.example.cryptowatcher.ui.theme.StrawberryRed
import com.example.cryptowatcher.ui.theme.SoftPurple
import com.example.cryptowatcher.ui.theme.SoftWhitePurple
import com.example.cryptowatcher.ui.theme.Typography
import com.example.cryptowatcher.ui.util.FormattingUtil.formatPrice
import com.example.cryptowatcher.ui.util.FormattingUtil.mapIconNameToResourceId
import com.example.cryptowatcher.ui.util.FormattingUtil.roundPercentage
import com.example.cryptowatcher.ui.viewmodels.CryptoListScreenViewModel

@Composable
fun CryptoListScreen(
    navigateToDetails: (Crypto) -> Unit,
    viewModel: CryptoListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState?.let { state ->
        CryptoListContent(uiState = state, navigateToDetails = navigateToDetails)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CryptoListContent(
    uiState: CryptoListScreenViewModel.UiState,
    navigateToDetails: (Crypto) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple,
                    titleContentColor = DarkGray,
                ),
                title = {
                    Text(text = stringResource(id = R.string.crypto_list_screen_header_title))
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(SoftPurple)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is CryptoListScreenViewModel.UiState.Error -> Text(
                    text = stringResource(
                        id = R.string.crypto_list_screen_error_message,
                        uiState.errorMessage
                    ),
                    textAlign = TextAlign.Center
                )

                is CryptoListScreenViewModel.UiState.Loading ->
                    CircularProgressIndicator(color = KingBlue)

                is CryptoListScreenViewModel.UiState.Content -> ListContent(
                    uiState = uiState,
                    navigateToDetails,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun ListContent(
    uiState: CryptoListScreenViewModel.UiState.Content,
    navigateToDetails: (Crypto) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
            .fillMaxSize()
            .background(SoftPurple)
            .padding(horizontal = dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dp12)
    ) {
        item {
            Spacer(modifier = Modifier.height(dp4))
        }

        items(uiState.data.orEmpty()) { crypto ->
            CryptoListItem(crypto = crypto, navigateToDetails = navigateToDetails)
        }

        item {
            Spacer(modifier = Modifier.height(dp4))
        }
    }
}

@Composable
private fun CryptoListItem(
    crypto: Crypto,
    navigateToDetails: (Crypto) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dp16))
            .background(SoftWhitePurple)
            .padding(dp16)
            .clickable { navigateToDetails(crypto) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = mapIconNameToResourceId(crypto.name)),
            contentDescription = null,
            modifier = Modifier.size(cryptoImageSize)
        )

        Spacer(modifier = Modifier.width(dp16))

        Column {
            Text(
                text = crypto.name,
                style = Typography.titleLarge
            )
            Text(text = crypto.symbol)
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatPrice(crypto.priceUsd),
                style = Typography.titleLarge
            )
            Text(
                text = roundPercentage(crypto.changePercent24Hr),
                color = if (crypto.changePercent24Hr > 0) LightGreen else StrawberryRed,
                style = Typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun CryptoListScreenPreview(
    @PreviewParameter(CryptoListScreenUiStateProvider::class)
    uiState: CryptoListScreenViewModel.UiState
) {
    CryptoWatcherTheme {
        CryptoListContent(uiState = uiState, navigateToDetails = {})
    }
}

private class CryptoListScreenUiStateProvider :
    PreviewParameterProvider<CryptoListScreenViewModel.UiState> {
    override val values = sequenceOf(
        CryptoListScreenViewModel.UiState.Content(CRYPTO_LIST),
        CryptoListScreenViewModel.UiState.Loading,
        CryptoListScreenViewModel.UiState.Error("Error message")
    )

    companion object {
        private val CRYPTO_LIST = listOf(
            Crypto(
                id = "id",
                rank = 2,
                symbol = "BTC",
                name = "BITCOIN",
                supply = 12312.0000000,
                maxSupply = 210000000000.00000,
                marketCapUsd = 12112312.332131255,
                volumeUsd24Hr = 4324326.4321321,
                priceUsd = 5684.5321,
                changePercent24Hr = -0.234,
                vwap24Hr = 5323.4132
            ),
            Crypto(
                id = "id",
                rank = 2,
                symbol = "USDT",
                name = "TETHER",
                supply = 12312.0000000,
                maxSupply = 210000000000.00000,
                marketCapUsd = 12112312.332131255,
                volumeUsd24Hr = 4324326.4321321,
                priceUsd = 614.5321,
                changePercent24Hr = 0.364,
                vwap24Hr = 5323.4132
            ),
            Crypto(
                id = "id",
                rank = 2,
                symbol = "ETH",
                name = "ETHEREUM",
                supply = 12312.0000000,
                maxSupply = 210000000000.00000,
                marketCapUsd = 12112312.332131255,
                volumeUsd24Hr = 4324326.4321321,
                priceUsd = 1664.5321,
                changePercent24Hr = 0.124,
                vwap24Hr = 5323.4132
            )
        )
    }
}
