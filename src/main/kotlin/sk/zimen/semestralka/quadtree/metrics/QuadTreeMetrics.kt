package sk.zimen.semestralka.quadtree.metrics

/**
 * Class that hold relevant information about QuadTree.
 * @author David Zimen
 */
class QuadTreeMetrics {
    var depth = 0
    var nodesTop = 0
    var nodesBottom = 0
    var nodesLeft = 0
    var nodesRight = 0
    var dataTop = 0
    var dataBottom = 0
    var dataLeft = 0
    var dataRight = 0
    var leftX = 0.0
    var rightX = 0.0
    var topY = 0.0
    var bottomY = 0.0
    var balanceFactorX = 0
    var balanceFactorY = 0
    var divisibleDataCount = 0

    override fun toString(): String {
        return "Depth: $depth, Items that can be divided: $divisibleDataCount\n" +
                "Balance on X axis: ${balanceFactorX}, Balance on Y axis: ${balanceFactorY}\n" +
                "Nodes on top: $nodesTop, Nodes on bottom: $nodesBottom\n" +
                "Nodes on left: $nodesLeft, Nodes on right: $nodesRight\n" +
                "Data on top: $dataTop, Data on bottom: $dataBottom\n" +
                "Data on left: $dataLeft, Data on right: $dataRight\n" +
                "X most on left: $leftX, X most on right: $rightX\n" +
                "Y most on top: $topY, Y most on bottom: $bottomY\n"
    }
}

data class NodeMetrics(
    val dataCount: Int,
    val divisibleDataCount: Int,
    val nodesCount: Int,
    val depth: Int,
    val leftX: Double,
    val rightX: Double,
    val topY: Double,
    val bottomY: Double
)