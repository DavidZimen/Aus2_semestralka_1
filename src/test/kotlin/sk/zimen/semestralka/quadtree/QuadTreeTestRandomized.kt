package sk.zimen.semestralka.quadtree

import org.junit.jupiter.api.Test
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.utils.*
import sk.zimen.semestralka.utils.GeneratedOperation
import sk.zimen.semestralka.utils.Generator
import kotlin.system.measureTimeMillis

internal class QuadTreeTestRandomized {
    @Test
    fun randomizedTest() {
        // prepare tree
        val itemsCount = 10_000
        val tree = AdvancedQuadTree<PlaceKey, Place>(10)
        val generator = Generator()
        val items = generator.generateItems(Place::class, itemsCount, tree.root.boundary)
        insertDataToTree(tree, items)

        // prepare operations
        val operationsCount = 500
        val ratio = intArrayOf(1, 2, 1, 3)  //INSERT, DELETE, EDIT, FIND
        val operations = generator.generateOperations(operationsCount, ratio)
            ?: throw IllegalArgumentException("Wrong number of operations or wrong ratio provided.")

        while (operations.isNotEmpty()) {
            val operation = operations.pop()!!
            when (operation) {
                GeneratedOperation.FIND -> {
                    testFind(tree)
                }
                GeneratedOperation.INSERT -> {
                    val item = generator.generateItems(Place::class, 1, tree.root.boundary)[0]
                    tree.insert(item)
                    testInsert(tree, item)
                }
                GeneratedOperation.DELETE -> {
                    val deletedItem = items.removeAt(generator.random.nextInt(0, items.size))
                    tree.delete(deletedItem)
                    testDelete(tree, deletedItem)
                }
                GeneratedOperation.EDIT -> {
                    val newItem = generator.generateItems(Place::class, 1, tree.root.boundary)[0]
                    val oldItem = items.removeAt(generator.random.nextInt(0, items.size))
                    tree.edit(oldItem, newItem)
                    testEdit(tree, oldItem, newItem)
                }
            }
            println("Remaining: ${operations.size}")
        }
    }

    @Test
    fun profilerTest() {
        val treeItemsCount = 10_000
        val findCount = 1_000
        val tree = AdvancedQuadTree<PlaceKey, Place>(5)
        val generator = with(tree.root) {
            Generator().apply {
                with(createNewNode(Position.TOP_LEFT).boundary) {
                    leftX = topLeft[0]
                    topY = topLeft[1]
                }
                with(createNewNode(Position.TOP_RIGHT).boundary) {
                    rightX = bottomRight[0]
                    bottomY = bottomRight[1] - 10
                }
            }
        }

        // initialize tree
        val items = generator.generateItems(Place::class, treeItemsCount)
        val findingKeys = generator.generateItems(Place::class, findCount, tree.root.boundary).map { it.key }
        insertDataToTree(tree, items)

        // first profiling
        val unoptimizedTime = measureTimeMillis {
            findOperations(tree, findingKeys)
        }
        println("Time taken for unoptimized tree: $unoptimizedTime ms")

        // optimise tree and then run second profiling
        tree.optimise()
        val optimizedTime = measureTimeMillis {
            findOperations(tree, findingKeys)
        }
        println("Time taken for optimized tree: $optimizedTime ms")
    }

    fun findOperations(tree: QuadTree<PlaceKey, Place>, foundKeys: List<PlaceKey>) {
        for (key in foundKeys) {
            try {
                tree.find(key)
            } catch (_: Exception) { }
        }
    }
}