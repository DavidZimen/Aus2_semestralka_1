package sk.zimen.semestralka_1_kotlin.utils

import sk.zimen.semestralka_1_kotlin.data.GpsPosition
import sk.zimen.semestralka_1_kotlin.data.HeightPos
import sk.zimen.semestralka_1_kotlin.data.PlaceKey
import sk.zimen.semestralka_1_kotlin.data.WidthPos
import sk.zimen.semestralka_1_kotlin.quadtree.boundary.Boundary
import kotlin.math.abs

/**
 * @author David Zimen
 */
object Mapper {
    /**
     * Maps [Boundary] to 2 [GpsPosition]s.
     * @param b Boundary to be mapped.
     * @return Array with size of 2, where index 0 is topLeft and index 1 is bottomRight position.
     */
    fun toGpsPositions(b: Boundary): Array<GpsPosition> {
        return arrayOf(
            GpsPosition(
                abs(b.topLeft[0]),
                if (DoubleUtils.isALessOrEqualsToB(b.topLeft[0], 0.0)) WidthPos.Z else WidthPos.V,
                abs(b.topLeft[1]),
                if (DoubleUtils.isALessOrEqualsToB(b.topLeft[1], 0.0)) HeightPos.J else HeightPos.S
            ),
            GpsPosition(
                abs(b.bottomRight[0]),
                if (DoubleUtils.isALessOrEqualsToB(b.bottomRight[0], 0.0)) WidthPos.Z else WidthPos.V,
                abs(b.bottomRight[1]),
                if (DoubleUtils.isALessOrEqualsToB(b.bottomRight[1], 0.0)) HeightPos.J else HeightPos.S
            )
        )
    }

    /**
     * @param topLeft Top left [GpsPosition] of bounding polygon.
     * @param bottomRight Bottom right [GpsPosition] of bounding polygon.
     * @return Instance of [Boundary] corresponding to provided [GpsPosition]s.
     */
    fun toBoundary(topLeft: GpsPosition, bottomRight: GpsPosition): Boundary {
        return Boundary(
            doubleArrayOf(
                if (topLeft.widthPosition === WidthPos.Z) -topLeft.width else topLeft.width,
                if (topLeft.heightPosition === HeightPos.J) -topLeft.height else topLeft.height
            ), doubleArrayOf(
                if (bottomRight.widthPosition === WidthPos.Z) -bottomRight.width else bottomRight.width,
                if (bottomRight.heightPosition === HeightPos.J) -bottomRight.height else bottomRight.height
            )
        )
    }

    fun toBoundary(positions: PlaceKey): Boundary {
        return toBoundary(positions.topLeft, positions.bottomRight)
    }

    fun toBoundary(position: GpsPosition): Boundary {
        return toBoundary(position, position)
    }

    fun toKey(position: GpsPosition): PlaceKey {
        return PlaceKey(position, position)
    }

    fun toKey(topLeft: GpsPosition, bottomRight: GpsPosition): PlaceKey {
        return PlaceKey(topLeft, bottomRight)
    }
}
