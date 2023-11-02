package sk.zimen.semestralka.utils

import org.apache.commons.math3.util.Precision


object DoubleUtils {

    private val EPSILON = 0.0000001
    val REGEX = Regex("(0|([1-9][0-9]*))(\\\\.[0-9]+)?\$")

    fun isAGreaterOrEqualsToB(a: Double, b: Double): Boolean {
        return Precision.compareTo(a, b, EPSILON) != -1
    }

    fun isALessOrEqualsToB(a: Double, b: Double): Boolean {
        return Precision.compareTo(a, b, EPSILON) != 1
    }

    fun isAGreaterThanB(a: Double, b: Double): Boolean {
        return Precision.compareTo(a, b, EPSILON) == 1
    }

    fun isALessThanB(a: Double, b: Double): Boolean {
        return Precision.compareTo(a, b, EPSILON) == -1
    }

    fun isABetweenBandC(a: Double, b: Double, c: Double): Boolean {
        var newB = b
        var newC = c
        if (isAGreaterThanB(a, c)) {
            newB = c
            newC = b
        }
        return isAGreaterOrEqualsToB(a, newB) && isALessOrEqualsToB(a, newC)
    }

    fun isAEqualsToB(a: Double, b: Double): Boolean {
        return Precision.equals(a, b, EPSILON)
    }

    fun average(a: Double, b: Double): Double {
        return (a + b) / 2.0
    }
}
