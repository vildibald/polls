package sk.vildibald.polls

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.context.TestPropertySource

@Retention(AnnotationRetention.RUNTIME)
@IfProfileValue(name="testprofile", value="integrationtest")
@SpringBootTest
@TestPropertySource(locations=["classpath:test.properties"])
annotation class IntegrationTest