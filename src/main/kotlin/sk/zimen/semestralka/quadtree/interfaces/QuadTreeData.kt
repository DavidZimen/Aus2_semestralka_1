package sk.zimen.semestralka.quadtree.interfaces

import sk.zimen.semestralka.quadtree.boundary.Boundary

/**
 * Interface to make inserted data into QuadTree convert to boundaries.
 */
abstract class QuadTreeData<T : QuadTreeKey> {

    lateinit var key: T

    abstract fun toKey(boundary: Boundary): T
}
