package sk.zimen.semestralka.api.types

/**
 * Class with data representing Property from assignment.
 * @author David Zimen
 */
class Property : Place {
    constructor(number: Int, topLeft: GpsPosition, bottomRight: GpsPosition) : super(
        number, topLeft,
        bottomRight
    )

    constructor(number: Int, description: String, topLeft: GpsPosition, bottomRight: GpsPosition) : super(
        number, description,
        topLeft,
        bottomRight
    )
}
