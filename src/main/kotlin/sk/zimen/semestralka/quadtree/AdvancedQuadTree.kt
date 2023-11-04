package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.quadtree.node.AdvancedNode
import sk.zimen.semestralka.quadtree.node.Node

/**
 * Class that specifically implements [QuadTree] with [AdvancedNode]
 * for underlying logic.
 * [K] Key which is used for manipulating the data in structure.
 *      Must implement [QuadTreeKey] interface to work correctly.
 * [T] Data type to be used in Quad Tree.
 *      Must implement [QuadTreeData] interface to work correctly.
*/
class AdvancedQuadTree<K : QuadTreeKey, T : QuadTreeData<K>> : QuadTree<K, T> {

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
    ): Node<K, T> {
        return AdvancedNode(0, Boundary(doubleArrayOf(topLeftX, topLeftY), doubleArrayOf(bottomRightX, bottomRightY)))
    }
}
