package com.hookiesolutions.webhookie.subscription.service

import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
@Profile("5xx")
class ServerErrorService: ResponseService {
  override fun result(value: Any, contentType: String): Mono<ResponseEntity<Any>> {
    return Mono.error<ResponseEntity<Any>>(RuntimeException("Server error!"))
      .delayElement(Duration.ofSeconds(1))
  }
}
