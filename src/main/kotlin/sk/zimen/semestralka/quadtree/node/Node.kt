package sk.zimen.semestralka.quadtree.node

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.exceptions.BoundaryException
import sk.zimen.semestralka.quadtree.exceptions.MultipleResultsFoundException
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.quadtree.exceptions.PositionException
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.quadtree.metrics.NodeMetrics
import java.util.*

/**
 * Represents a base abstract class for node with data in QuadTree.
 * @author David Zimen
 */
abstract class Node<K : QuadTreeKey, T : QuadTreeData<K>> {

    val dataList: MutableList<T> = ArrayList()
    val level: Int
    var parent: Node<K, T>? = null
    var topLeft: Node<K, T>? = null
    var bottomLeft: Node<K, T>? = null
    var topRight: Node<K, T>? = null
    var bottomRight: Node<K, T>? = null
    var boundary: Boundary

    constructor(level: Int, boundary: Boundary) {
        this.level = level
        this.boundary = boundary
    }

    constructor(level: Int, parent: Node<K, T>?, boundary: Boundary) {
        this.level = level
        this.boundary = boundary
        this.parent = parent
    }

    // ABSTRACT function and functional attributes
    /**
     * Returns boolean flag, if current node has no elements.
     */
    abstract val isEmpty: Boolean

    /**
     * @return Number of elements in node.
     */
    abstract val size: Int

    /**
     * Function to edit data in current node.
     * @param old Version of item before editing.
     * @param new Version to be saved.
     */
    abstract fun edit(old: T, new: T): T

    /**
     * Function to only add data the list in node.
     * @param item Provided data.
     */
    abstract fun simpleInsert(item: T, p: Position): Boolean

    /**
     * Iterator with every item in [Node].
     */
    abstract fun dataIterator(): MutableIterator<T>

    /**
     * Creates new [ClassicNode] with boundary corresponding to provided [Position].
     * @param p Position where to create [Boundary].
     */
    abstract fun createNewNode(p: Position): Node<K, T>

    /**
     * Used to remove data when there is only one item left in node.
     */
    protected abstract fun removeSingleItem(): T

    /**
     * @param maxDepth Maximum depth of QuadTree where node is located.
     * @return Information whether node can be further divided.
     */
    protected abstract fun canBeDivided(maxDepth: Int): Boolean

    /**
     * @return Number of items in node which can go into lower nodes if allowed.
     */
    protected abstract fun divisibleItems(): List<T>

    // Functions and functional attributes
    /**
     * Function to provide information, whether [ClassicNode] has is leaf -> has no children.
     */
    val isLeaf: Boolean
        get() = topLeft == null
                && bottomLeft == null
                && topRight == null
                && bottomRight == null

    /**
     * Returns boolean value, whether current node is root of the QuadTree
     */
    val isRoot: Boolean
        get() = parent == null

    /**
     * Method to insert data in [ClassicNode]
     * @param item Generic data to be inserted.
     */
    fun insert(item: T, maxDepth: Int): Boolean {
        val boundary: Boundary = item.key.toBoundary()

        // if no data and not divided yet or node level is maximum
        if ((isEmpty && isLeaf) || level == maxDepth) {
            return simpleInsert(item, this.getPosition(boundary))
        }

        // insert item on required position
        var position: Position = this.getPosition(boundary)
        var node: Node<K, T> = getOrCreateNodeOnPosition(position)
        val res = node.simpleInsert(item, node.getPosition(boundary))

        // check all data and rearrange
        val nodeIterator: NodeIterator<K, T> = this.iterator()
        if (this !== node) {
            nodeIterator.addToIteration(node)
        }
        // loop through children nodes
        while (nodeIterator.hasNext()) {
            node = nodeIterator.nextWithoutChildren()
            if (node.canBeDivided(maxDepth)) {
                val dataIterator: MutableIterator<T> = node.dataList.iterator()

                // for each data try to put it in correct position
                while (dataIterator.hasNext()) {
                    val listData: T = dataIterator.next()
                    position = node.getPosition(listData.key.toBoundary())

                    //if position is not current -> insert on position
                    if (position !== Position.CURRENT) {
                        val newPosNode: Node<K, T> = node.getOrCreateNodeOnPosition(position)
                        newPosNode.simpleInsert(listData, newPosNode.getPosition(listData))
                        dataIterator.remove()

                        // add to iteration, if new node of data can be divided
                        if (newPosNode.canBeDivided(maxDepth)) {
                            nodeIterator.addToIteration(newPosNode)
                        }
                    }
                }
            }
        }

        return res
    }

