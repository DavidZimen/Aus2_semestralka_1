package sk.zimen.semestralka.data

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.utils.Mapper

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
            topLeft == other.topLeft && bottomRight == other.bottomRight
        } else false
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + bottomRight.hashCode()
        return result
    }
}
