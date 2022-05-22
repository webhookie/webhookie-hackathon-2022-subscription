package com.hookiesolutions.webhookie.subscription.web

import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.security.Principal

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 23/4/2022 09:19
 */
@RestController
class ResourceController {
  @PreAuthorize("hasAuthority('SCOPE_mod_custom')")
  @GetMapping("/oauth2/{path}")
  fun get(principal: Principal, @PathVariable path: String): Mono<Any> {
    return "Welcome to $path, ${principal.name}".toMono()
  }

  @PreAuthorize("hasAuthority('SCOPE_mod_custom')")
  @PostMapping(value = ["/oauth2/{path}"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun create(
    principal: Principal,
    @RequestBody body: Map<String, Any>,
    @PathVariable path: String,
  ): Mono<Map<String, Any>> {
    println(body)
    return body
      .plus(Pair("message", "Welcome to $path, ${principal.name}"))
      .toMono()
  }
}