    /**
     * Method to find all items which intersects with given key.
     * @param key Provided key.
     */
    @Throws(NoResultFoundException::class)
    fun find(key: K): List<T> {
        val foundData: MutableList<T> = mutableListOf()
        val boundary: Boundary = key.toBoundary()

        // check child notes and their data
        for (node in this) {
            for (item in node.dataIterator()) {
                if (item.key.toBoundary().intersects(boundary)) {
                    foundData.add(item)
                }
            }
        }

        if (foundData.isNotEmpty()) {
            return foundData
        } else {
            throw NoResultFoundException()
        }
    }

    /**
     * Deletes provided item from [Node].
     * @param item Item to be deleted.
     * @throws NoResultFoundException When no items were deleted.
     * @throws MultipleResultsFoundException When there are more exactly same items.
     */
    @Throws(NoResultFoundException::class, MultipleResultsFoundException::class)
    fun delete(item: T, maxDepth: Int) {
        var data: T
        val removedData: MutableList<T> = ArrayList(1)
        val dataIterator: MutableIterator<T> = dataIterator()

        // loop over all data in node
        while (dataIterator.hasNext()) {
            data = dataIterator.next()
            if (data == item) {
                removedData.add(data)
                dataIterator.remove()
            }
        }

        // check correctness
        if (removedData.isEmpty()) {
            throw NoResultFoundException("No items match provided item for deletion.")
        } else if (removedData.size > 1) {
            dataList.addAll(removedData)
            throw MultipleResultsFoundException("Two items are of the same specification")
        }

        // rearrange nodes if necessary
        val childCount: Int = childrenCount()
        val onlyChild: Node<K, T>? = oneNotNullChild()
        if (isEmpty) {
            if (childCount == 0) {
                removeNode()
            } else if (childCount == 1 && onlyChild != null) {
                if (onlyChild.childrenCount() == 0) {
                    if (onlyChild.isEmpty) {
                        onlyChild.removeNode()
                        removeNode()
                    } else if (onlyChild.size == 1) {
                        val reinsertItem: T = onlyChild.removeSingleItem()
                        insert(reinsertItem, maxDepth)
                        onlyChild.removeNode()
                    }
                }
            }
        }
        //TODO also add something to collapse nodes to root
    }

    /**
     * Return a node on provided position.
     * Can't return a null value.
     * @param p Position where to find node.
     */
    fun getNodeOnPosition(p: Position): Node<K, T> = getNodeOnPositionOrNull(p)!!

    /**
     * Return a node on provided position.
     * Can return a null value.
     * @param p Position where to find node.
     */
    fun getNodeOnPositionOrNull(p: Position): Node<K, T>? {
        return when (p) {
            Position.CURRENT -> this
            Position.TOP_LEFT -> topLeft
            Position.BOTTOM_LEFT -> bottomLeft
            Position.TOP_RIGHT -> topRight
            Position.BOTTOM_RIGHT -> bottomRight
        }
    }

    /**
     * Finds existing node, that is the most suitable for provided data.
     * @param item Provided data
     * @return Reference to [ClassicNode]
     */
    fun findMostEligibleNode(item: T): Node<K, T> {
        val boundary: Boundary = item.key.toBoundary()
        return this.findMostEligibleNode(boundary)
    }

    /**
     * Finds existing node, that is the most suitable for provided boundary.
     * @param b Provided boundary.
     * @return Reference to [ClassicNode]
     */
    fun findMostEligibleNode(b: Boundary): Node<K, T> {
        var position: Position = this.getPosition(b)
        var existsNode: Boolean = existsOnPosition(position)
        var node: Node<K, T> = if (existsNode) getNodeOnPosition(position) else this

        while (existsNode) {
            position = node.getPosition(b)
            existsNode = node.existsOnPosition(position)
            node = if (existsNode) node.getNodeOnPosition(position) else node
        }
        return node
    }

