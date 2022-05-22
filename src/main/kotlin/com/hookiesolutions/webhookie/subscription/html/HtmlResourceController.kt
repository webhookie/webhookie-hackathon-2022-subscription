package com.hookiesolutions.webhookie.subscription.html

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 19/5/2022 20:53
 */
@Controller
class HtmlResourceController(
  private val properties: HtmlResourceProperties
) {
  @GetMapping(
    "{file}.html",
    produces = [MediaType.TEXT_HTML_VALUE]
  )
  fun html(@PathVariable file: String): Mono<ResponseEntity<String>> {
    return properties.files.getOrElse(file) { throw ResourceNotFoundException(file) }
      .toMono()
      .map { readFile(it) }
      .map { ResponseEntity.ok(it) }
      .doOnError { println(it) }
      .onErrorReturn(ResponseEntity.notFound().build())
  }

  fun readFile(filePath: String): String {
    val path = Paths.get(filePath)
    return Files.readAllLines(path)
      .reduce { acc, s -> "$acc\n$s" }
  }
}

class ResourceNotFoundException(name: String): RuntimeException("file not found for key: $name")

@ControllerAdvice
class ErrorHAndler {
  @ExceptionHandler(ResourceNotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun notFound(ex: ResourceNotFoundException): Mono<String> {
    return ex.message.toMono()
  }
}
