package com.erdi.blooddonor.data.model

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

data class City(
    val name: String,
    val plate: String,
    val latitude: String,
    val longitude: String,
    val counties: List<String>
)

fun loadCitiesFromJson(context: Context): Array<City>? {
    val json: String
    try {
        val inputStream = context.assets.open("cities.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = buffer.toString(Charset.defaultCharset())
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }

    val gson = Gson()
    return gson.fromJson(json, Array<City>::class.java)
}
