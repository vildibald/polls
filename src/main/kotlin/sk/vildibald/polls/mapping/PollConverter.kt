package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import sk.vildibald.polls.model.Poll
import sk.vildibald.polls.payload.PollResponse

class PollConverter: Converter<Poll, PollResponse>{
    override fun convert(source: Poll): PollResponse =
            PollResponse(id = source.id,
                    expirationDateTime = source.expirationDateTime,
                    choices = source.choices.map{ ChoiceConverter().convert(it) },
                    question = source.question,
                    createdBy = source.createdBy!!,
                    createdAt = source.createdAt,
                    updatedAt = source.updatedAt,
                    updatedBy = source.updatedBy!!
            )

}