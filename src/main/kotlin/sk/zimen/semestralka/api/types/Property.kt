package sk.zimen.semestralka.api.types

import sk.zimen.semestralka.utils.CsvExclude
import sk.zimen.semestralka.utils.Mapper

/**
 * Class with data representing Property from assignment.
 * @author David Zimen
 */
class Property() : Place() {

    @CsvExclude
    var parcelsForProperty: MutableList<Parcel> = mutableListOf()

    constructor(number: Int, topLeft: GpsPosition, bottomRight: GpsPosition) : this() {
        this.number = number
        this.key = Mapper.toKey(topLeft, bottomRight)
    }

    constructor(number: Int, description: String, topLeft: GpsPosition, bottomRight: GpsPosition) : this(
        number, topLeft, bottomRight
    ) {
        this.description = description
    }
}
