package ir.rainyday.android.common.helpers

import android.graphics.Color

/**
 * Created by taghipour on 15/10/2017.
 */


object ColorHelper{
    fun getColorOfDegradate(colorStart: Int, colorEnd: Int, percent: Int): Int {
        return Color.rgb(
                getColorOfDegradateCalculation(Color.red(colorStart), Color.red(colorEnd), percent),
                getColorOfDegradateCalculation(Color.green(colorStart), Color.green(colorEnd), percent),
                getColorOfDegradateCalculation(Color.blue(colorStart), Color.blue(colorEnd), percent)
        )
    }

    private fun getColorOfDegradateCalculation(colorStart: Int, colorEnd: Int, percent: Int): Int {
        return (Math.min(colorStart, colorEnd) * (100 - percent) + Math.max(colorStart, colorEnd) * percent) / 100
    }

    fun adjustColorAlpha(opaqueColor: Int, factor: Float): Int {
        return (factor * 255.0f).toInt() shl 24 or (opaqueColor and 0x00ffffff)
    }
}