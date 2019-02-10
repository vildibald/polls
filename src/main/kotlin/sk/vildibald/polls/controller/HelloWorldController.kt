package sk.vildibald.polls.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HelloWorldController {
    @GetMapping("/hello")
    @ResponseBody
    fun hello(): String = "Hello World!"
}
