package sk.zimen.semestralka.api.types

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.utils.CsvObject
import sk.zimen.semestralka.utils.Mapper

/**
 * Class that holds common data of [Parcel] and [Property].
 * Implements [QuadTreeData] interface, so it can be inserted into QuadTree.
 * @author David Zimen
 */
@CsvObject
open class Place() : QuadTreeData<PlaceKey>() {

    var number: Int = 0
    var description: String? = null

    constructor(number: Int, topLeft: GpsPosition, bottomRight: GpsPosition) : this () {
        this.number = number
        this.key = Mapper.toKey(topLeft, bottomRight)
    }

    constructor(number: Int, description: String?, topLeft: GpsPosition, bottomRight: GpsPosition) : this(number, topLeft, bottomRight) {
        this.description = description
    }

    override fun toKey(boundary: Boundary): PlaceKey = Mapper.toKey(boundary)

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
