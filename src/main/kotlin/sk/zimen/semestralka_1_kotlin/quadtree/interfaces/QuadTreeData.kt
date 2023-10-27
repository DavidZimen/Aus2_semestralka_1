package sk.zimen.semestralka_1_kotlin.quadtree.interfaces

/**
 * Interface to make inserted data into QuadTree convert to boundaries.
 */
abstract class QuadTreeData<T : QuadTreeKey?>(var key: T) {

    abstract fun printData()
}
