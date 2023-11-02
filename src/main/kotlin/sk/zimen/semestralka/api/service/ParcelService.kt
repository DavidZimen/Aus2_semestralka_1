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
        associateProperties(parcel)
        parcels.insert(parcel)
        combinedService.add(parcel)
    }

    fun edit(parcelBefore: Parcel, parcelAfter: Parcel) {
        if (parcelBefore.key != parcelAfter.key) {
            associateProperties(parcelAfter)
        }
        parcels.edit(parcelBefore, parcelAfter)
        combinedService.edit(parcelBefore, parcelAfter)
    }

    fun find(position: GpsPosition): MutableList<Parcel> {
        return try {
            parcels.find(Mapper.toKey(position)) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun find(key: PlaceKey): MutableList<Parcel> {
        return try {
            parcels.find(key) as MutableList
        } catch (e: NoResultFoundException) {
            mutableListOf()
        }
    }

    fun delete(parcel: Parcel) {
        parcels.delete(parcel)
        combinedService.delete(parcel)
    }

    private fun associateProperties(parcel: Parcel) {
        parcel.propertiesForParcel = PropertyService.getInstance().find(parcel.key)
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