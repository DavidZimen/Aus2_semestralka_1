package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.node.ClassicNode
import sk.zimen.semestralka.quadtree.node.Node

/**
 * Class that specifically implements [QuadTree] with [ClassicNode]
 * for underlying logic.
 * @author David Zimen
*/
class ClassicQuadTree<T : QuadTreeData> : QuadTree<T> {

    constructor(): super(10)

    constructor(maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double)
            : super(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)

    constructor(maxAllowedDepth: Int) : super(maxAllowedDepth)


    override fun createRoot(
        topLeftX: Double,
        topLeftY: Double,
        bottomRightX: Double,
        bottomRightY: Double
    ): Node<T> {
        return ClassicNode(0, Boundary(doubleArrayOf(topLeftX, topLeftY), doubleArrayOf(bottomRightX, bottomRightY)))
    }
}
