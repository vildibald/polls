package sk.vildibald.polls.service

import sk.vildibald.polls.DEFAULT_PAGE_SIZE
import sk.vildibald.polls.payload.*
import sk.vildibald.polls.security.UserPrincipal

interface PollService {

    fun allPolls(currentUser: UserPrincipal?,
                 page: Int = 0,
                 size: Int = DEFAULT_PAGE_SIZE)
            : PagedResponse<ExtendedPollResponse>

    fun pollsCreatedBy(username: String,
                       currentUser: UserPrincipal,
                       page: Int = 0,
                       size: Int = DEFAULT_PAGE_SIZE)
            : PagedResponse<ExtendedPollResponse>

    fun pollsVotedBy(username: String,
                     currentUser: UserPrincipal,
                     page: Int = 0,
                     size: Int = DEFAULT_PAGE_SIZE)
            : PagedResponse<ExtendedPollResponse>

    fun createPoll(pollRequest: PollRequest,
                   currentUser: UserPrincipal)
            : PollResponse

    fun pollById(pollId: Long,
                 currentUser: UserPrincipal?)
            : ExtendedPollResponse

    fun castVote(pollId: Long,
                 voteRequest: VoteRequest,
                 currentUser: UserPrincipal)
            : ExtendedPollResponse

}