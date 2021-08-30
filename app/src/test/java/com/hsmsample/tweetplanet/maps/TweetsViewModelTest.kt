package com.hsmsample.tweetplanet.maps

import com.hsmsample.tweetplanet.di.dispatchers.StandardDispatcherTest
import com.hsmsample.tweetplanet.maps.repository.FakeTweetsRepository
import com.hsmsample.tweetplanet.tweets.TweetsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TweetsViewModelTest {

    lateinit var tweetsViewModel: TweetsViewModel
    lateinit var tweetsRepository: FakeTweetsRepository


    @Before
    fun init() {

        tweetsRepository = FakeTweetsRepository(StandardDispatcherTest())

        tweetsViewModel = TweetsViewModel(tweetsRepository)

    }


}