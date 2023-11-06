package sk.zimen.semestralka.api.types

import sk.zimen.semestralka.utils.DoubleUtils

/**
 * Class to hold data about GPS position on map.
 * @author David Zimen
 */
class GpsPosition() {

    var width: Double = 0.0
    var widthPosition: WidthPos = WidthPos.Z
    var height: Double = 0.0
    var heightPosition: HeightPos = HeightPos.S

    constructor(width: Double, widthPosition: WidthPos, height: Double, heightPosition: HeightPos): this() {
        this.width = width
        this.widthPosition = widthPosition
        this.height = height
        this.heightPosition = heightPosition
    }


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
