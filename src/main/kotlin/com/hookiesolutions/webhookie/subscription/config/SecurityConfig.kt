package com.hookiesolutions.webhookie.subscription.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurityDsl
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 23/4/2022 09:17
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

  @Bean
  fun springSecurityFilterChain(
    http: ServerHttpSecurity,
    oauth2SecurityConfigCustomizer: Customizer<ServerHttpSecurityDsl>,
    permitAllSecurityConfigCustomizer: Customizer<ServerHttpSecurityDsl>,
  ): SecurityWebFilterChain {
    http.cors(Customizer.withDefaults())
    return http {
      csrf { disable() }
      permitAllSecurityConfigCustomizer.customize(this)
      oauth2SecurityConfigCustomizer.customize(this)
    }
  }


  @Bean
  fun oauth2SecurityConfigCustomizer(): Customizer<ServerHttpSecurityDsl> = Customizer { security ->
    security.apply {
      authorizeExchange {
        authorize("/oauth2/**", authenticated)
        authorize()
      }
      oauth2ResourceServer {
        jwt {}
      }
    }
  }


  @Bean
  fun permitAllSecurityConfigCustomizer(): Customizer<ServerHttpSecurityDsl> = Customizer { security ->
    security.apply {
      authorizeExchange {
        authorize("/**", permitAll)
      }
    }
  }
}
