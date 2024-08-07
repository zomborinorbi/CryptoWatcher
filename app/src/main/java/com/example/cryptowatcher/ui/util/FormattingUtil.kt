package com.example.cryptowatcher.ui.util

import androidx.annotation.DrawableRes
import com.example.cryptowatcher.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

object FormattingUtil {

    private const val THOUSAND = 1_000
    private const val MILLION = 1_000_000
    private const val BILLION = 1_000_000_000

    private const val DEFAULT_FORMAT = "$%.2f"
    private const val THOUSAND_FORMAT = "$%.2fK"
    private const val MILLION_FORMAT = "$%.2fM"
    private const val BILLION_FORMAT = "$%.2fB"

    private const val SCALE = 2
    private const val PERCENTAGE = "%"

    /**
     * Formats a given price value to a readable string with abbreviations suffixes (K for thousands, M for millions, B for billions).
     *
     * @param value the price to format.
     * @return a formatted price string with the suffix.
     */
    fun formatPrice(value: Double): String {
        val defaultLocale = Locale.getDefault()

        return when {
            value >= BILLION -> String.format(defaultLocale, BILLION_FORMAT, value / BILLION)
            value >= MILLION -> String.format(defaultLocale, MILLION_FORMAT, value / MILLION)
            value >= THOUSAND -> String.format(defaultLocale, THOUSAND_FORMAT, value / THOUSAND)
            else -> String.format(defaultLocale, DEFAULT_FORMAT, value)
        }
    }

    /**
     * Rounds a given percentage value to two decimal places and appends a percentage sign.
     *
     * @param value the percentage to round.
     * @return a rounded percentage string with two decimal places and a percentage sign.
     */
    fun roundPercentage(value: Double): String {
        return BigDecimal(value).setScale(SCALE, RoundingMode.HALF_UP).toString() + PERCENTAGE
    }

    /**
     * Maps a given icon name to its corresponding drawable resource ID.
     *
     * @param iconName the name of the icon to map.
     * @return the drawable resource ID corresponding to the icon name, or a default icon resource ID if the name is not found.
     */
    @DrawableRes
    fun mapIconNameToResourceId(iconName: String): Int {
        val iconMap = IconMappings.ICON_MAP
        return iconMap[iconName.lowercase()] ?: R.drawable.defaulticon
    }
}
