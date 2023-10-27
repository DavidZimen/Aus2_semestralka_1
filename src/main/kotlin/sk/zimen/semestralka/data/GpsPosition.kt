package sk.zimen.semestralka.data

import sk.zimen.semestralka.utils.DoubleUtils

/**
 * Class to hold data about GPS position on map.
 * @author David Zimen
 */
data class GpsPosition(val width: Double, val widthPosition: WidthPos, val height: Double, val heightPosition: HeightPos) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other is GpsPosition) {
            DoubleUtils.isAEqualsToB(
                width,
                other.width
            ) && widthPosition == other.widthPosition && DoubleUtils.isAEqualsToB(
                height, other.height
            ) && heightPosition == other.heightPosition
        } else false
    }

    override fun hashCode(): Int {
        var result = width.hashCode()
        result = 31 * result + widthPosition.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + heightPosition.hashCode()
        return result
    }
}

/**
 * Enum with possibilities for width in [GpsPosition].
 */
enum class WidthPos {
    Z,
    V
}

/**
 * Enum with possibilities for height in [GpsPosition].
 */
enum class HeightPos {
    S,
    J
}
