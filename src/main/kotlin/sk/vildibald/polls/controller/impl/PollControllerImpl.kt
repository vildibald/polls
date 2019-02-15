package sk.vildibald.polls.controller.impl

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.vildibald.polls.service.PollService
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import sk.vildibald.polls.security.CurrentUser
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.bind.annotation.RequestParam
import sk.vildibald.polls.DEFAULT_PAGE_NUMBER
import sk.vildibald.polls.DEFAULT_PAGE_SIZE
import sk.vildibald.polls.controller.PollController
import sk.vildibald.polls.exception.BadRequestException
import sk.vildibald.polls.payload.*
import sk.vildibald.polls.security.UserPrincipal


@RestController
@RequestMapping("/api/polls")
class PollControllerImpl(
        private val pollService: PollService
) : PollController {
    private val logger = LoggerFactory.getLogger(PollControllerImpl::class.java)

    @GetMapping
    override fun polls(@CurrentUser
                 currentUser: UserPrincipal?,
                       @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER.toString())
                 page: Int,
                       @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE.toString())
                 size: Int):
            PagedResponse<ExtendedPollResponse> {
        return pollService.allPolls(currentUser, page, size)
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    override fun createPoll(@CurrentUser currentUser: UserPrincipal,
                            @Valid @RequestBody pollRequest: PollRequest): ResponseEntity<*> {
        val poll = pollService.createPoll(pollRequest, currentUser)
        val location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.id).toUri()

        return ResponseEntity.created(location)
                .body(ApiResponse(true, "Poll Created Successfully"))
    }


    @GetMapping("/{pollId}")
    override fun pollById(@CurrentUser currentUser: UserPrincipal,
                          @PathVariable pollId: Long?): ExtendedPollResponse = pollId?.let {
        pollService.pollById(pollId, currentUser)
    } ?: throw BadRequestException("Cannot convert parameter '$pollId' to number.")


    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    override fun castVote(@CurrentUser currentUser: UserPrincipal,
                          @PathVariable pollId: Long?,
                          @Valid @RequestBody voteRequest: VoteRequest): ExtendedPollResponse = pollId?.let {
        pollService.castVote(pollId, voteRequest, currentUser)
    } ?: throw BadRequestException("Cannot convert parameter '$pollId' to number.")

}