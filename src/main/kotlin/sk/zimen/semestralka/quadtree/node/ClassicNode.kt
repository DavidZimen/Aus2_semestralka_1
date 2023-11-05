package sk.zimen.semestralka.quadtree.node

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.boundary.Position
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData

/**
 * Represents a classic node with data in meant for ClassicQuadTree.
 * @author David Zimen
 */
class ClassicNode<T : QuadTreeData> : Node<T> {

    constructor(level: Int, boundary: Boundary) : super(level, boundary)

    constructor(level: Int, parent: ClassicNode<T>?, boundary: Boundary) : super(level, parent, boundary)

    override val isEmpty: Boolean
        get() = dataList.isEmpty()

    override val size: Int
        get() = dataList.size

    override fun edit(old: T, new: T): T {
        val iterator = dataList.listIterator()
        for (item in iterator) {
            if (item == old) {
                iterator.set(new)
            }
        }
        return new
    }

    override fun simpleInsert(item: T, p: Position) = dataList.add(item)

    override fun dataIterator(): MutableIterator<T> = dataList.iterator()

    override fun removeSingleItem(): T = dataList.removeAt(0)

    override fun canBeDivided(maxDepth: Int): Boolean = !isEmpty && (size > 1 || !isLeaf) && level < maxDepth

    override fun createNewNode(p: Position): Node<T> = ClassicNode(level + 1, this, Boundary.createBoundaryOnPosition(p, boundary))

    override fun divisibleItems(): List<T> {
        return if (size > 1) {
            dataList.filter { getPosition(it) != Position.CURRENT }
        } else {
            emptyList()
        }
    }
}
