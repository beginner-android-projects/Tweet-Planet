package com.hsmsample.tweetplanet.tweets

import androidx.lifecycle.*
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepositoryImpl
import com.hsmsample.tweetplanet.utils.ERROR_MESSAGE
import com.hsmsample.tweetplanet.utils.SEARCH_DELAY_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val tweetsRepositoryImpl: TweetsRepositoryImpl,
) : ViewModel() {

    private val _errorHandler = MutableLiveData<String>()

    val errorHandler: LiveData<String> get() = _errorHandler


    private val _progressDialog = MutableLiveData<Boolean>()

    val progressDialog: LiveData<Boolean> get() = _progressDialog


    private val _searchChannel = MutableStateFlow("")

    private val searchChannel = _searchChannel.asStateFlow()

    fun setSearchQuery(search: String) {
        _searchChannel.update { search }
    }

    /**
     * 1. Check whether rules are already added using [retrieveRules]
     * 2. Add a new rule if the list if empty
     * 3. Else delete and add a new rule
     * 4. Make the Filtered Stream call
     */

    @FlowPreview
    suspend fun observeSearchChanges() {

        searchChannel
            .debounce { SEARCH_DELAY_MILLIS }
            .distinctUntilChanged()
            .collectLatest { keyword ->
                Timber.d("They key has changed $keyword")
                if (keyword.isNotEmpty())
                    initializeFilteredStream(keyword)
                else
                    retrieveRules(null)
            }

    }

    private fun initializeFilteredStream(keyword: String) {

        viewModelScope.launch {

            retrieveRules(keyword)

            /*if (initCall) {

                initCall = false

                addAndInitializeFetching(keyword)

            } else {



            }*/


        }
    }

    private fun retrieveRules(keyword: String?) {
        viewModelScope.launch {

            _progressDialog.value = true

            val response = tweetsRepositoryImpl.retrieveRules()


            if (response.isSuccess) {
                _progressDialog.value = false

                if (response.getOrDefault(emptyList()).isNotEmpty())
                    deleteAndInitializeFetching(response, keyword)
                else
                    if (keyword != null)
                        addAndInitializeFetching(keyword)

            } else {
                _progressDialog.value = false
                _errorHandler.value = ERROR_MESSAGE
            }

        }
    }

    /*private val _liveTweets = MutableLiveData<ResponseBody>()

    val liveTweets: LiveData<ResponseBody> get() = _liveTweets

    fun observeLiveTweets() {

        viewModelScope.launch {

            tweetsRepositoryImpl.getFilteredStream()
                .collect { value: ResponseBody? ->

                    _liveTweets.value = value
                }

        }

    }*/

    private suspend fun addAndInitializeFetching(keyword: String) {

        _progressDialog.value = true

        val addRuleResponse = tweetsRepositoryImpl.addRule(keyword)

        when {
            addRuleResponse.isSuccess -> {
                _progressDialog.value = false
                /**
                 * If we are fetching the filtered stream inside a viewmodel scope
                 * the cold stream will stop sending data as soon as the viewmodel scope dies,
                 * in our case it's going to live as long as the fragment (not the fragment view)
                 * lives, based on the context you initialize your viewmodel in.
                 */


                Timber.d("Fetching the filtered stream inside viewmodel scope")
            }
            addRuleResponse.isFailure -> {

                _progressDialog.value = false

                _errorHandler.value = ERROR_MESSAGE
            }
        }

    }

    private suspend fun deleteAndInitializeFetching(
        response: Result<List<MatchingRule>>,
        keyword: String?
    ) {
        _progressDialog.value = true

        val rulesList = response.getOrDefault(emptyList())

        val deleteRules = if (rulesList.firstOrNull() != null)
            tweetsRepositoryImpl.deleteExistingRules(rulesList.firstOrNull()?.id.orEmpty())
        else Result.success(null)

        when {
            deleteRules.isSuccess -> {

                _progressDialog.value = false

                if (keyword != null)
                    addAndInitializeFetching(keyword)
            }

            deleteRules.isFailure -> {
                _progressDialog.value = false

                _errorHandler.value = ERROR_MESSAGE
            }
        }
    }

}