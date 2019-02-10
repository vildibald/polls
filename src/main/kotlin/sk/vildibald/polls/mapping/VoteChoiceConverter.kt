package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import sk.vildibald.polls.model.Choice
import sk.vildibald.polls.payload.VoteChoiceResponse

class VoteChoiceConverter : Converter<Pair<Choice, Long>, VoteChoiceResponse> {
    override fun convert(source: Pair<Choice, Long>): VoteChoiceResponse =
            VoteChoiceResponse(
                    id = source.first.id,
                    text = source.first.text,
                    voteCount = source.second
            )

}