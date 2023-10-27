package sk.zimen.semestralka.utils

import org.apache.commons.math3.random.RandomDataGenerator
import sk.zimen.semestralka.data.GpsPosition
import sk.zimen.semestralka.quadtree.boundary.Boundary
import java.util.*


class Generator {
    /**
     * Instance of [RandomDataGenerator].
     */
    val random = Random()

    fun nextPositions(size: GeneratedSize): Array<GpsPosition> {
        return Mapper.toGpsPositions(nextBoundary(size))
    }

    fun generateSize(): GeneratedSize {
        val probability = random.nextDouble()
        return if (probability < 0.2) {
            GeneratedSize.XXS
        } else if (probability < 0.5) {
            GeneratedSize.XS
        } else if (probability < 0.75) {
            GeneratedSize.S
        } else if (probability < 0.9) {
            GeneratedSize.M
        } else if (probability < 0.95) {
            GeneratedSize.L
        } else if (probability < 0.98) {
            GeneratedSize.XL
        } else {
            GeneratedSize.XXL
        }
    }

    private fun nextBoundary(size: GeneratedSize): Boundary {
        val topX = this.widthCoordinate()
        val topY = this.heightCoordinate()
        val bottomX = this.widthCoordinate(topX, size)
        val bottomY = this.heightCoordinate(topY, size)
        return Boundary(doubleArrayOf(topX, topY), doubleArrayOf(bottomX, bottomY))
    }

    private fun widthCoordinate(): Double {
        return random.nextInt(-1800000, 1800000) / 10000.0
    }

    private fun widthCoordinate(lower: Double, size: GeneratedSize): Double {
        var higher: Double = lower + size.maxSize
        higher = if (DoubleUtils.isALessOrEqualsToB(higher, 180.0)) higher else 180.0
        return if (higher == lower) {
            higher
        } else random.nextDouble(lower, higher)
    }

    private fun heightCoordinate(): Double {
        return random.nextInt(-900000, 900000) / 10000.0
    }

    private fun heightCoordinate(higher: Double, size: GeneratedSize): Double {
        var lower: Double = higher - size.maxSize
        lower = if (DoubleUtils.isAGreaterOrEqualsToB(lower, -90.0)) lower else -90.0
        return if (higher == lower) {
            higher
        } else random.nextDouble(lower, higher)
    }
}

/**
 * Enum values to represent max distance between 2 generated [GpsPosition]s.
 * @author David Zimen
 */
enum class GeneratedSize(val maxSize: Double) {
    XXS(0.0001),
    XS(0.001),
    S(0.1),
    M(0.2),
    L(0.5),
    XL(1.0),
    XXL(2.0)
}

