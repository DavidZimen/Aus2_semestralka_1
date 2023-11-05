package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.node.AdvancedNode
import sk.zimen.semestralka.quadtree.node.Node

/**
 * Class that specifically implements [QuadTree] with [AdvancedNode]
 * for underlying logic.
 * [T] Data type to be used in Quad Tree.
 *      Must implement [QuadTreeData] interface to work correctly.
*/
class AdvancedQuadTree<T : QuadTreeData> : QuadTree<T> {

    constructor()
            : super(5)

    constructor(maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double)
            : super(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)

    constructor(maxAllowedDepth: Int)
            : super(maxAllowedDepth)


    override fun createRoot(
        topLeftX: Double,
        topLeftY: Double,
        bottomRightX: Double,
        bottomRightY: Double
    ): Node<T> {
        return AdvancedNode(0, Boundary(doubleArrayOf(topLeftX, topLeftY), doubleArrayOf(bottomRightX, bottomRightY)))
    }
}
