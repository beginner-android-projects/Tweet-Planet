package com.hsmsample.tweetplanet.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val tweetsRepositoryImpl: TweetsRepositoryImpl
) : ViewModel() {

    private var initCall = false

    init {
        initCall = true
    }


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
            .debounce { 1000 }
            .distinctUntilChanged()
            .collectLatest { keyword ->
                Timber.d("They key has changed $keyword")
//                fetchFilteredStream(keyword)
            }

    }

    private fun fetchFilteredStream(keyword: String) {

        if (initCall) {

            initCall = false

            addRule(keyword)

        } else {

            retrieveRules(keyword)

        }
    }

    private fun addRule(keyword: String) {

        viewModelScope.launch {

            val response = tweetsRepositoryImpl.addRule(keyword)

            if (response.isSuccess) {
                retrieveRules(keyword)

            } else {
                retrieveRules(keyword)
            }


        }
    }


    private fun deleteRule(ruleId: String, keyword: String) {

        viewModelScope.launch {

            val response = tweetsRepositoryImpl.deleteExistingRules(ruleId)

            if (response.isSuccess) {
                retrieveRules(keyword)
            } else {
                retrieveRules(keyword)
            }

        }

    }

    fun retrieveRules(keyword: String) {
        viewModelScope.launch {

            val response = tweetsRepositoryImpl.retrieveRules()

            if (response.isSuccess && response.getOrDefault(emptyList()).isNotEmpty()) {

                val rulesList = response.getOrDefault(emptyList())

                deleteRule(rulesList.firstOrNull()?.id.orEmpty(), keyword)

            } else {

                fetchFilteredStream(keyword)

            }

        }
    }


    val readRandomString = tweetsRepositoryImpl.getRandomData()

}