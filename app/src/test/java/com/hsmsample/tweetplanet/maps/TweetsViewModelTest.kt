package com.hsmsample.tweetplanet.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hsmsample.tweetplanet.CoroutineTestRule
import com.hsmsample.tweetplanet.tweets.TweetsViewModel
import com.hsmsample.tweetplanet.tweets.data.repository.FAKE_ITEM_1
import com.hsmsample.tweetplanet.tweets.data.repository.FakeTweetsRepositoryTest
import com.hsmsample.tweetplanet.tweets.data.repository.TweetsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TweetsViewModelTest {

    lateinit var tweetsViewModel: TweetsViewModel
    lateinit var tweetsRepository: TweetsRepository
    lateinit var remoteDataStore: TweetsRemoteDataStore
    lateinit var fakeTweetsRepo: FakeTweetsRepositoryTest

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val server: MockWebServer = MockWebServer()
    private val MOCK_WEBSERVER_PORT = 8080


    @Before
    fun init() {
        /*server.start(MOCK_WEBSERVER_PORT)

        val gson = GsonConverterFactory.create(Gson())
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(gson)
            .build()

        remoteDataStore = TweetsRemoteDataStore(retrofit)*/

        fakeTweetsRepo = FakeTweetsRepositoryTest()

        /*tweetsRepository = TweetsRepository(remoteDataStore, StandardDispatcherTest(), Gson())*/

        tweetsViewModel = TweetsViewModel(fakeTweetsRepo)
    }

    @Test
    fun `filtered stream data emitted`()  = runBlocking {

        val firstItem = fakeTweetsRepo.getFilteredStream().first()

        assert(firstItem?.equals(FAKE_ITEM_1) == true)

    }

    /*@After
    fun shutdown() {
        server.shutdown()
    }*/

}