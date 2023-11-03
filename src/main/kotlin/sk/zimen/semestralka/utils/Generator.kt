package sk.zimen.semestralka.utils

import org.apache.commons.math3.random.RandomDataGenerator
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import java.util.*
import kotlin.math.abs
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Generator() {
    /**
     * Instance of [RandomDataGenerator].
     */
    private val random = Random()
    private var quadrantWidth: Double = 0.0
    private var quadrantHeight: Double = 0.0

    constructor(quadrantWidth: Double, quadrantHeight: Double) : this() {
        this.quadrantWidth = quadrantWidth
        this.quadrantHeight = quadrantHeight
    }

    fun <K : QuadTreeKey, T : QuadTreeData<K>> generateItems(
        itemClass: KClass<T>,
        boundary: Boundary,
        count: Int
    ): List<T> {
        this.quadrantWidth = abs(boundary.topLeft[0])
        this.quadrantHeight = abs(boundary.topLeft[1])
        val generatedItems = ArrayList<T>(count)
        while(generatedItems.size < count) {
            try {
                generatedItems.add(generateItem(itemClass))
            } catch (_: Exception) { }
        }
        return generatedItems
    }

    fun <K : QuadTreeKey, T : QuadTreeData<K>> generateTree(
        tree: QuadTree<K, T>,
        itemClass: KClass<T>,
        quadrantWidth: Double,
        quadrantHeight: Double,
        maxDepth: Int,
        itemCount: Int
    ): MutableList<T> {
        tree.changeParameters(maxDepth, -quadrantWidth, quadrantHeight, quadrantWidth, -quadrantHeight)
        this.quadrantWidth = quadrantWidth
        this.quadrantHeight = quadrantHeight
        val addedItems = ArrayList<T>(itemCount)
        while (tree.size < itemCount) {
            try {
                val item = generateItem(itemClass)
                addedItems.add(item)
                tree.insert(item)
            } catch (_: Exception) { }
        }
        return addedItems
    }

    private fun <K : QuadTreeKey, T : QuadTreeData<K>> generateItem(clazz: KClass<T>): T {
        val instance = clazz.createInstance()
        instance.key = instance.toKey(nextBoundary(generateSize()))
        return instance
    }

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
        return random.nextDouble(-quadrantWidth, quadrantWidth)
    }

    private fun widthCoordinate(lower: Double, size: GeneratedSize): Double {
        var higher: Double = lower + size.maxSize
        higher = if (DoubleUtils.isALessOrEqualsToB(higher, quadrantWidth)) higher else quadrantWidth
        return if (higher == lower) {
            higher
        } else random.nextDouble(lower, higher)
    }

    private fun heightCoordinate(): Double {
        return random.nextDouble(-quadrantHeight, quadrantHeight)
    }

    private fun heightCoordinate(higher: Double, size: GeneratedSize): Double {
        var lower: Double = higher - size.maxSize
        lower = if (DoubleUtils.isAGreaterOrEqualsToB(lower, -quadrantHeight)) lower else -quadrantHeight
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
    XXS(0.000_01),
    XS(0.000_1),
    S(0.001),
    M(0.01),
    L(0.1),
    XL(0.2),
    XXL(1.0)
}

