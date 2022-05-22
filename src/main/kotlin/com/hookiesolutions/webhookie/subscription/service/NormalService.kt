package com.hookiesolutions.webhookie.subscription.service

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class NormalService: ResponseService {
  override fun result(value: Any, contentType: String): Mono<ResponseEntity<Any>> {
    val ok = ResponseEntity
      .status(200)
      .contentType(MediaType.parseMediaType(contentType))
      .body(value)

    return Mono.just(ok)
      .delayElement(Duration.ofSeconds(1))
  }
}
