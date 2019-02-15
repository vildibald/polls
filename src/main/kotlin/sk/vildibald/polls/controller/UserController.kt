package sk.vildibald.polls.controller

import sk.vildibald.polls.payload.*
import sk.vildibald.polls.security.UserPrincipal

interface UserController {
    fun currentUserInfo(currentUser: UserPrincipal)
            : UserSummary

    fun checkUsernameAvailability(username: String)
            : UserIdentityAvailability

    fun checkEmailAvailability(email: String)
            : UserIdentityAvailability

    fun userProfile(username: String)
            : UserProfile

    fun pollsCreatedBy(username: String,
                       currentUser: UserPrincipal,
                       page: Int,
                       size: Int)
            : PagedResponse<ExtendedPollResponse>

    fun pollsVotedBy(username: String,
                     currentUser: UserPrincipal,
                     page: Int,
                     size: Int): PagedResponse<ExtendedPollResponse>
}