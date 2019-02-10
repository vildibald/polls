package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import sk.vildibald.polls.model.User
import sk.vildibald.polls.payload.UserSummary

class UserConverter : Converter<User, UserSummary> {
    override fun convert(source: User): UserSummary =
            UserSummary(source.id, source.username, source.name)

}