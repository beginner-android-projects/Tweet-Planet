package com.hsmsample.tweetplanet.tweets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.hsmsample.tweetplanet.R
import com.hsmsample.tweetplanet.data.remote.Result
import com.hsmsample.tweetplanet.databinding.FragmentTweetsMapsBinding
import com.hsmsample.tweetplanet.tweets.model.GeoGsonData
import com.hsmsample.tweetplanet.tweets.model.TweetData
import com.hsmsample.tweetplanet.utils.ERROR_MESSAGE
import com.hsmsample.tweetplanet.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
class TweetsMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var dataBinding: FragmentTweetsMapsBinding

    private lateinit var mapView: MapView

    private lateinit var googleMap: GoogleMap

    private val viewModel by viewModels<TweetsViewModel>()

    @Inject
    lateinit var gson: Gson

    //region Lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tweets_maps,
            container,
            false
        )

        setupMaps(savedInstanceState)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVMObservers()
        setupViewObservers()
        setupEditTextListener()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    //endregion


    private fun setupMaps(savedInstanceState: Bundle?) {
        mapView = dataBinding.mapView
        mapView.onCreate(savedInstanceState) // This is a super important thing to be added, Suggest mentioning it in the documentation!
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    private fun setupEditTextListener() {
        dataBinding.tietSearchField.addTextChangedListener { editable ->
            if (editable.toString().isNotEmpty())
                viewModel.setSearchQuery(editable.toString())
            else
                googleMap.clear()
        }
    }

    private fun setupVMObservers() {
        lifecycleScope.launch {

            viewModel.observeSearchChanges()

        }
    }

    private fun displayMarker(data: TweetData?) {
        data?.includes?.places?.let { listOfPlaces ->

            if (this::googleMap.isInitialized) {

                if (listOfPlaces.firstOrNull() != null) {

                    /*val geoGsonData =
                        gson.toJson(listOfPlaces.firstOrNull()?.geo ?: GeoGsonData())

                    val layer = GeoJsonLayer(googleMap, JSONObject(geoGsonData))*/

                    /***
                     * TweetData(data=Data(authorId=87947798, geo=Geo(placeId=07d9cff2c3c86002), id=1431925295091789825, text=Emirates), includes=Includes(places=[Place(fullName=Lebanese Grill Retaurant, geo=GeoGsonData(bbox=[55.34511026786179, 25.27484105721577, 55.34511026786179, 25.27484105721577], properties=com.hsmsample.tweetplanet.tweets.model.Properties@e16c8d2, type=Feature), id=07d9cff2c3c86002)], users=[User(id=87947798, name=Hussain Mukadam, username=HSM59)]), matchingRules=[MatchingRule(id=1431925243556270080, tag=keyword, value=null)])
                     */

                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                listOfPlaces.firstOrNull()?.geo?.bbox?.first()?: 0.0,
                                listOfPlaces.firstOrNull()?.geo?.bbox?.last()?: 0.0
                            )
                        )
                    )
                }
            }

        }
    }

    private fun setupViewObservers() {
        observeProgressDialog()

        observeErrorToast()

        viewModel.liveTweets.observe(viewLifecycleOwner, { tweetData ->

            Timber.d("--------------response in view $tweetData")

            tweetData?.let { displayMarker(it) }
        })
    }

    private fun observeProgressDialog() {
        // Change this and put it inside the XML (use dataBinding)
        viewModel.progressDialog.observe(viewLifecycleOwner, { flag ->
            dataBinding.progressBar.visibility = if (flag) View.VISIBLE else View.GONE
        })
    }

    private fun observeErrorToast() {
        viewModel.errorHandler.observe(viewLifecycleOwner, { message ->

            context?.showLongToast(message ?: getString(R.string.error_message))

        })
    }


}