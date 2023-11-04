package sk.zimen.semestralka.quadtree.utils

import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.time.StopWatch
import org.junit.jupiter.api.Assertions
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.node.AdvancedNode
import sk.zimen.semestralka.quadtree.node.Node
import sk.zimen.semestralka.utils.Generator
import sk.zimen.semestralka.utils.Mapper

fun <T : QuadTreeData<PlaceKey>> testFind(tree: QuadTree<PlaceKey, T>) {
    val generator = Generator(180.0, 90.0)
    var positions: Array<GpsPosition>
    var placeKey: PlaceKey
    var foundItems: List<T>
    var foundItemsSame: List<T>?
    var foundItemsInAll: List<T>

    for (i in 0..99) {
        positions = generator.nextPositions(generator.generateSize())
        placeKey = Mapper.toKey(positions[0], positions[1])
        try {
            foundItems = tree.find(placeKey)
            foundItemsSame = tree.find(placeKey)
        } catch (e: Exception) {
            foundItems = emptyList()
            foundItemsSame = emptyList()
        }
        foundItemsInAll = findInWholeTree(tree, placeKey)
        Assertions.assertEquals(foundItemsInAll.size, foundItems.size)
        Assertions.assertEquals(foundItems, foundItemsSame) // order does matter
        Assertions.assertTrue(CollectionUtils.isEqualCollection(foundItems, foundItemsInAll)) //order does not matter
    }
}

fun <T : QuadTreeData<PlaceKey>> testDelete(tree: QuadTree<PlaceKey, T>, deletedItem: T) {
    testInsert(tree)
    Assertions.assertFalse(tree.contains(deletedItem))
}

fun <T : QuadTreeData<PlaceKey>> testInsert(tree: QuadTree<PlaceKey, T>, item: T? = null) {
    val iterator: Iterator<Node<PlaceKey, T>> = tree.root.iterator()
    var node: Node<PlaceKey, T>
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
            val streamNode: Node<PlaceKey, T> = node
            filteredItems = streamNode.dataList.filter { streamNode.getPosition(it) != Position.CURRENT }
            Assertions.assertTrue(filteredItems.isEmpty())
        }
        if (node is AdvancedNode<PlaceKey, T>) {
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

fun <T : QuadTreeData<PlaceKey>> testEdit(tree: QuadTree<PlaceKey, T>, old: T, new: T) {
    Assertions.assertTrue(tree.contains(new))
    Assertions.assertFalse(tree.contains(old))
    testInsert(tree)
}

private fun <T : QuadTreeData<PlaceKey>> findInWholeTree(tree: QuadTree<PlaceKey, T>, key: PlaceKey): List<T> {
    val nodeIterator: NodeIterator<PlaceKey, T> = tree.root.iterator()
    val boundary: Boundary = key.toBoundary()
    val foundItems: MutableList<T> = ArrayList<T>()
    var node: Node<PlaceKey, T>
    var dataIterator: Iterator<T>
    var data: T
    while (nodeIterator.hasNext()) {
        node = nodeIterator.next()
        dataIterator = node.dataIterator()
        while (dataIterator.hasNext()) {
            data = dataIterator.next()
            if (data.key.toBoundary().intersects(boundary)) {
                foundItems.add(data)
            }
        }
    }
    return foundItems
}

fun <T : QuadTreeData<PlaceKey>> insertDataToTree(tree: QuadTree<PlaceKey, T>, items: List<T>): Long {
    val watch = StopWatch()
    watch.start()
    for (item in items) {
        tree.insert(item)
    }
    watch.stop()
    return watch.time
}