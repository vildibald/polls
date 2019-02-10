package sk.vildibald.polls.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(
        val resourceName: String,
        val fieldName: String,
        val fieldValue: Any
) : RuntimeException("Resource '$resourceName' not found by field '$fieldName' with value '$fieldValue'")