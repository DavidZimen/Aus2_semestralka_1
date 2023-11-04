package sk.zimen.semestralka.quadtree.metrics

import kotlin.math.abs

/**
 * Converts [balance] to double between <-1; 1> by dividing it with size of Quadtree.
 */
fun balanceFactor(balance: Int, treeSize: Int) = balance.toDouble() / treeSize.toDouble()

/**
 * Calculates how much should coordinate change based on provided value.
 * Minimal value is 0.005 which is 0.5%.
 */
fun enlargingPercentage(treeCoordinate: Double, metricsCoordinate: Double): Double {
    val difference = abs(treeCoordinate - metricsCoordinate)
    val scaleFactor = 1.0 / (difference + 1.0)
    val scaledResult = scaleFactor * (difference)
    println("Tree: $treeCoordinate, Metric: $metricsCoordinate, Scale: $scaledResult")
    return if (scaledResult < 0.005) 0.005 else scaledResult
}