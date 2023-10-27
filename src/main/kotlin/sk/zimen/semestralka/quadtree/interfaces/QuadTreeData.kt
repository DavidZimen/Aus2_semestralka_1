package sk.zimen.semestralka.quadtree.interfaces

/**
 * Interface to make inserted data into QuadTree convert to boundaries.
 */
abstract class QuadTreeData<T : QuadTreeKey?>(var key: T) {

    abstract fun printData()
}
