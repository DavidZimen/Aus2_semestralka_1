package sk.zimen.semestralka.quadtree.metrics

import kotlin.math.abs

/**
 * Converts [balance] to double between <-1; 1> by dividing it with size of QuadTree.
 */
fun balanceFactor(balance: Int, treeSize: Int) = balance.toDouble() / treeSize.toDouble()

/**
 * Factor to of how many items in tree can go to deeper than max allowed depth of QuadTree.
 */
fun divisibleDataFactor(divisibleSize: Int, treeSize: Int) = 1 - (divisibleSize.toDouble() / treeSize)

/**
 * Calculates how much should coordinate change based on provided value.
 * Minimal value is 0.005 which is 0.5%.
 */
fun enlargingPercentage(treeCoordinate: Double, metricsCoordinate: Double): Double {
    val difference = abs(abs(treeCoordinate) - abs(metricsCoordinate))
    val scaleFactor = 1.0 / (difference + 1.0)
    val scaledResult = scaleFactor * (difference)
    return if (scaledResult < 0.005) 0.005 else scaledResult
}