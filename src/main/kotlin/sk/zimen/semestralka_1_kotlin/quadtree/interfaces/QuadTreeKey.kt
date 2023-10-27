package sk.zimen.semestralka_1_kotlin.quadtree.interfaces

import sk.zimen.semestralka_1_kotlin.quadtree.boundary.Boundary

/**
 * Interface to implement for class to ensure, that key can be inserted
 * into QuadTree quadrants.
 */
interface QuadTreeKey {
    fun toBoundary(): Boundary
}
