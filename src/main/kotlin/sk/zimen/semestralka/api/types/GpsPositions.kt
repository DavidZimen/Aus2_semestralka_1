package sk.zimen.semestralka.api.types

/**
 * Class that represent key of [Place], that will be stored in QuadTree.
 * @author David Zimen
 */
data class GpsPositions(val topLeft: GpsPosition, val bottomRight: GpsPosition) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other is GpsPositions) {
            topLeft == other.topLeft && bottomRight == other.bottomRight
        } else false
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + bottomRight.hashCode()
        return result
    }
}
