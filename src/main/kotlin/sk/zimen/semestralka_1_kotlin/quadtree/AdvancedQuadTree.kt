package sk.zimen.semestralka_1_kotlin.quadtree

import sk.zimen.semestralka_1_kotlin.quadtree.boundary.Boundary
import sk.zimen.semestralka_1_kotlin.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka_1_kotlin.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka_1_kotlin.quadtree.node.AdvancedNode
import sk.zimen.semestralka_1_kotlin.quadtree.node.Node

/**
 * Class that specifically implements [QuadTree] with [AdvancedNode]
 * for underlying logic.
 * [K] Key which is used for manipulating the data in structure.
 *      Must implement [QuadTreeKey] interface to work correctly.
 * [T] Data type to be used in Quad Tree.
 *      Must implement [QuadTreeData] interface to work correctly.
*/
class AdvancedQuadTree<K : QuadTreeKey, T : QuadTreeData<K>> : QuadTree<K, T> {

    constructor() : super()

    constructor(maxAllowedDepth: Int) : super(maxAllowedDepth)

    constructor(topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double) : super(
        topLeftX,
        topLeftY,
        bottomRightX,
        bottomRightY
    )

    override fun createRoot(
        topLeftX: Double,
        topLeftY: Double,
        bottomRightX: Double,
        bottomRightY: Double
    ): Node<K, T> {
        return AdvancedNode(0, Boundary(doubleArrayOf(topLeftX, topLeftY), doubleArrayOf(bottomRightX, bottomRightY)))
    }
}
