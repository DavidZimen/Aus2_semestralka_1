package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.*
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.utils.Mapper

class PropertyService private constructor() {
    /**
     * Main structure for holding all [Property]ies of the application.
     */
    private val properties: QuadTree<PlaceKey, Property> = AdvancedQuadTree(10)

    private val combinedService = CombinedService.getInstance()

    init {
        add(
            Property(
                1,
                "Some random desc",
                GpsPosition(20.0, WidthPos.Z, 20.0, HeightPos.S),
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S)
            )
        )
        add(
            Property(
                2,
                "Another desc",
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S),
                GpsPosition(14.0, WidthPos.Z, 14.0, HeightPos.S)
            )
        )
    }

    fun add(property: Property) {
        //property.parcelsForProperty = ParcelService.getInstance().find(property.key)
        properties.insert(property)
        combinedService.add(property)
    }

    fun edit(propertyBefore: Property, propertyAfter: Property) {
        properties.edit(propertyBefore, propertyAfter)
        combinedService.edit(propertyBefore, propertyAfter)
    }

    fun find(position: GpsPosition): List<Property> {
        return try {
            properties.find(Mapper.toKey(position))
        } catch (e: NoResultFoundException) {
            emptyList()
        }
    }

    fun find(key: PlaceKey): List<Property> = properties.find(key)

    fun delete(property: Property) {
        properties.delete(property)
        combinedService.delete(property)
    }

    companion object {
        private var instance: PropertyService? = null

        fun getInstance(): PropertyService {
            if (instance == null) {
                synchronized(this) {
                    instance = PropertyService()
                }
            }
            return instance!!
        }
    }
}