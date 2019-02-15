package sk.vildibald.polls.controller.impl

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import sk.vildibald.polls.controller.IHelloWorldControllerImpl

@RestController
@RequestMapping("/")
class HelloWorldControllerImpl : IHelloWorldControllerImpl {
    @GetMapping("/hello")
    @ResponseBody
    override fun hello(): String = "Hello World!"
}
