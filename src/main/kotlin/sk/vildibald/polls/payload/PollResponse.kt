package sk.vildibald.polls.payload

import java.time.Instant

data class PollResponse(
        val id: Long,
        val question: String,
        val choices: List<ChoiceResponse>,
        val expirationDateTime: Instant,
        val createdBy: Long,
        val updatedBy: Long,
        val createdAt: Instant,
        val updatedAt: Instant
)