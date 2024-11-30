package com.example.myapplication2.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseTypeAdapter<T>(
    private val gson: Gson,
    private val typeOfT: Type
) : TypeAdapter<NetworkResponse<T>>() {
    override fun write(out: JsonWriter, value: NetworkResponse<T>?) {
        // Serialization not needed for this use case
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): NetworkResponse<T> {
        val jsonElement = gson.fromJson<JsonElement>(reader, JsonElement::class.java)
        
        return when {
            jsonElement.isJsonObject -> {
                val jsonObject = jsonElement.asJsonObject

                // Check for success response
                if (jsonObject.has("status") &&  jsonObject.get("status").asInt in 200..299) {
                    val data = if (jsonObject.has("data")) {
                        gson.fromJson<T>(jsonObject.get("data"), typeOfT)
                    } else {
                        null
                    }
                    val status = jsonObject.get("status")?.asInt ?: 200
                    val message = jsonObject.get("message")?.asString ?: "Success"
                    NetworkResponse.Success(status, message, data)
                } else {
                    // Handle failure case
                    val errorObj = jsonObject.getAsJsonObject("error")
                    val errorType = when (errorObj?.get("status")?.asInt ?: 500) {
                        401 -> NetworkResponse.NetworkError.UNAUTHORIZED
                        404 -> NetworkResponse.NetworkError.NOT_FOUND
                        408 -> NetworkResponse.NetworkError.TIMEOUT
                        500 -> NetworkResponse.NetworkError.INTERNAL_SERVER_ERROR
                        else -> null
                    }
                    NetworkResponse.Failure(errorType)
                }
            }
            else -> throw IllegalArgumentException("Unexpected JSON format")
        }
    }
}

// Custom TypeAdapterFactory to register the NetworkResponse adapter
class NetworkResponseTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (!NetworkResponse::class.java.isAssignableFrom(type.rawType)) {
            return null
        }

        val rawType = type.rawType as Class<NetworkResponse<*>>
        val typeArgument = (type.type as? ParameterizedType)?.actualTypeArguments?.get(0)

        @Suppress("UNCHECKED_CAST")
        return NetworkResponseTypeAdapter<T>(
            gson,
            typeArgument ?: Any::class.java
        ) as TypeAdapter<T>
    }
}