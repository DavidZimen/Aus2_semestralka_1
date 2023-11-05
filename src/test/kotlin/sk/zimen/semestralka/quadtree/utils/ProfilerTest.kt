package sk.zimen.semestralka.quadtree.utils

import org.junit.jupiter.api.Test
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.ClassicQuadTree

internal class ProfilerTest {
    @Test
    fun profilerTest() {
        val badHealth = true
        val treeItemsCount = 50_000
        val numberOfOperations = 10_000
        var advancedTree = AdvancedQuadTree<Place>(10)
        var classicTree = ClassicQuadTree<Place>(10)
        var generator = prepareGenerator(advancedTree, badHealth)
        val random = generator.random

        // initialize tree
        val items = generator.generateItems(Place::class, treeItemsCount)
        val insertItems = generator.generateItems(Place::class, numberOfOperations, advancedTree.root.boundary)
        val delItems = ArrayList<Place>(numberOfOperations)
        insertDataToTree(classicTree, items)
        insertDataToTree(advancedTree, items)

        // get healths
        var metrics = advancedTree.calculateMetrics()
        advancedTree.updateHealth(metrics)
        println("AdvancedTree health: ${advancedTree.health}")
        metrics = classicTree.calculateMetrics()
        classicTree.updateHealth(metrics)
        println("Classic health: ${advancedTree.health}")

        for (i in 0 until numberOfOperations) {
            val deleteItem = items.removeAt(random.nextInt(0, items.size))
            delItems.add(deleteItem)
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

        if (!badHealth) return

        // optimise trees
        items.addAll(delItems)
        advancedTree = AdvancedQuadTree(10)
        classicTree = ClassicQuadTree(10)
        insertDataToTree(advancedTree, items)
        insertDataToTree(classicTree, items)
        advancedTree.optimise()
        classicTree.optimise()
        println("AdvancedTree health optimised: ${advancedTree.health}")
        println("ClassicTree health optimised: ${advancedTree.health}")

        for (i in 0 until numberOfOperations) {
            val deleteItem = delItems[i]
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
}