package com.hookiesolutions.webhookie.subscription.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class DelayService {
  fun delay(entity: ResponseEntity<Any>, delay: Long = 0): Mono<ResponseEntity<Any>> {
    return Mono.defer {
      Mono.delay(Duration.ofSeconds(delay))
        .log()
        .map { entity }
    }
  }

  fun delay(entity: ResponseEntity<Any>, delay: String = "0"): Mono<ResponseEntity<Any>> {
    return delay(entity, delay.toLongOrNull() ?: 0)
  }
}
