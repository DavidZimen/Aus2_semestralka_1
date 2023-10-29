package sk.zimen.semestralka.quadtree

import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.time.StopWatch
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.NodeIterator
import sk.zimen.semestralka.quadtree.node.AdvancedNode
import sk.zimen.semestralka.quadtree.node.Node
import sk.zimen.semestralka.utils.GeneratedSize
import sk.zimen.semestralka.utils.Generator
import sk.zimen.semestralka.utils.Mapper
import java.util.*


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class QuadTreeTest {

    private val classicTree: ClassicQuadTree<PlaceKey, Place> = ClassicQuadTree(20)
    private val advancedTree: AdvancedQuadTree<PlaceKey, Place> = AdvancedQuadTree(20)
    private val itemsToRemove: Stack<Place> = Stack<Place>()

    @BeforeEach
    fun setUp() {
        val count = 10_000
        val generator = Generator()
        val items: MutableList<Place> = ArrayList<Place>(count)
        var positions: Array<GpsPosition>
        var size: GeneratedSize
        for (i in 0 until count) {
            size = generator.generateSize()
            positions = generator.nextPositions(size)
            val place = Place(generator.random.nextInt(), positions[0], positions[1])
            items.add(place)
            if (i % 25 == 0) {
                itemsToRemove.push(place)
            }
        }
        val avgClassic = insertDataToTree(classicTree, items).toDouble() / items.size
        val avgAdvanced = insertDataToTree(advancedTree, items).toDouble() / items.size
        println("Insert in classic. Average time: " + avgClassic + ", Depth: " + classicTree.currentDepth)
        println("Insert in advanced. Average time: " + avgAdvanced + ", Depth: " + advancedTree.currentDepth)
    }

    @Test
    @Order(1)
    fun testInsert() {
        testInsert(classicTree)
        testInsert(advancedTree)
    }

    @Test
    @Order(2)
    fun testFind() {
        testFind(classicTree)
        testFind(advancedTree)
    }

    @Test
    @Order(3)
    fun testDelete() {
        var item: Place
        val advanced = StopWatch()
        advanced.start()
        advanced.suspend()
        val classic = StopWatch()
        classic.start()
        classic.suspend()
        val numberOfDeletes = itemsToRemove.size.toDouble()
        while (!itemsToRemove.isEmpty()) {
            item = itemsToRemove.pop()
            try {
                classic.resume()
                classicTree.delete(item)
                classic.suspend()
                advanced.resume()
                advancedTree.delete(item)
                advanced.suspend()
                this.testInsert(classicTree)
                this.testInsert(advancedTree)
                assertFalse(classicTree.contains(item))
                assertFalse(advancedTree.contains(item))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        classic.stop()
        advanced.stop()
        println("Average delete time of CLASSIC: " + classic.time.toDouble() / numberOfDeletes)
        println("Average delete time of ADVANCED: " + advanced.time.toDouble() / numberOfDeletes)
    }

    @Order(4)
    @Test
    fun testChangeHeight() {
        classicTree.changeHeight(20)
        advancedTree.changeHeight(20)
        testInsert(classicTree)
        testFind(advancedTree)
        println("New max height of CLASSIC: " + classicTree.maxAllowedDepth + ", current depth: " + classicTree.currentDepth)
        println("New max height of ADVANCED: " + advancedTree.maxAllowedDepth + ", current depth: " + advancedTree.currentDepth)
        advancedTree.balanceFactor()
    }

    private fun testInsert(tree: QuadTree<PlaceKey, Place>) {
        val iterator: Iterator<Node<PlaceKey, Place>> = tree.root.iterator()
        var node: Node<PlaceKey, Place>
        var dataIterator: Iterator<Place?>
        var filteredItems: List<Place?>
        var data: Place?
        val treeDepth: Int = tree.currentDepth
        var size = 0
        while (iterator.hasNext()) {
            node = iterator.next()
            assertTrue(node.level <= tree.maxAllowedDepth)
            if (node.level == tree.maxAllowedDepth || node.level == treeDepth) {
                assertNull(node.topLeft)
                assertNull(node.topRight)
                assertNull(node.bottomLeft)
                assertNull(node.bottomRight)
            } else if (!node.isLeaf || node.level == treeDepth) {
                val streamNode: Node<PlaceKey, Place> = node
                filteredItems = streamNode.dataList.filter { item -> streamNode.getPosition(item) != Position.CURRENT }
                assertTrue(filteredItems.isEmpty())
            }
            if (node is AdvancedNode<PlaceKey, Place>) {
                if (!node.isLeaf) {
                    assertTrue(node.dataList.isEmpty())
                }
            }
            dataIterator = node.dataIterator()
            while (dataIterator.hasNext()) {
                data = dataIterator.next()
                if (node.level != tree.maxAllowedDepth && !node.isLeaf) {
                    assertEquals(Position.CURRENT, node.getPosition(data))
                }
                size++
            }
        }
        assertEquals(tree.size, size)
    }

    private fun testFind(tree: QuadTree<PlaceKey, Place>) {
        val generator = Generator()
        var positions: Array<GpsPosition>
        var placeKey: PlaceKey
        var foundItems: List<Place?>
        var foundItemsSame: List<Place?>?
        var foundItemsInAll: List<Place?>
        for (i in 0..99) {
            positions = generator.nextPositions(generator.generateSize())
            placeKey = Mapper.toKey(positions[0], positions[1])
            try {
                foundItems = tree.find(placeKey)
                foundItemsSame = tree.find(placeKey)
            } catch (e: Exception) {
                foundItems = emptyList<Place>()
                foundItemsSame = emptyList<Place>()
            }
            foundItemsInAll = findInWholeTree(tree, placeKey)
            assertEquals(foundItemsInAll.size, foundItems.size)
            assertEquals(foundItems, foundItemsSame) // order does matter
            assertTrue(CollectionUtils.isEqualCollection(foundItems, foundItemsInAll)) //order does not matter
        }
    }

    private fun findInWholeTree(tree: QuadTree<PlaceKey, Place>, key: PlaceKey): List<Place?> {
        val nodeIterator: NodeIterator<PlaceKey, Place> = tree.root.iterator()
        val boundary: Boundary = key.toBoundary()
        val foundItems: MutableList<Place?> = ArrayList<Place?>()
        var node: Node<PlaceKey, Place>
        var dataIterator: Iterator<Place>
        var data: Place
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

    private fun insertDataToTree(tree: QuadTree<PlaceKey, Place>, items: List<Place>): Long {
        val watch = StopWatch()
        watch.start()
        for (item in items) {
            tree.insert(item)
        }
        watch.stop()
        return watch.time
    }
}