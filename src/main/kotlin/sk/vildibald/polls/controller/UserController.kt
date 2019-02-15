package sk.vildibald.polls.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import sk.vildibald.polls.payload.*
import sk.vildibald.polls.security.CurrentUser
import sk.vildibald.polls.security.UserPrincipal

interface IUserControllerImpl {
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