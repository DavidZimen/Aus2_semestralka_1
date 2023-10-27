package sk.zimen.semestralka.data

import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.utils.Mapper

/**
 * Class that holds common data of [Parcel] and [Property].
 * Implements [QuadTreeData] interface, so it can be inserted into QuadTree.
 * @author David Zimen
 */
open class Place(var number: Int, topLeft: GpsPosition, bottomRight: GpsPosition) : QuadTreeData<PlaceKey>(Mapper.toKey(topLeft, bottomRight)) {
    var description: String? = null

    constructor(number: Int, description: String?, topLeft: GpsPosition, bottomRight: GpsPosition) : this(number, topLeft, bottomRight) {
        this.description = description
    }

    override fun printData() { }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        return if (other is Place) {
            this.key == other.key && description == other.description && number == other.number
        } else false
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}
