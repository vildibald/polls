package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import sk.vildibald.polls.model.Choice
import sk.vildibald.polls.payload.ChoiceResponse

class ChoiceConverter : Converter<Choice, ChoiceResponse> {
    override fun convert(source: Choice): ChoiceResponse =
            ChoiceResponse(
                    id = source.id,
                    text = source.text
            )

}