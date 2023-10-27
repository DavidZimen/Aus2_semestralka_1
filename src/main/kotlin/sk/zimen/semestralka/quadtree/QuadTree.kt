package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.quadtree.node.Node

/**
 * Base fot data structure, which represents Quad Tree.
 * For more information visit [Quad Tree](https://en.wikipedia.org/wiki/Quadtree).
 * @param <T> Data type to be used in Quad Tree.
 * Must implement [QuadTreeData] interface to work correctly.
 * @param <K> Key which is used for manipulating the data in structure.
 * Must implement [QuadTreeKey] interface to work correctly.
</K></T> */
abstract class QuadTree<K : QuadTreeKey, T : QuadTreeData<K>> @JvmOverloads constructor(
    topLeftX: Double = -180.0,
    topLeftY: Double = 90.0,
    bottomRightX: Double = 180.0,
    bottomRightY: Double = -90.0
) {
    val root: Node<K, T> by lazy { createRoot(topLeftX, topLeftY, bottomRightX, bottomRightY) }
    var maxAllowedDepth = 200
        protected set
    var size = 0
        protected set

    constructor(maxDepth: Int) : this(-180.0, 90.0, 180.0, -90.0) {
        maxAllowedDepth = maxDepth
    }

    /**
     * Creates a root node specific for implementation of [QuadTree].
     * @param topLeftX Top x coordinate of tree boundary.
     * @param topLeftY Top y coordinate of tree boundary.
     * @param bottomRightX Bottom x coordinate of tree boundary.
     * @param bottomRightY Bottom y coordinate of tree boundary.
     */
    protected abstract fun createRoot(topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double): Node<K, T>

    val currentDepth: Int
        get() {
            var currentDepth = 0
            val it: NodeIterator<K, T> = this.root.iterator()
            while (it.hasNext()) {
                val node: Node<K, T> = it.next()
                if (node.level > currentDepth) currentDepth = node.level
                if (currentDepth == maxAllowedDepth) break
            }
            return currentDepth
        }

    // Functions and functional attributes
    /**
     * R
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * Inserts provided data to the correct position in the [QuadTree].
     * @param data Data to be inserted.
     */
    fun insert(data: T) {
        // check if it is in boundary of QuadTree
        val p: Position = root.getPosition(data)
        if (size == 0) {
            root.simpleInsert(data, p)
        } else {
            root.findMostEligibleNode(data).insert(data, maxAllowedDepth)
        }
        size++
    }

    /**
     * Find all data that where keys are intersecting with provided key
     * @param key Provided key
     */
    fun find(key: K): List<T> = root.find(key)

    /**
     * Removes item from [QuadTree].
     * @param item Data to be removed.
     */
    fun delete(item: T) {
        root.findMostEligibleNode(item).delete(item, maxAllowedDepth)
        size--
    }

    /**
     * Check if [QuadTree] contains provided item.
     * @param item Provided item.
     */
    operator fun contains(item: T): Boolean {
        var dataIterator: Iterator<T>
        val nodeIterator: Iterator<Node<K, T>> = root.iterator()
        var node: Node<K, T>
        while (nodeIterator.hasNext()) {
            node = nodeIterator.next()
            dataIterator = node.dataIterator()
            while (dataIterator.hasNext()) {
                if (dataIterator.next() == item) {
                    return true
                }
            }
        }
        return false
    }

    fun printTree() { }
}
