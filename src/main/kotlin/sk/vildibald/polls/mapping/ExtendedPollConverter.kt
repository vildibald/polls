package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import sk.vildibald.polls.model.Poll
import sk.vildibald.polls.model.User
import java.time.Instant
import sk.vildibald.polls.payload.ExtendedPollResponse

data class ExtendedPollResponseData(
        val poll: Poll,
        val choiceVotesMap: Map<Long, Long>,
        val creator: User,
        val userVote: Long?
)

class ExtendedPollConverter: Converter<ExtendedPollResponseData, ExtendedPollResponse> {
    override fun convert(source: ExtendedPollResponseData): ExtendedPollResponse {
        val now = Instant.now()
        val choiceResponses = source.poll.choices.map { choice ->
            VoteChoiceConverter().convert(choice to (source.choiceVotesMap[choice.id] ?: 0))
        }
        return ExtendedPollResponse(id = source.poll.id,
                question = source.poll.question,
                choices = choiceResponses,
                creationDateTime = source.poll.createdAt,
                expirationDateTime = source.poll.expirationDateTime,
                isExpired = source.poll.expirationDateTime.isBefore(now),
                createdBy = UserConverter().convert(source.creator),
                selectedChoice = source.userVote,
                totalVotes = choiceResponses.map { it.voteCount }.sum()
        )
    }
}