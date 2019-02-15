package sk.vildibald.polls.payload

data class PagedResponse<T>(
        val content: List<T>,
        val page: Int,
        val size: Int,
        val totalElements: Long,
        val totalPages: Long,
        val last: Boolean
)

