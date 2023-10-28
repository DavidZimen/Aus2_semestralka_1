package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.utils.Mapper

class ParcelService(maxDepth: Int? = null) {
    /**
     * Main structure for holding all [Place]ies of the application.
     */
    private val combinedPlaces: QuadTree<PlaceKey, Place>

    init {
        combinedPlaces = AdvancedQuadTree(maxDepth ?: 20)
    }

    fun add(place: Place) {
        combinedPlaces.insert(place)
        //TODO naplnenie zoznamu parciel
    }

    fun edit(propertyBefore: Place, propertyAfter: Place) {
        delete(propertyBefore)
        add(propertyAfter)
    }

    fun find(position: GpsPosition): List<Place> = combinedPlaces.find(Mapper.toKey(position))

    fun delete(place: Place) = combinedPlaces.delete(place)
}