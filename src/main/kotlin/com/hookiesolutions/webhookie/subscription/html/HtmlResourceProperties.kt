package com.hookiesolutions.webhookie.subscription.html

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 19/5/2022 20:50
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "html")
class HtmlResourceProperties(
  val files: Map<String, String> = mapOf()
)
