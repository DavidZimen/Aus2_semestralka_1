package sk.zimen.semestralka_1_kotlin.data

import sk.zimen.semestralka_1_kotlin.quadtree.boundary.Boundary
import sk.zimen.semestralka_1_kotlin.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka_1_kotlin.utils.Mapper

/**
 * Class that represent key of [Place], that will be stored in QuadTree.
 * @author David Zimen
 */
data class PlaceKey(val topLeft: GpsPosition, val bottomRight: GpsPosition) : QuadTreeKey {
    override fun toBoundary(): Boundary {
        return Mapper.toBoundary(topLeft, bottomRight)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other is PlaceKey) {
            topLeft.equals(other.topLeft) && bottomRight.equals(other.bottomRight)
        } else false
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + bottomRight.hashCode()
        return result
    }
}
