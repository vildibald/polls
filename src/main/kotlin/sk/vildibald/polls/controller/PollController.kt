package sk.vildibald.polls.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import sk.vildibald.polls.payload.ExtendedPollResponse
import sk.vildibald.polls.payload.PagedResponse
import sk.vildibald.polls.payload.PollRequest
import sk.vildibald.polls.payload.VoteRequest
import sk.vildibald.polls.security.CurrentUser
import sk.vildibald.polls.security.UserPrincipal
import javax.validation.Valid

interface PollController {
    fun polls(currentUser: UserPrincipal?,
              page: Int,
              size: Int):
            PagedResponse<ExtendedPollResponse>

    fun createPoll(currentUser: UserPrincipal,
                   pollRequest: PollRequest)
            : ResponseEntity<*>

    fun pollById(currentUser: UserPrincipal,
                 pollId: Long?)
            : ExtendedPollResponse

    fun castVote(currentUser: UserPrincipal,
                 pollId: Long?,
                 voteRequest: VoteRequest)
            : ExtendedPollResponse
}