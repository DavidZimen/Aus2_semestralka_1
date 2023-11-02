package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.quadtree.node.Node

//TODO writing and reading from file
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
    var root: Node<K, T> = createRoot(topLeftX, topLeftY, bottomRightX, bottomRightY)
        protected set
    var maxAllowedDepth = 10
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

    // Functions and functional attributes
    /**
     * Functional attribute to get current depth of quadtree.
     */
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

    /**
     * Functional attribute to find out if quadtree is empty.
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
     * Finds old item in quadtree.
     * - If old item has same key as new, just replace them.
     * - If keys are different, then delete old and insert new.
     * @return Reference to new version of item.
     */
    fun edit(old: T, new: T): T {
        val node = root.findMostEligibleNode(old)
        if (old.key == new.key) {
            var oldItem = node.findSingleItem(old)
            oldItem = new
        } else {
            node.delete(old, maxAllowedDepth)
            root.findMostEligibleNode(new).insert(new, maxAllowedDepth)
        }

        return new
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

    //TODO finish tree optimization function
    fun balanceFactor() {
        println("Nodes is top left: " + (root.topLeft?.nodeBalance() ?: 0))
        println("Nodes is bottom left: " + (root.bottomLeft?.nodeBalance() ?: 0))
        println("Nodes is top right: " + (root.topRight?.nodeBalance() ?: 0))
        println("Nodes is bottom right: " + (root.bottomRight?.nodeBalance() ?: 0))
    }

    /**
     * Function to change the current height of the [QuadTree].
     * Takes all data of the current tree and inserts it in new with provided height.
     * Deletes old Quadtree.
     */
    @Throws(IllegalArgumentException::class)
    fun changeHeight(newHeight: Int) {
        if (newHeight < 1) throw IllegalArgumentException("Height of quadtree can not be less than 1.")

        val iterator = root.iterator()
        var dataIterator: MutableIterator<T>
        var node: Node<K, T>

        with(root.boundary) {
            root = createRoot(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1])
            maxAllowedDepth = newHeight
            size = 0
        }

        while (iterator.hasNext()) {
            node = iterator.next()
            dataIterator = node.dataIterator()
            while (dataIterator.hasNext()) {
                insert(dataIterator.next())
                dataIterator.remove()
            }
            node.removeNode()
        }
    }

    fun printTree() { }
}
