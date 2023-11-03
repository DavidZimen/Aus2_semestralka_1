package sk.zimen.semestralka.quadtree.interfaces

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.utils.CsvExclude

/**
 * Interface to make inserted data into QuadTree convert to boundaries.
 */
abstract class QuadTreeData<T : QuadTreeKey> {

    @CsvExclude
    lateinit var key: T

    abstract fun toKey(boundary: Boundary): T
}
