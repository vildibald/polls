package sk.vildibald.polls.service

import sk.vildibald.polls.payload.UserIdentityAvailability
import sk.vildibald.polls.payload.UserProfile
import sk.vildibald.polls.payload.UserSummary
import sk.vildibald.polls.security.UserPrincipal

interface UserService{
    fun currentUserInfo(currentUser: UserPrincipal): UserSummary

    fun checkUsernameAvailability(newUsername: String): UserIdentityAvailability

    fun checkEmailAvailability(newEmail: String): UserIdentityAvailability

    fun userProfile(username: String): UserProfile
}