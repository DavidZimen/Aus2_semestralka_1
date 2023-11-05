package sk.zimen.semestralka.quadtree.interfaces

import sk.zimen.semestralka.quadtree.node.Node

/**
 * Interface for NodeIterator with few special function for additional functionality.
 * @author David Zimen
 */
interface NodeIterator<T : QuadTreeData> : MutableIterator<Node<T>> {
    /**
     * Special method to return only next node in iterator, without adding its children for the iteration.
     * @return Current node on top of stack.
     * @throws NoSuchElementException When no element is present in stack.
     */
    fun nextWithoutChildren(): Node<T>

    /**
     * Special method to add method into Iterator outside the next method.
     * Use very carefully !!!.
     * @param quadtreeNode Node to be added to the iteration.
     */
    fun addToIteration(quadtreeNode: Node<T>)
}
