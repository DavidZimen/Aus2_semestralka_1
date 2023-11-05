package sk.zimen.semestralka.quadtree.metrics

/**
 * Class that hold relevant information about QuadTree.
 * @author David Zimen
 */
class QuadTreeMetrics {
    var depth = 0
    var potentialDepth = 0
    var nodesTop = 0
    var nodesBottom = 0
    var nodesLeft = 0
    var nodesRight = 0
    var dataTop = 0
    var dataBottom = 0
    var dataLeft = 0
    var dataRight = 0
    var dataRoot = 0
    var leftX = 0.0
    var rightX = 0.0
    var topY = 0.0
    var bottomY = 0.0
    var balanceFactorX = 0.0
    var balanceFactorY = 0.0
    var dataBalanceFactorX = 0.0
    var dataBalanceFactorY = 0.0
    var divisibleDataSize = 0

    override fun toString(): String {
        return "\n-------------------------------Quad Tree Metrics-------------------------------\n" +
                "Depth: $depth, Potential depth: $potentialDepth, Items that can be divided: $divisibleDataSize\n" +
                "Balance of nodes on X: ${balanceFactorX}, Balance of node on Y: ${balanceFactorY}\n" +
                "Balance of data on X: ${dataBalanceFactorX}, Balance of data on Y: ${dataBalanceFactorY}\n" +
                "Nodes on top: $nodesTop, Nodes on bottom: $nodesBottom\n" +
                "Nodes on left: $nodesLeft, Nodes on right: $nodesRight\n" +
                "Data on top: $dataTop, Data on bottom: $dataBottom\n" +
                "Data on left: $dataLeft, Data on right: $dataRight\n" +
                "Data in root: $dataRoot\n" +
                "X most on left: $leftX, X most on right: $rightX\n" +
                "Y most on top: $topY, Y most on bottom: $bottomY\n" +
                "------------------------------------------------------------------------------\n"
    }
}

class SubTreeMetrics() {
    var dataCount = 0
    var divisibleDataSize = 0
    var nodesCount = 0
    var depth = 0
    var leftX = 0.0
    var rightX = 0.0
    var topY = 0.0
    var bottomY = 0.0
    var potentialDepth = 0

    constructor(dataCount: Int, divisibleDataSize: Int, nodesCount: Int, depth: Int,
                leftX: Double, rightX: Double, topY: Double, bottomY: Double, potentialDepth: Int) : this() {
        this.dataCount = dataCount
        this.divisibleDataSize = divisibleDataSize
        this.nodesCount = nodesCount
        this.depth = depth
        this.leftX = leftX
        this.rightX = rightX
        this.topY = topY
        this.bottomY = bottomY
        this.potentialDepth = potentialDepth
    }
}
