package com.example.stargazers

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SearchValidationUtilTest {

    @Test
    fun `empty owner name and repository name return OWNER_AND_REPO_NOT_VALID`() {
        val result = SearchValidationUtil.validateSearchInput("","")
        assertThat(result).isEqualTo(SearchValidationResult.OWNER_AND_REPO_NOT_VALID)
    }

    @Test
    fun `empty owner name return OWNER_NOT_VALID`() {
        val result = SearchValidationUtil.validateSearchInput("","123")
        assertThat(result).isEqualTo(SearchValidationResult.OWNER_NOT_VALID)
    }

    @Test
    fun `empty repository name return REPOSITORY_NOT_VALID`() {
        val result = SearchValidationUtil.validateSearchInput("123","")
        assertThat(result).isEqualTo(SearchValidationResult.REPOSITORY_NOT_VALID)
    }

    @Test
    fun `non empty owner name and repository name return VALID`() {
        val result = SearchValidationUtil.validateSearchInput("123","123")
        assertThat(result).isEqualTo(SearchValidationResult.VALID)
    }
}