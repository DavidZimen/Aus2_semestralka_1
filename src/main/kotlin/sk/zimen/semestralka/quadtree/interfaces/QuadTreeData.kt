package sk.zimen.semestralka.quadtree.interfaces

import sk.zimen.semestralka.quadtree.boundary.Boundary

/**
 * Interface to make inserted data into QuadTree convert to boundaries.
 */
interface QuadTreeData {
    fun getBoundary(): Boundary

    fun setBoundary(boundary: Boundary)
}