    /**
     * Removes node from the tree.
     */
    fun removeNode() {
        parent?.let {
            if (topLeft === this) {
                topLeft = null
            } else if (bottomLeft === this) {
                bottomLeft = null
            } else if (topRight === this) {
                topRight = null
            } else if (bottomRight === this) {
                bottomRight = null
            }
        }

        parent = null
        topLeft = null
        bottomLeft = null
        topRight = null
        bottomRight = null
    }

    /**
     * @param data Provided data.
     * @throws IllegalArgumentException When no data was provided.
     * @return Position where the data belongs in the context of current [ClassicNode].
     */
    @Throws(IllegalArgumentException::class)
    fun getPosition(data: T): Position = this.getPosition(data.key.toBoundary())

    /**
     * @param b Provided boundary.
     * @throws IllegalArgumentException When no boundary was provided.
     * @return Position where the boundary belongs in the context of current [ClassicNode].
     */
    protected fun getPosition(b: Boundary): Position {
        // initialize all boundaries
        val topLeftBoundary: Boundary = topLeft?.boundary ?: Boundary.createBoundaryOnPosition(Position.TOP_LEFT, boundary)
        val bottomLeftBoundary: Boundary = bottomLeft?.boundary ?: Boundary.createBoundaryOnPosition(Position.BOTTOM_LEFT, boundary)
        val topRightBoundary: Boundary = topRight?.boundary ?: Boundary.createBoundaryOnPosition(Position.TOP_RIGHT, boundary)
        val bottomRightBoundary: Boundary = bottomRight?.boundary ?: Boundary.createBoundaryOnPosition(Position.BOTTOM_RIGHT, boundary)

        // check where boundary fits
        return if (topLeftBoundary.contains(b)) {
            Position.TOP_LEFT
        } else if (bottomLeftBoundary.contains(b)) {
            Position.BOTTOM_LEFT
        } else if (topRightBoundary.contains(b)) {
            Position.TOP_RIGHT
        } else if (bottomRightBoundary.contains(b)) {
            Position.BOTTOM_RIGHT
        } else if (boundary.contains(b)) {
            Position.CURRENT
        } else {
            throw BoundaryException("Boundary can't fit into node.")
        }
    }

    /**
     * @param p Provided position where to get or create a new [ClassicNode].
     * @return Node on a position.
     * @throws PositionException When no position was provided.
     */
    @Throws(PositionException::class)
    protected fun getOrCreateNodeOnPosition(p: Position?): Node<K, T> {
        return when (p) {
            Position.CURRENT -> this
            Position.TOP_LEFT -> topLeft ?: createNewNode(p).also { topLeft = it }
            Position.BOTTOM_LEFT -> bottomLeft ?: createNewNode(p).also { bottomLeft = it }
            Position.TOP_RIGHT -> topRight ?: createNewNode(p).also { topRight = it }
            Position.BOTTOM_RIGHT -> bottomRight ?: createNewNode(p).also { bottomRight = it }
            else -> throw PositionException("No position provided.")
        }
    }

    /**
     * Checks whether [ClassicNode] exists on provided position.
     * When position is CURRENT, then return false to not enter into while loop.
     * Use only in findMostEligibleNode method !!!
     */
    protected fun existsOnPosition(p: Position): Boolean {
        return when (p) {
            Position.TOP_LEFT -> topLeft != null
            Position.BOTTOM_LEFT -> bottomLeft != null
            Position.TOP_RIGHT -> topRight != null
            Position.BOTTOM_RIGHT -> bottomRight != null
            Position.CURRENT -> false
        }
    }

    /**
     * Returns child [Node], if there is only one node.
     * If there are more than 1 or 0, then returns null.
     */
    protected fun oneNotNullChild(): Node<K, T>? {
        val count: Int = childrenCount()
        if (count == 0 || count > 1) return null

        return if (topLeft != null) {
            topLeft
        } else if (bottomLeft != null) {
            bottomLeft
        } else if (topRight != null) {
            topRight
        } else if (bottomRight != null) {
            bottomRight
        } else {
            null
        }
    }

    /**
     * @return Number of not-null children for [Node].
     */
    protected fun childrenCount(): Int = listOf(topLeft, bottomLeft, topRight, bottomRight).count { it != null }

