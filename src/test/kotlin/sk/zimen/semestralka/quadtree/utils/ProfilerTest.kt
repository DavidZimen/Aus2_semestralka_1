package sk.zimen.semestralka.quadtree.utils

import org.junit.jupiter.api.Test
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.ClassicQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.utils.Generator
import kotlin.system.measureTimeMillis

internal class ProfilerTest {
    @Test
    fun profileTestOptimizationFunction() {
        val treeItemsCount = 10_000
        val findCount = 500
        val tree = AdvancedQuadTree<Place>(5)
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
        val findingKeys = generator.generateBoundaries(findCount, tree.root.boundary)
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

    @Test
    fun profilerTest() {
        val generator = Generator()
        val random = generator.random
        val treeItemsCount = 100_000
        val numberOfOperations = 10_000
        val advancedTree = AdvancedQuadTree<Place>(15)
        val classicTree = ClassicQuadTree<Place>(15)

        //initialization of tree
        val items = generator.generateItems(Place::class, treeItemsCount)
        val insertItems = generator.generateItems(Place::class, numberOfOperations)
        insertDataToTree(classicTree, items)
        insertDataToTree(advancedTree, items)

        for (i in 0 until numberOfOperations) {
            val deleteItem = items.removeAt(random.nextInt(0, items.size))
            val insertItem = insertItems[i]
            val findBoundary = deleteItem.getBoundary()

            // classic
            classicTree.find(findBoundary)
            classicTree.insert(insertItem)
            classicTree.delete(deleteItem)

            // then advanced
            advancedTree.find(findBoundary)
            advancedTree.insert(insertItem)
            advancedTree.delete(deleteItem)
        }
    }

    fun findOperations(tree: QuadTree<Place>, boundaries: List<Boundary>) {
        for (b in boundaries) {
            try {
                tree.find(b)
            } catch (_: Exception) { }
        }
    }
}