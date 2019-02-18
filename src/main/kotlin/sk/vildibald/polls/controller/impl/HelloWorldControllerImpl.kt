package sk.vildibald.polls.controller.impl

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import sk.vildibald.polls.controller.HelloWorldController

@RestController
@RequestMapping("/")
class HelloWorldControllerImpl : HelloWorldController {

    private val logger = LoggerFactory.getLogger(HelloWorldControllerImpl::class.java)

    @GetMapping("/hello")
    @ResponseBody
    override fun hello(): String {
//        logger.trace("Hello, I'm a trace log. You can enable me in application.properties")
        logger.info("Hello, I'm a info log.")
        return "Hello World!"
    }
}
