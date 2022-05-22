package com.hookiesolutions.webhookie.subscription.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class Converter(
  private val om: ObjectMapper,
) {
  private val mapTypeRef = MapTypeRef()

  fun read(body: ByteArray, contentType: String): Any {
    if(contentType == MediaType.APPLICATION_JSON_VALUE) {
      return json(body)
    } else if (contentType == MediaType.APPLICATION_XML_VALUE) {
      return xml(body)
    }

    return string(body)
  }

  fun json(ba: ByteArray): Map<String, Any> {
    try {
      return om.readValue(ba, mapTypeRef) as Map<String, Any>
    } catch (e: Exception) {
      println(e.message)
    }

    return emptyMap()
  }

  fun xml(body: ByteArray): Map<String, Any> {
    try {
      val xm = XmlMapper()
      return xm.readValue(body, mapTypeRef) as Map<String, Any>
    } catch (e: Exception) {
      println(e.message)
    }

    return emptyMap()
  }

  fun string(body: ByteArray): String {
    return String(body)
  }
}
