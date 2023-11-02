package sk.zimen.semestralka.utils

import java.io.*

object CopyUtils {
    fun deepCopy(obj: Serializable): Any {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)

        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)

        return objectInputStream.readObject()
    }
}