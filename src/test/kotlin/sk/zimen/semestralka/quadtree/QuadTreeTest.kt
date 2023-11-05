package sk.zimen.semestralka.quadtree

import org.apache.commons.lang3.time.StopWatch
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.WidthPos
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.utils.insertDataToTree
import sk.zimen.semestralka.quadtree.utils.testDelete
import sk.zimen.semestralka.quadtree.utils.testFind
import sk.zimen.semestralka.quadtree.utils.testInsert
import sk.zimen.semestralka.utils.Generator
import java.util.*


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class QuadTreeTest {

    private var classicTree: ClassicQuadTree<Place> = ClassicQuadTree(10)
    private var advancedTree: AdvancedQuadTree<Place> = AdvancedQuadTree(10)
    private var itemsToRemove: Stack<Place> = Stack<Place>()
    private val generator = Generator()

    @BeforeEach
    fun setUp() {
        val count = 10_000
        val items: List<Place> = generator.generateItems(Place::class, count, classicTree.root.boundary)
        items.forEachIndexed { index, place -> if (index % 20 == 0) itemsToRemove.add(place) }

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
                testDelete(classicTree, item)
                testDelete(advancedTree, item)
            } catch (_: Exception) { }
        }
        classic.stop()
        advanced.stop()
        println("Average delete time of CLASSIC: " + classic.time.toDouble() / numberOfDeletes)
        println("Average delete time of ADVANCED: " + advanced.time.toDouble() / numberOfDeletes)

        // check if upstream merge is correct
        val items = listOf(
            Place(
                1,
                "Some random desc",
                GpsPosition(20.0, WidthPos.Z, 20.0, HeightPos.S),
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S)
            ),
            Place(
                2,
                "Another desc",
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S),
                GpsPosition(14.0, WidthPos.Z, 14.0, HeightPos.S)
            )
        )
        advancedTree = AdvancedQuadTree(10)
        advancedTree.insert(items[0])
        advancedTree.insert(items[1])
        advancedTree.delete(items[0])
        assertEquals(1, advancedTree.root.size)
        assertEquals(0, advancedTree.root.childrenCount())
    }

    @Order(4)
    @Test
    fun testChangeHeight() {
        val classic = StopWatch()
        classic.start()
        classicTree.changeHeight(20)
        classic.stop()
        val advanced = StopWatch()
        advanced.start()
        advancedTree.changeHeight(20)
        advanced.stop()
        testInsert(classicTree)
        testFind(advancedTree)
        println("New max height of CLASSIC: " + classicTree.maxAllowedDepth + ", current depth: " + classicTree.currentDepth + ", Time: " + classic.time)
        println("New max height of ADVANCED: " + advancedTree.maxAllowedDepth + ", current depth: " + advancedTree.currentDepth + ", Time: " + advanced.time)
    }

    @Order(5)
    @Test
    fun testMetrics() {
        val items = with(advancedTree.root) {
            Generator().apply {
                with(getNodeOnPosition(Position.TOP_LEFT).boundary) {
                    leftX = topLeft[0]
                    topY = topLeft[1]
                }
                with(getNodeOnPosition(Position.TOP_RIGHT).boundary) {
                    rightX = bottomRight[0]
                    bottomY = bottomRight[1] - 10
                }
            }
        }.generateItems(Place::class, 10_000)
        advancedTree = AdvancedQuadTree(5)
        insertDataToTree(advancedTree, items)
        var metrics = advancedTree.metrics()
        advancedTree.updateHealth(metrics)
        println(metrics.toString())
        println("Health: ${advancedTree.health}\n\n")
        advancedTree.optimise()
        metrics = advancedTree.metrics()
        println(metrics.toString())
        println("Health: ${advancedTree.health}")
        testInsert(advancedTree)
    }
}