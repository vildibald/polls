package sk.vildibald.polls.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

interface IHelloWorldControllerImpl {
    @GetMapping("/hello")
    @ResponseBody
    fun hello(): String
}