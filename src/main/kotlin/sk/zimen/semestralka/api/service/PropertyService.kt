package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.PlaceKey
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.utils.CsvUtils
import sk.zimen.semestralka.utils.Generator
import sk.zimen.semestralka.utils.Mapper

class PropertyService private constructor() {
    /**
     * Main structure for holding all [Property]ies of the application.
     */
    private val properties: QuadTree<PlaceKey, Property> = AdvancedQuadTree(10)

    private val combinedService = CombinedService.getInstance()

    fun add(property: Property) {
        associateParcels(property)
        properties.insert(property)
        combinedService.add(property)
    }

    fun edit(propertyBefore: Property, propertyAfter: Property) {
        if (propertyBefore.key != propertyAfter.key) {
            associateParcels(propertyAfter)
        }
        properties.edit(propertyBefore, propertyAfter)
        combinedService.edit(propertyBefore, propertyAfter)
    }

    fun find(position: GpsPosition): MutableList<Property> {
        return try {
            properties.find(Mapper.toKey(position)) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun find(key: PlaceKey): MutableList<Property> {
        return try {
            properties.find(key) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun all(): MutableList<Property> = properties.all() as MutableList

    fun delete(property: Property) {
        properties.delete(property)
        combinedService.delete(property)
    }

    fun changeParameters(maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double)
        = properties.changeParameters(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)

    fun generateData(count: Int, maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double) {
        changeParameters(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)
        val items = Generator().generateItems(Property::class, count, properties.root.boundary,)
        items.forEach {
            add(it)
        }
    }

    fun saveToFile() {
        val items = all()
        val gpsPositions = ArrayList<GpsPosition>(items.size * 2)
        items.forEach {
            gpsPositions.add(it.key.topLeft)
            gpsPositions.add(it.key.bottomRight)
        }
        CsvUtils.writeDataToCSV("properties.csv", Property::class, items)
        CsvUtils.writeDataToCSV("properties-positions.csv", GpsPosition::class, gpsPositions)
    }

    fun loadFromFile() {
        val items = CsvUtils.readDataFromCSV("properties.csv", Property::class)
        val gpsPositions = CsvUtils.readDataFromCSV("properties-positions.csv", GpsPosition::class)
        gpsPositions.reverse()
        items.forEach {
            val topLeft = gpsPositions.removeAt(gpsPositions.size - 1)
            val bottomRightX = gpsPositions.removeAt(gpsPositions.size - 1)
            it.key = PlaceKey(topLeft, bottomRightX)
            add(it)
        }
    }

    private fun associateParcels(property: Property) {
        property.parcelsForProperty = ParcelService.getInstance().find(property.key)
        property.parcelsForProperty.forEach {
            it.propertiesForParcel.add(property)
        }
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