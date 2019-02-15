package sk.vildibald.polls

import com.fasterxml.jackson.databind.ObjectMapper

fun Any.toJson() = ObjectMapper().writeValueAsString(this)