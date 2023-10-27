package sk.zimen.semestralka.quadtree.boundary

import sk.zimen.semestralka.quadtree.exceptions.BoundaryException
import sk.zimen.semestralka.utils.DoubleUtils

/**
 * Represents boundary of the Node in QuadTree.
 * @param topLeft Top left coordinates of the boundary as array with length 2.
 * - Index 0 represents point X.
 * - Index 1 represents point Y.
 * @param bottomRight Bottom right coordinates of the boundary as array with length 2.
 * - Index 0 represents point X.
 * - Index 1 represents point Y.
 * @author David Zimen
 */
data class Boundary(val topLeft: DoubleArray, val bottomRight: DoubleArray) {

    init {
        if (!isCorrectBoundary(topLeft, bottomRight)) {
            throw BoundaryException(topLeft, bottomRight)
        }
    }

    /**
     * @param boundary Boundary to be checked against current boundary.
     * @return Whether provided boundary is within current boundary.
     */
    fun contains(boundary: Boundary): Boolean {
        return DoubleUtils.isALessOrEqualsToB(topLeft[0], boundary.topLeft[0])
                && DoubleUtils.isAGreaterOrEqualsToB(topLeft[1], boundary.topLeft[1])
                && DoubleUtils.isAGreaterOrEqualsToB(bottomRight[0], boundary.bottomRight[0])
                && DoubleUtils.isALessOrEqualsToB(bottomRight[1], boundary.bottomRight[1])
    }

    /**
     * Check if at least one of the points of provided [Boundary] is withing current boundary.
     * @param boundary Provided boundary.
     */
    fun intersects(boundary: Boundary): Boolean {
        return (DoubleUtils.isABetweenBandC(boundary.topLeft[0], topLeft[0], bottomRight[0])
                || DoubleUtils.isABetweenBandC(boundary.bottomRight[0], topLeft[0], bottomRight[0]))
                && (DoubleUtils.isABetweenBandC(boundary.topLeft[1], bottomRight[1], topLeft[1])
                || DoubleUtils.isABetweenBandC(boundary.bottomRight[1], bottomRight[1], topLeft[1]))
    }

    /**
     * @param topLeft     Top left coordinates.
     * @param bottomRight Bottom right coordinate.
     * @return Whether provided coordinates can make a boundary.
     */
    private fun isCorrectBoundary(topLeft: DoubleArray, bottomRight: DoubleArray): Boolean {
        // incorrect lengths of arrays
        if (topLeft.size != 2 || bottomRight.size != 2) {
            return false
        }

        // if left is not on the left from right
        return if (DoubleUtils.isAGreaterThanB(topLeft[0], bottomRight[0])) {
            false
        } else DoubleUtils.isAGreaterOrEqualsToB(topLeft[1], bottomRight[1])
    }

    override fun hashCode(): Int {
        var result = topLeft.contentHashCode()
        result = 31 * result + bottomRight.contentHashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other is Boundary) {
            (DoubleUtils.isAEqualsToB(topLeft[0], other.topLeft[0])
                    && DoubleUtils.isAEqualsToB(topLeft[1], other.topLeft[1])
                    && DoubleUtils.isAEqualsToB(bottomRight[0], other.bottomRight[0])
                    && DoubleUtils.isAEqualsToB(bottomRight[1], other.bottomRight[1]))
        } else false
    }

    companion object {
        @Throws(IllegalArgumentException::class)
        fun createBoundaryOnPosition(p: Position, b: Boundary): Boundary {
            val topLeft = b.topLeft.copyOf(2)
            val bottomRight = b.bottomRight.copyOf(2)
            return when (p) {
                Position.TOP_LEFT -> Boundary(
                    topLeft, doubleArrayOf(
                        DoubleUtils.average(topLeft[0], bottomRight[0]),
                        DoubleUtils.average(topLeft[1], bottomRight[1])
                    )
                )
                Position.BOTTOM_LEFT -> Boundary(
                    doubleArrayOf(
                        topLeft[0],
                        DoubleUtils.average(topLeft[1], bottomRight[1])
                    ), doubleArrayOf(
                        DoubleUtils.average(topLeft[0], bottomRight[0]),
                        bottomRight[1]
                    )
                )
                Position.TOP_RIGHT -> Boundary(
                    doubleArrayOf(
                        DoubleUtils.average(topLeft[0], bottomRight[0]),
                        topLeft[1]
                    ), doubleArrayOf(
                        bottomRight[0],
                        DoubleUtils.average(topLeft[1], bottomRight[1])
                    )
                )
                Position.BOTTOM_RIGHT -> Boundary(
                    doubleArrayOf(
                        DoubleUtils.average(topLeft[0], bottomRight[0]),
                        DoubleUtils.average(topLeft[1], bottomRight[1])
                    ),
                    bottomRight
                )
                Position.CURRENT -> throw IllegalArgumentException("Current position has assigned boundary")
            }
        }
    }
}
