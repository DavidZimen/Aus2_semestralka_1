package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.GpsPositions
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.utils.CsvUtils
import sk.zimen.semestralka.utils.Generator
import sk.zimen.semestralka.utils.Mapper


class ParcelService private constructor(){
    /**
     * Main structure for holding all [Parcel]ies of the application.
     */
    private val parcels: QuadTree<Parcel> = AdvancedQuadTree(10)

    private val combinedService = CombinedService.getInstance()

    fun add(parcel: Parcel) {
        associateProperties(parcel)
        parcels.insert(parcel)
        combinedService.add(parcel)
    }

    fun edit(parcelBefore: Parcel, parcelAfter: Parcel) {
        if (parcelBefore.positions != parcelAfter.positions) {
            associateProperties(parcelAfter)
        }
        parcels.edit(parcelBefore, parcelAfter)
        combinedService.edit(parcelBefore, parcelAfter)
    }

    fun find(position: GpsPosition): MutableList<Parcel> {
        return try {
            parcels.find(Mapper.toBoundary(position)) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun find(positions: GpsPositions): MutableList<Parcel> {
        return try {
            parcels.find(Mapper.toBoundary(positions)) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun all(): MutableList<Parcel> = parcels.all() as MutableList

    fun delete(parcel: Parcel) {
        parcels.delete(parcel)
        combinedService.delete(parcel)
    }

    fun changeParameters(maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double)
        = parcels.changeParameters(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)

    fun generateData(count: Int, maxDepth: Int, topLeftX: Double, topLeftY: Double, bottomRightX: Double, bottomRightY: Double) {
        changeParameters(maxDepth, topLeftX, topLeftY, bottomRightX, bottomRightY)
        val items = Generator().generateItems(Parcel::class, count, parcels.root.boundary)
        items.forEach {
            add(it)
        }
    }

    fun saveToFile() {
        val items = all()
        val gpsPositions = ArrayList<GpsPosition>(items.size * 2)
        items.forEach {
            gpsPositions.add(it.positions.topLeft)
            gpsPositions.add(it.positions.bottomRight)
        }
        CsvUtils.writeDataToCSV("parcels.csv", Parcel::class, items)
        CsvUtils.writeDataToCSV("parcels-positions.csv", GpsPosition::class, gpsPositions)
    }

    fun loadFromFile() {
        val items = CsvUtils.readDataFromCSV("parcels.csv", Parcel::class)
        val gpsPositions = CsvUtils.readDataFromCSV("parcels-positions.csv", GpsPosition::class)
        gpsPositions.reverse()
        items.forEach {
            val topLeft = gpsPositions.removeAt(gpsPositions.size - 1)
            val bottomRightX = gpsPositions.removeAt(gpsPositions.size - 1)
            it.positions = GpsPositions(topLeft, bottomRightX)
            add(it)
        }
    }

    private fun associateProperties(parcel: Parcel) {
        parcel.propertiesForParcel = PropertyService.getInstance().find(parcel.positions)
        parcel.propertiesForParcel.forEach {
            it.parcelsForProperty.add(parcel)
        }
    }

    companion object {
        private var instance: ParcelService? = null

        fun getInstance(): ParcelService {
            if (instance == null) {
                synchronized(this) {
                    instance = ParcelService()
                }
            }
            return instance!!
        }
    }
}