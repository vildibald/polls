package sk.vildibald.polls.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Dummy controller.
 */
interface HelloWorldController {
    @GetMapping("/hello")
    @ResponseBody
    fun hello(): String
}