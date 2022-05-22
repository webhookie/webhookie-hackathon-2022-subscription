package com.hookiesolutions.webhookie.subscription.web

import com.hookiesolutions.webhookie.subscription.util.CryptoUtils
import org.slf4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthFilter(
  private val log: Logger
) : WebFilter {
  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    val request: ServerHttpRequest = exchange.request
    val headers: HttpHeaders = request.headers

    if(request.queryParams["hmac"].isNullOrEmpty()) {
      return chain.filter(exchange)
    }

    if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
      log.warn("'{}' could not be found! request is REJECTED!", HttpHeaders.AUTHORIZATION)
      throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
    log.info("Checking HMAC signature for '{}', '{}'", request.path, request.queryParams)

    val authSignature = headers[HttpHeaders.AUTHORIZATION]!!
      .first()
      .replace("Signature ", "")

    val values = authSignature.split(",")

    val attrs = mutableMapOf<String,String>()
    values.forEach {
      val key = it.substringBefore("=")
      val value = it.substringAfter("=")
      attrs[key] = value
    }

    val time = headers["Date"]!!.first()
    val traceId = headers["x-trace-id"]!!.first()
    val spanId = headers["x-span-id"]!!.first()


    val signatureValue = "(request-target): ${request.method?.name} ${request.uri}" +
        " date: $time" +
        " x-trace-id: $traceId" +
        " x-span-id: $spanId"

    val keyId = attrs["keyId"] as String

    val sv = attrs["signature"]!!
    val signature = CryptoUtils.hmac(signatureValue, "MY SECRET")

    log.info("Checking MY SECRET, '{}' with value from header: '{}'", signature, sv)
    if(signature != sv) {
      log.warn("Invalid signature! values: '{}', '{}'", attrs, headers)
      val hmac = CryptoUtils.hmac(signatureValue, keyId)
      log.info("Checking for same keyId '{}', '{}'....", hmac, sv)
      if(hmac != sv) {
        log.info("request is REJECTED! '{}', '{}'", request.path, request.queryParams)
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
      }
    }

    log.info("request is ALLOWED! '{}', '{}'", request.path, request.queryParams)

    return chain.filter(exchange)
  }
}
