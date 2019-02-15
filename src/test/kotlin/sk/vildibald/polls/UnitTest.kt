package sk.vildibald.polls

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue

@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest
@IfProfileValue(name="testprofile", value="unittest")
annotation class UnitTest