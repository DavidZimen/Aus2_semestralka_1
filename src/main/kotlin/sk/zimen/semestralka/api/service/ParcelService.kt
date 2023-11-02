package sk.zimen.semestralka.api.service

import sk.zimen.semestralka.api.types.*
import sk.zimen.semestralka.quadtree.AdvancedQuadTree
import sk.zimen.semestralka.quadtree.QuadTree
import sk.zimen.semestralka.quadtree.exceptions.NoResultFoundException
import sk.zimen.semestralka.utils.Mapper


class ParcelService private constructor(){
    /**
     * Main structure for holding all [Parcel]ies of the application.
     */
    private val parcels: QuadTree<PlaceKey, Parcel> = AdvancedQuadTree(10)

    private val combinedService = CombinedService.getInstance()

    init {
        add(
            Parcel(
                100,
                "Some random desc 1",
                GpsPosition(20.0, WidthPos.Z, 20.0, HeightPos.S),
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S)
            )
        )
        add(
            Parcel(
                200,
                "Another desc for parcel",
                GpsPosition(15.0, WidthPos.Z, 15.0, HeightPos.S),
                GpsPosition(14.0, WidthPos.Z, 14.0, HeightPos.S)
            )
        )
    }

    fun add(parcel: Parcel) {
        //parcel.propertiesForParcel = PropertyService.getInstance().find(parcel.key)
        parcels.insert(parcel)
        combinedService.add(parcel)
    }

    fun edit(parcelBefore: Parcel, parcelAfter: Parcel) {
        parcels.edit(parcelBefore, parcelAfter)
        combinedService.edit(parcelBefore, parcelAfter)
    }

    fun find(position: GpsPosition): List<Parcel> {
        return try {
            parcels.find(Mapper.toKey(position))
        } catch (e: NoResultFoundException) {
            emptyList()
        }
    }

    fun find(key: PlaceKey): List<Parcel> {
        return try {
            parcels.find(key)
        } catch (e: NoResultFoundException) {
            emptyList()
        }
    }

    fun delete(parcel: Parcel) {
        parcels.delete(parcel)
        combinedService.delete(parcel)
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