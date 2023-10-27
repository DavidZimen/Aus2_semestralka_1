package sk.zimen.semestralka.quadtree.node

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey

/**
 * Represents a classic node with data in meant for ClassicQuadTree.
 * @author David Zimen
 */
class ClassicNode<K : QuadTreeKey, T : QuadTreeData<K>> : Node<K, T> {

    constructor(level: Int, boundary: Boundary) : super(level, boundary)

    constructor(level: Int, parent: ClassicNode<K, T>?, boundary: Boundary) : super(level, parent, boundary)

    override val isEmpty: Boolean
        get() = dataList.isEmpty()

    override val size: Int
        get() = dataList.size

    override fun simpleInsert(item: T, p: Position) = dataList.add(item)

    override fun dataIterator(): MutableIterator<T> = dataList.iterator()

    override fun removeSingleItem(): T = dataList.removeAt(0)

    override fun canBeDivided(maxDepth: Int): Boolean = !isEmpty && (size > 1 || !isLeaf) && level < maxDepth

    override fun createNewNode(p: Position): Node<K, T> = ClassicNode(level + 1, this, Boundary.createBoundaryOnPosition(p, boundary))
}
