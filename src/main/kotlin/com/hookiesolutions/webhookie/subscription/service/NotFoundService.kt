package com.hookiesolutions.webhookie.subscription.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class NotFoundService: ResponseService {
  override fun result(value: Any, contentType: String): Mono<ResponseEntity<Any>> {
    val entity = ResponseEntity
      .notFound()
      .build<Any>()
    return Mono.just<ResponseEntity<Any>>(entity)
      .delayElement(Duration.ofSeconds(1))
  }
}
