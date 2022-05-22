package com.hookiesolutions.webhookie.subscription.service

import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

interface ResponseService {
  fun result(value: Any, contentType: String): Mono<ResponseEntity<Any>>
}
