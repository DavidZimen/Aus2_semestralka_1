package sk.zimen.semestralka.utils

import org.apache.commons.math3.random.RandomDataGenerator
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Generator() {
    /**
     * Instance of [RandomDataGenerator].
     */
    val random = Random()
    var leftX: Double = -180.0
    var topY: Double = 90.0
    var rightX: Double = 180.0
    var bottomY: Double = -90.0

    constructor(quadrantWidth: Double, quadrantHeight: Double) : this() {
        setCoordinates(-quadrantWidth, quadrantHeight, quadrantWidth, -quadrantHeight)
    }

    constructor(leftX: Double, topY: Double, rightX: Double, bottomY: Double) : this() {
        setCoordinates(leftX, topY, rightX, bottomY)
    }

    fun <T : Place> generateItems(
        itemClass: KClass<T>,
        count: Int,
        boundary: Boundary? = null
    ): MutableList<T> {
        setCoordinates(boundary)
        val items = ArrayList<T>(count)
        while(items.size < count) {
            try {
                val item = generateItem(itemClass)
                items.add(item)
            } catch (_: Exception) { }
        }
        return items
    }

    fun generateBoundaries(count: Int, boundary: Boundary? = null): MutableList<Boundary> {
        setCoordinates(boundary)
        val boundaries = ArrayList<Boundary>(count)
        while(boundaries.size < count) {
            try {
                boundaries.add(nextBoundary(generateSize()))
            } catch (_: Exception) { }
        }
        return boundaries
    }

    /**
     * Generates operations from [GeneratedOperation] enum with a given ratio.
     * @param count Number of operations to be generated.
     * @param ratio Double array of size 4, where
     *              0 -> INSERT
     *              1 -> DELETE
     *              2 -> EDIT
     *              3 -> FIND
     */
    fun generateOperations(count: Int, ratio: IntArray): Stack<GeneratedOperation>? {
        if (count < 1 || ratio.size != 4) {
            return null
        }
        val probs = DoubleArray(4)
        val sum = ratio.sum()
        ratio.forEachIndexed { index, i ->
            probs[index] = i.toDouble() / sum
        }

        val operations = Stack<GeneratedOperation>()
        for (i in 0 until count) {
            val probability = random.nextDouble()
            if (probability < probs[0]) {
                operations.push(GeneratedOperation.INSERT)
            } else if (probability < probs[0] + probs[1]) {
                operations.push(GeneratedOperation.DELETE)
            } else if (probability < probs[0] + probs[1] + probs[2]) {
                operations.push(GeneratedOperation.EDIT)
            } else {
                operations.push(GeneratedOperation.FIND)
            }
        }
        return operations
    }

    fun <T : Place> generateTree(
        tree: QuadTree<T>,
        itemClass: KClass<T>,
        quadrantWidth: Double,
        quadrantHeight: Double,
        maxDepth: Int,
        itemCount: Int
    ): MutableList<T> {
        tree.changeParameters(maxDepth, -quadrantWidth, quadrantHeight, quadrantWidth, -quadrantHeight)
        this.leftX = quadrantWidth
        this.topY = quadrantHeight
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

    private fun <T : Place> generateItem(clazz: KClass<T>): T {
        val instance = clazz.createInstance()
        instance.positions = Mapper.toKey(nextBoundary(generateSize()))
        instance.number = random.nextInt(0, Int.MAX_VALUE)
        instance.description = nextString(random.nextInt(20))
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
        return random.nextDouble(leftX, rightX)
    }

    private fun widthCoordinate(lower: Double, size: GeneratedSize): Double {
        var higher: Double = lower + size.maxSize
        higher = if (DoubleUtils.isALessOrEqualsToB(higher, rightX)) higher else rightX
        return if (higher == lower) {
            higher
        } else random.nextDouble(lower, higher)
    }

    private fun heightCoordinate(): Double {
        return random.nextDouble(bottomY, topY)
    }

    private fun heightCoordinate(higher: Double, size: GeneratedSize): Double {
        var lower: Double = higher - size.maxSize
        lower = if (DoubleUtils.isAGreaterOrEqualsToB(lower, bottomY)) lower else bottomY
        return if (higher == lower) {
            higher
        } else random.nextDouble(lower, higher)
    }

    private fun nextString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
                .map { charset[random.nextInt(charset.length)] }
                .joinToString("")
    }

    private fun setCoordinates(boundary: Boundary?) {
        if (boundary != null) {
            with(boundary) {
                setCoordinates(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1])
            }
        }
    }

    private fun setCoordinates(leftX: Double, topY: Double, rightX: Double, bottomY: Double) {
        this.leftX = leftX
        this.topY = topY
        this.rightX = rightX
        this.bottomY = bottomY
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

/**
 * Enum values to represent operation to invoke in testing.
 * @author David Zimen
 */
enum class GeneratedOperation() {
    DELETE,
    INSERT,
    FIND,
    EDIT
}
