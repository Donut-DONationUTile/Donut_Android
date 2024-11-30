package org.gdgoc.donut.ui.receive

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.gdgoc.donut.R
import org.gdgoc.donut.databinding.ActivityReceiveMapsBinding

class ReceiveMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityReceiveMapsBinding
    private val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var mainGoogleMap: GoogleMap
    var myLocationListener: LocationListener? = null
    private var myMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        binding = ActivityReceiveMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions(permissionList, 0)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            // 구글맵 객체 변수에 담아 사용
            mainGoogleMap = it
            // 확대 축소 기능
            it.uiSettings.isZoomControlsEnabled = true
            // 현재 위치 표시 기능
            it.isMyLocationEnabled = true
            // 현재 위치 표시하는 버튼 표시 여부
            it.uiSettings.isMyLocationButtonEnabled = false
            // 위치 정보 관리하는 객체 가져오기
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            val a1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val a2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {
                // 현재 저장되어 있는 위치 정보값 가져오기
                val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val location2 =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location1 != null) setMyLocation(location1)
                else if (location2 != null) setMyLocation(location2)

                getMyLocation()
            }
        }
    }

    private fun setMyLocation(location: Location) {
        // 위치 측정 중단
        if (myLocationListener != null) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(myLocationListener!!)
            myLocationListener = null
        }

        // 위도 & 경도 관리 객체
        val latLng = LatLng(location.latitude, location.longitude)
        // 지도를 이용시키기 위한 객체
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
        // 애니메이션 적용
        mainGoogleMap.animateCamera(cameraUpdate)
        // 현재 위치에 마커 표시
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        // 마커 이미지 변경
        val markerBitmap =
            BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)
        markerOptions.icon(markerBitmap)
        // 기존에 표시한 마커를 제거
        if (myMarker != null) {
            myMarker?.remove()
            myMarker = null
        }
        mainGoogleMap.addMarker(markerOptions)

        Toast.makeText(this, "위도 : ${location.latitude}, 경도 : ${location.longitude}", Toast.LENGTH_SHORT).show()
    }

    private fun getMyLocation() {
        val a1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val a2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            // 위치 측정 리스너
            myLocationListener = LocationListener { p0 -> setMyLocation(p0) }
            // 위치 측정 요청
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, myLocationListener!!)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mainGoogleMap = googleMap
    }
}
