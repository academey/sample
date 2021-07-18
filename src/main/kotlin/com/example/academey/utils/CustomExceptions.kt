package com.example.academey.utils

class CustomExceptions {
    class PasswordFailedExceededException : RuntimeException()
    class PasswordNotMatchedException(val failedCount: Int) : RuntimeException()

    class BadRequestException(msg: String? = null) : RuntimeException(msg)
    class NotFoundException(msg: String? = null) : RuntimeException(msg)
    class AuthenticationEntryPointException(msg: String? = null) : RuntimeException(msg)
    class CommunicationException(msg: String? = null) : RuntimeException(msg)
    class EmailSignInFailedException(msg: String? = null) : RuntimeException(msg)
    class UserExistException(msg: String? = null) : RuntimeException(msg)
    class UserNotFoundException(msg: String? = null) : RuntimeException(msg)
    class UserNickNameDuplicatedException(msg: String? = null) : RuntimeException(msg)
    class ProviderNotFoundException(msg: String? = null) : RuntimeException(msg)
    class TokenNotValidException(msg: String? = null) : RuntimeException(msg)
    class TokenNotExistException(msg: String? = null) : RuntimeException(msg)
    class CreateMatchException(msg: String? = null) : RuntimeException(msg)
    class MatchNotFoundException(msg: String? = null) : RuntimeException(msg)
}
