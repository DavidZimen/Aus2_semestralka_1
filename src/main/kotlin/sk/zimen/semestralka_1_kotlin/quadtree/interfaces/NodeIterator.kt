package sk.zimen.semestralka_1_kotlin.quadtree.interfaces

import sk.zimen.semestralka_1_kotlin.quadtree.node.Node
import java.util.NoSuchElementException

/**
 * Interface for NodeIterator with few special function for additional functionality.
 * @author David Zimen
 */
interface NodeIterator<K : QuadTreeKey, T : QuadTreeData<K>> : MutableIterator<Node<K, T>> {
    /**
     * Special method to return only next node in iterator, without adding its children for the iteration.
     * @return Current node on top of stack.
     * @throws NoSuchElementException When no element is present in stack.
     */
    fun nextWithoutChildren(): Node<K, T>

    /**
     * Special method to add method into Iterator outside the next method.
     * Use very carefully !!!.
     * @param quadtreeNode Node to be added to the iteration.
     */
    fun addToIteration(quadtreeNode: Node<K, T>)
}
