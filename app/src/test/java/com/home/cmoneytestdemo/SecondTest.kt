package com.home.cmoneytestdemo

import com.home.cmoneytestdemo.second.model.repository.SecondRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 *
 * Hmm, i don't have much experience with Unit Test
 * No previous project needed this
 * I should find time to make it up later
 */
@ExperimentalCoroutinesApi
class SecondTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun getData() = runBlocking {
        SecondRepository().getData()
    }
}