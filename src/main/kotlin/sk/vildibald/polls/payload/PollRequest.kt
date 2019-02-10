package sk.vildibald.polls.payload

data class PollRequest(
        val question: String,
        val choices: List<ChoiceRequest>,
        val pollLength: PollLength
)