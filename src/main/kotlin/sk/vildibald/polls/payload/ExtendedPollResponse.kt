package sk.vildibald.polls.payload

import java.time.Instant

data class ExtendedPollResponse(
        val id: Long,
        val question: String,
        val choices: List<VoteChoiceResponse>,
        val createdBy: UserSummary,
        val creationDateTime: Instant,
        val expirationDateTime: Instant,
        val isExpired: Boolean,
        val selectedChoice: Long?,
        val totalVotes: Long
)