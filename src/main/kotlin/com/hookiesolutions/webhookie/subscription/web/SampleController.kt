package com.hookiesolutions.webhookie.subscription.web

import com.hookiesolutions.webhookie.subscription.service.DelayService
import com.hookiesolutions.webhookie.subscription.util.Converter
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 4/12/20 18:16
 */
@RestController
class SampleController(
  private val converter: Converter,
  private val delayService: DelayService
) {
  fun handleRequest(
    body: ByteArray,
    contentType: String,
    headers: Map<String, String>,
    number: String,
    item: String
  ): Any {
    headers.forEach {
      println("$number =======> ${it.key} ==========> ${it.value}")
    }

    val message = converter.read(body, contentType)
    println("/${item.lowercase()}, $contentType ===> $message")
    return message
  }

/*
  @PostMapping("/product{number:\\d+|\$}")
  fun handleProductExtended(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable(required = false, ) number: String?,
  ): Mono<ResponseEntity<String>> = handleRequest(body, contentType, headers, number ?: "0", "Product")

  @PostMapping("/order{number:\\d+|\$}")
  fun handleOrder(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable(required = false, ) number: String?,
  ): Mono<ResponseEntity<String>> = handleRequest(body, contentType, headers, number ?: "0", "Order")

  @PostMapping("/weather{number:\\d+|\$}")
  fun handleWeather(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable(required = false, ) number: String?,
  ): Mono<ResponseEntity<String>> = handleRequest(body, contentType, headers, number ?: "0", "Weather")
*/

  @PostMapping("/{path}/404")
  fun handle404(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable path: String,
    @RequestParam(required = false, defaultValue = "0") delay: String
  ): Mono<ResponseEntity<Any>> {
    handleRequest(body, contentType, headers, path, "404")
    val entity = ResponseEntity
      .notFound()
      .build<Any>()
    return delayService.delay(entity, delay)
  }

  @PostMapping("/{path}")
  fun handleSuccess(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable path: String,
    @RequestParam(required = false, defaultValue = "0") delay: String
  ): Mono<ResponseEntity<Any>> {
    val response = handleRequest(body, contentType, headers, path, "200")
    val entity = ResponseEntity
      .status(200)
      .contentType(MediaType.parseMediaType(contentType))
      .body(response)
    return delayService.delay(entity, delay)
  }

  @PostMapping("/{path}/200")
  fun handle200(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable path: String,
    @RequestParam(required = false, defaultValue = "0") delay: String
  ): Mono<ResponseEntity<Any>> {
    return handleSuccess(body, contentType, headers, path, delay)
  }

  @PostMapping("/{path}/4xx")
  fun handle4xx(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable path: String,
    @RequestParam(required = false, defaultValue = "0") delay: String
  ): Mono<ResponseEntity<Any>> {
    val response = handleRequest(body, contentType, headers, path, "4xx")
    val entity = ResponseEntity
      .badRequest()
      .body(response)
    return delayService.delay(entity, delay)
  }

  @PostMapping("/{path}/5xx")
  fun handle5xx(
    @RequestBody body: ByteArray,
    @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String,
    @RequestHeader headers: Map<String, String>,
    @PathVariable path: String,
    @RequestParam(required = false, defaultValue = "0") delay: String
  ): Mono<ResponseEntity<Any>> {
    val response = handleRequest(body, contentType, headers, path, "5xx")
    val entity = ResponseEntity
      .internalServerError()
      .body(response)
    return delayService.delay(entity, delay)
  }
}
