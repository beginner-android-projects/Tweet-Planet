package com.hsmsample.tweetplanet.tweets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hsmsample.tweetplanet.R
import com.hsmsample.tweetplanet.databinding.FragmentTweetsMapsBinding
import com.hsmsample.tweetplanet.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class TweetsMapsFragment: Fragment(), OnMapReadyCallback{

    private lateinit var dataBinding: FragmentTweetsMapsBinding

    private lateinit var mapView: MapView

    private val viewModel by viewModels<TweetsViewModel>()

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
        mapView.onCreate(savedInstanceState) // This is a super important thing to be added! Please mention it in the documentation!
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)))

        context?.showLongToast(viewModel.readRandomString)
    }

    private fun setupEditTextListener() {
        dataBinding.tietSearchField.addTextChangedListener { editable ->
            viewModel.setSearchQuery(editable.toString())
        }
    }

    private fun setupVMObservers() {
        lifecycleScope.launch {
            viewModel.observeSearchChanges()
        }
    }


}