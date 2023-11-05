package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.utils.Mapper

class CombinedService private constructor() {
    /**
     * Main structure for holding all [Place]ies of the application.
     */
    private val combinedPlaces: QuadTree<Place> = AdvancedQuadTree(10)

    fun add(place: Place) {
        combinedPlaces.insert(place)
    }

    fun edit(placeBefore: Place, placeAfter: Place) {
        combinedPlaces.edit(placeBefore, placeAfter)
    }

    fun find(position: GpsPosition): List<Place> {
        return try {
            combinedPlaces.find(Mapper.toBoundary(position))
        } catch (e: NoResultFoundException) {
            emptyList()
        }
    }

    fun all(): List<Place> = combinedPlaces.all()

    fun delete(place: Place) = combinedPlaces.delete(place)

    fun changeParameters(maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double)
            = combinedPlaces.changeParameters(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)

    companion object {
        private var instance: CombinedService? = null

        fun getInstance(): CombinedService {
            if (instance == null) {
                synchronized(this) {
                    instance = CombinedService()
                }
            }
            return instance!!
        }
    }
}