package sk.zimen.semestralka.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class CsvObject

/**
 * Annotation class, to annotate properties in classes,
 * which should not be written into CSV file.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class CsvExclude


object CsvUtils {
    private const val DELIMITER = ";"

    fun <T : Any> writeDataToCSV(fileName: String, clazz: KClass<T>, data: List<T>) {
        if (data.isEmpty()) return

        val header = clazz.memberProperties
                .filter { it.findAnnotation<CsvExclude>() == null }
                .joinToString(DELIMITER) { it.name }

        val lines = data.joinToString("\n") { item ->
            clazz.memberProperties
                    .filter { it.findAnnotation<CsvExclude>() == null }
                    .joinToString(DELIMITER) { it.get(item).toString() }
        }

        val csvData = "$header\n$lines"
        File(fileName).writeText(csvData)
    }

    fun <T : Any> readDataFromCSV(fileName: String, clazz: KClass<T>): List<T> {
        val lines = File(fileName).readText().lines()
        val objects = mutableListOf<T>()

        if (lines.isNotEmpty()) {
            val header = lines[0].split(',')
            val propertyMap = clazz.memberProperties
                    .map { property -> property as KMutableProperty1<T, *> }
                    .associateBy { it.name }

            for (line in lines.drop(1)) {
                val values = line.split(',')
                if (values.size == header.size) {
                    val objectInstance = clazz.createInstance()
                    for ((index, value) in values.withIndex()) {
                        val propertyName = header[index]
                        val property = propertyMap[propertyName]
                        if (property != null) {
                            val propValue = when (property) {
                                Int::class -> value.toInt()
                                Double::class -> value.toDouble()
                                String::class -> value
                                else -> throw IllegalArgumentException("Unsupported type: $property")
                            }
                            property.setter.call(objectInstance, propValue)
                        }
                    }
                    objects.add(objectInstance)
                }
            }
        }

        return objects
    }

    fun <T : Any> hasHeader(fileName: String, clazz: KClass<T>) {
        BufferedReader(FileReader(fileName)).use { reader ->
            val line = reader.readLine()
        }
    }
}