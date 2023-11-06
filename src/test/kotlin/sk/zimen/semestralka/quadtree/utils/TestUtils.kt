package sk.zimen.semestralka.quadtree.utils

import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.time.StopWatch
import org.junit.jupiter.api.Assertions
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.node.AdvancedNode
import sk.zimen.semestralka.quadtree.node.Node
import sk.zimen.semestralka.utils.Generator

fun <T : QuadTreeData> testFind(tree: QuadTree<T>, count: Int = 50) {
    val generator = Generator(180.0, 90.0)
    var boundary: Boundary
    var foundItems: List<T>
    var foundItemsSame: List<T>?
    var foundItemsInAll: List<T>

    for (i in 0 until count) {
        boundary = generator.generateBoundaries(1)[0]
        try {
            foundItems = tree.find(boundary)
            foundItemsSame = tree.find(boundary)
        } catch (e: Exception) {
            foundItems = emptyList()
            foundItemsSame = emptyList()
        }
        foundItemsInAll = findInWholeTree(tree, boundary)
        Assertions.assertEquals(foundItemsInAll.size, foundItems.size)
        Assertions.assertEquals(foundItems, foundItemsSame) // order does matter
        Assertions.assertTrue(CollectionUtils.isEqualCollection(foundItems, foundItemsInAll)) //order does not matter
    }
}

fun <T : QuadTreeData> testDelete(tree: QuadTree<T>, deletedItem: T) {
    testInsert(tree)
    Assertions.assertFalse(tree.contains(deletedItem))
}

fun <T : QuadTreeData> testInsert(tree: QuadTree<T>, item: T? = null) {
    val iterator: Iterator<Node<T>> = tree.root.iterator()
    var node: Node<T>
    var dataIterator: Iterator<T>
    var filteredItems: List<T>
    var data: T
    val treeDepth: Int = tree.currentDepth
    var size = 0
    while (iterator.hasNext()) {
        node = iterator.next()
        Assertions.assertTrue(node.level <= tree.maxAllowedDepth)
        if (node.level == tree.maxAllowedDepth || node.level == treeDepth) {
            Assertions.assertNull(node.topLeft)
            Assertions.assertNull(node.topRight)
            Assertions.assertNull(node.bottomLeft)
            Assertions.assertNull(node.bottomRight)
        } else if (!node.isLeaf || node.level == treeDepth) {
            val streamNode: Node<T> = node
            filteredItems = streamNode.dataList.filter { streamNode.getPosition(it) != Position.CURRENT }
            Assertions.assertTrue(filteredItems.isEmpty())
        }
        if (node is AdvancedNode<T>) {
            if (!node.isLeaf) {
                Assertions.assertTrue(node.dataList.isEmpty())
            }
        }
        dataIterator = node.dataIterator()
        while (dataIterator.hasNext()) {
            data = dataIterator.next()
            if (node.level != tree.maxAllowedDepth && !node.isLeaf) {
                Assertions.assertEquals(Position.CURRENT, node.getPosition(data))
            }
            size++
        }
    }
    Assertions.assertEquals(tree.size, size)
    if (item != null) {
        Assertions.assertTrue(tree.contains(item))
    }
}

fun <T : QuadTreeData> testEdit(tree: QuadTree<T>, old: T, new: T) {
    Assertions.assertTrue(tree.contains(new))
    Assertions.assertFalse(tree.contains(old))
    testInsert(tree)
}

private fun <T : QuadTreeData> findInWholeTree(tree: QuadTree<T>, boundary: Boundary): List<T> {
    val nodeIterator: NodeIterator<T> = tree.root.iterator()
    val foundItems: MutableList<T> = ArrayList()
    var node: Node<T>
    var dataIterator: Iterator<T>
    var data: T
    while (nodeIterator.hasNext()) {
        node = nodeIterator.next()
        dataIterator = node.dataIterator()
        while (dataIterator.hasNext()) {
            data = dataIterator.next()
            if (data.getBoundary().intersects(boundary)) {
                foundItems.add(data)
            }
        }
    }
    return foundItems
}

fun <T : QuadTreeData> insertDataToTree(tree: QuadTree<T>, items: List<T>): Long {
    val watch = StopWatch()
    watch.start()
    for (item in items) {
        tree.insert(item)
    }
    watch.stop()
    return watch.time
}

/**
 * Prepares [Generator] for provided [QuadTree] based on its [Boundary].
 * When [badHealth] is set to true, it shifts generator boundaries so the tree
 * will have poor [QuadTree.health] statistic.
 */
fun <T : QuadTreeData> prepareGenerator(tree: QuadTree<T>, badHealth: Boolean = false): Generator {
    val generator = Generator()
    var leftXShift = 0
    var rightXShift = 0
    var topYShift = 0
    var bottomYShift = 0

    if (badHealth) {
        leftXShift = generator.random.nextInt(0, 20)
        rightXShift = generator.random.nextInt(0, 20)
        topYShift = generator.random.nextInt(0, 20)
        bottomYShift = generator.random.nextInt(0, 20)
    }

    with(tree.root) {
        generator.apply {
            with(createNewNode(Position.TOP_LEFT).boundary) {
                leftX = topLeft[0] + leftXShift
                topY = topLeft[1] - topYShift
            }
            with(createNewNode(Position.TOP_RIGHT).boundary) {
                rightX = bottomRight[0] - rightXShift
                bottomY = bottomRight[1] + bottomYShift
            }
        }
    }

    return generator
}