    //FUNCTIONS FOR METRICS

    /**
     * Calculates [NodeMetrics] for current node.
     */
    fun metrics(): NodeMetrics {
        var depth = 0
        var nodesCount = 0
        var dataCount = 0
        var divisibleItemCount = 0
        var potentialDepth = level
        var leftX = 0.0
        var rightX = 0.0
        var topY = 0.0
        var bottomY = 0.0

        val iterator = iterator()
        while (iterator.hasNext()) {
            val node = iterator.next()
            dataCount += node.size
            nodesCount++
            if (node.level > depth) depth = node.level
            if (depth > potentialDepth) potentialDepth = depth
            if (node.isLeaf) {
                val divisibleItems = node.divisibleItems()
                divisibleItemCount += divisibleItems.size
                divisibleItems.forEach {
                    val potential = node.getPotentialDepth(it)
                    if (potential > potentialDepth) potentialDepth = potential
                }
            }
            for (item in node.dataIterator()) {
                with(item.key.toBoundary()) {
                    if (topLeft[0] < leftX) leftX = topLeft[0]
                    if (topLeft[1] > topY) topY = topLeft[1]
                    if (bottomRight[0] > rightX) rightX = bottomRight[0]
                    if (bottomRight[1] < bottomY) bottomY = bottomRight[1]
                }
            }
        }

        return NodeMetrics(dataCount, divisibleItemCount, nodesCount, depth, leftX, rightX, topY, bottomY, potentialDepth)
    }

    /**
     * Calculates the depth that can be reached from current node.
     * Returns maximal level from search.
     * If needed for number of levels between current node and deepest
     * just calculate result - node.level
     */
    fun depth(): Int {
        var currentDepth = 0
        val it = iterator()
        while (it.hasNext()) {
            val node = it.next()
            if (node.level > currentDepth) currentDepth = node.level
        }
        return currentDepth
    }

    /**
     * For provided item it returns depth to which it can go.
     */
    private fun getPotentialDepth(item: T): Int {
        var depth = level
        val position = getPosition(item)

        if (position == Position.CURRENT) return depth

        var node = createNewNode(position)
        while (node.getPosition(item) != Position.CURRENT) {
            node = node.createNewNode(node.getPosition(item))
            depth = node.level
        }
        return depth
    }

    // ITERATOR CLASS AND FUNCTIONS
    /**
     * @return [QuadTreeNodeIterator] instance for current [ClassicNode].
     */
    operator fun iterator(): NodeIterator<K, T> {
        return QuadTreeNodeIterator()
    }

    /**
     * Iterator implementation for [ClassicNode].
     * Iterates in order TOP_LEFT -> BOTTOM_LEFT -> TOP_RIGHT -> BOTTOM_RIGHT.
     * @author David Zimen
     */
    private inner class QuadTreeNodeIterator : NodeIterator<K, T> {
        /**
         * Stack of the nodes for traversal.
         */
        private val stack: Stack<Node<K, T>> = Stack()

        init {
            stack.push(this@Node)
        }

        override operator fun hasNext(): Boolean {
            return !stack.isEmpty()
        }

        override operator fun next(): Node<K, T> {
            if (!hasNext()) {
                throw NoSuchElementException("No more items in NodeIterator.")
            }
            val current: Node<K, T>? = stack.pop()

            // Push all not null children into stack
            if (current!!.topLeft != null) {
                addToIteration(current.topLeft!!)
            }
            if (current.bottomLeft != null) {
                addToIteration(current.bottomLeft!!)
            }
            if (current.topRight != null) {
                addToIteration(current.topRight!!)
            }
            if (current.bottomRight != null) {
                addToIteration(current.bottomRight!!)
            }
            return current
        }

        override fun nextWithoutChildren(): Node<K, T> {
            if (!hasNext()) {
                throw NoSuchElementException("No more items in NodeIterator.")
            }
            return stack.pop()
        }

        override fun addToIteration(quadtreeNode: Node<K, T>) {
            stack.push(quadtreeNode)
        }

        /**
         * @throws UnsupportedOperationException Operation not implemented !!!
         */
        @Deprecated("DON'T USE THIS METHOD")
        override fun remove() { }
    }
}
