package sk.zimen.semestralka.quadtree

import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeData
import sk.zimen.semestralka.quadtree.interfaces.QuadTreeKey
import sk.zimen.semestralka.quadtree.node.ClassicNode
import sk.zimen.semestralka.quadtree.node.Node

/**
 * Class that specifically implements [QuadTree] with [ClassicNode]
 * for underlying logic.
 * [K] Key which is used for manipulating the data in structure.
 *      Must implement [QuadTreeKey] interface to work correctly.
 * [T] Data type to be used in Quad Tree.
 * Must implement [QuadTreeData] interface to work correctly.
 * @author David Zimen
</T></K> */
class ClassicQuadTree<K : QuadTreeKey, T : QuadTreeData<K>> : QuadTree<K, T> {

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
        return ClassicNode(0, Boundary(doubleArrayOf(topLeftX, topLeftY), doubleArrayOf(bottomRightX, bottomRightY)))
    }
}
