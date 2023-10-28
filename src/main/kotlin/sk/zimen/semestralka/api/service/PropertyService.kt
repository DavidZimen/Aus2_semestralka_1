package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.utils.Mapper

class PropertyService(maxDepth: Int? = null) {

    /**
     * Main structure for holding all [Property]ies of the application.
     */
    private val properties: QuadTree<PlaceKey, Property>

    init {
       properties = AdvancedQuadTree(maxDepth ?: 20)
    }

    fun add(property: Property) {
        properties.insert(property)
        //TODO naplnenie zoznamu parciel
    }

    fun edit(propertyBefore: Property, propertyAfter: Property) {
        delete(propertyBefore)
        add(propertyAfter)
    }

    fun find(position: GpsPosition): List<Property> = properties.find(Mapper.toKey(position))

    fun delete(property: Property) = properties.delete(property)
}