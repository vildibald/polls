package sk.vildibald.polls.controller

import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.vildibald.polls.security.CurrentUser
import sk.vildibald.polls.security.UserPrincipal
import sk.vildibald.polls.service.PollService
import sk.vildibald.polls.service.UserService
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import sk.vildibald.polls.DEFAULT_PAGE_NUMBER
import sk.vildibald.polls.payload.*


@RestController
@RequestMapping("/api")
class UserController(
        private val pollService: PollService,
        private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun currentUserInfo(@CurrentUser currentUser: UserPrincipal): UserSummary =
            userService.currentUserInfo(currentUser)

    @GetMapping("/user/checkUsernameAvailability")
    fun checkUsernameAvailability(@RequestParam(value = "username") username: String)
            : UserIdentityAvailability =
            userService.checkUsernameAvailability(username)

    @GetMapping("/user/checkEmailAvailability")
    fun checkEmailAvailability(@RequestParam(value = "email") email: String)
            : UserIdentityAvailability =
            userService.checkEmailAvailability(email)

    @GetMapping("/users/{username}")
    fun userProfile(@PathVariable(value = "username") username: String): UserProfile =
            userService.userProfile(username)

    @GetMapping("/users/{username}/polls")
    fun pollsCreatedBy(@PathVariable(value = "username")
                          username: String,
                       @CurrentUser
                          currentUser: UserPrincipal,
                       @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER.toString())
                          page: Int,
                       @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE.toString())
                          size: Int)
            : PagedResponse<ExtendedPollResponse> =
            pollService.pollsCreatedBy(username, currentUser, page, size)


    @GetMapping("/users/{username}/votes")
    fun pollsVotedBy(@PathVariable(value = "username")
                        username: String,
                     @CurrentUser
                        currentUser: UserPrincipal,
                     @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER.toString())
                        page: Int,
                     @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE.toString()) size:
                        Int): PagedResponse<ExtendedPollResponse> =
            pollService.pollsVotedBy(username, currentUser, page, size)
}