package sk.zimen.semestralka.quadtree

import org.junit.jupiter.api.Test
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.utils.*
import sk.zimen.semestralka.utils.GeneratedOperation
import sk.zimen.semestralka.utils.Generator

internal class QuadTreeTestRandomized {
    @Test
    fun randomizedTest() {
        // prepare tree
        val itemsCount = 100_000
        val tree = AdvancedQuadTree<Place>(10)
        val generator = Generator()
        val items = generator.generateItems(Place::class, itemsCount, tree.root.boundary)
        insertDataToTree(tree, items)

        // prepare operations
        val operationsCount = 5000
        val ratio = intArrayOf(1, 2, 1, 3)  //INSERT, DELETE, EDIT, FIND
        val operations = generator.generateOperations(operationsCount, ratio)
            ?: throw IllegalArgumentException("Wrong number of operations or wrong ratio provided.")

        while (operations.isNotEmpty()) {
            val operation = operations.pop()!!
            when (operation) {
                GeneratedOperation.FIND -> {
                    testFind(tree, 1)
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
        }
    }
}