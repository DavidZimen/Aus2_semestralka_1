package sk.zimen.semestralka.quadtree.utils

import org.junit.jupiter.api.Test
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.utils.Generator
import kotlin.system.measureTimeMillis

internal class ProfilerTest {
    @Test
    fun profilerTest() {
        val treeItemsCount = 10_000
        val findCount = 1_000
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

    fun findOperations(tree: QuadTree<Place>, boundaries: List<Boundary>) {
        for (b in boundaries) {
            try {
                tree.find(b)
            } catch (_: Exception) { }
        }
    }
}