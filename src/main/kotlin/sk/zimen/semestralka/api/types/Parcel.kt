package sk.zimen.semestralka.api.types

/**
 * Class with data representing Parcel from assignment.
 * @author David Zimen
 */
class Parcel : Place {

    var propertiesForParcel: List<Property>? = null

    constructor(number: Int, topLeft: GpsPosition, bottomRight: GpsPosition) : super(
        number, topLeft, bottomRight
    )

    constructor(number: Int, description: String, topLeft: GpsPosition, bottomRight: GpsPosition) : super(
        number, description, topLeft, bottomRight
    )
}
