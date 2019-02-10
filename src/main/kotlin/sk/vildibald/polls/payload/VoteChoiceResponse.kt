package sk.vildibald.polls.payload

data class VoteChoiceResponse(
        val id: Long,
        val text: String,
        val voteCount: Long
)