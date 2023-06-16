package com.example.stargazers

object SearchValidationUtil {

    /**
     * The search is not valid if:
     * owner name and repository name is empty
     * owner name is Empty
     * repository name is Empty
     */
    fun validateSearchInput(
        ownerName: String,
        repositoryName: String,
    ): SearchValidationResult {
        return when {
            ownerName.isNotEmpty() && repositoryName.isNotEmpty() -> SearchValidationResult.VALID
            ownerName.isNotEmpty() && repositoryName.isEmpty() -> SearchValidationResult.REPOSITORY_NOT_VALID
            ownerName.isEmpty() && repositoryName.isNotEmpty() -> SearchValidationResult.OWNER_NOT_VALID
            else -> SearchValidationResult.OWNER_AND_REPO_NOT_VALID
        }
    }
}

enum class SearchValidationResult {
    OWNER_NOT_VALID,
    REPOSITORY_NOT_VALID,
    OWNER_AND_REPO_NOT_VALID,
    VALID,
}
