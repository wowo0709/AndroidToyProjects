package kr.co.hanbit.googlemaps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.co.hanbit.googlemaps.databinding.ActivityMapsBinding

//class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // 위치값 사용
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // 위치값 요청에 대한 갱신 정보
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // id가 map인 SupportMapFragment를 찾은 후 getMapAsync()를 호출해서
//        // 안드로이드에 구글 지도를 그려달라는 요청
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        // 앱에서 사용할 위치 권한
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION)
        requirePermissions(permissions, 99)
    }

    override fun permissionGranted(requestCode: Int) {
        // 위치 권한 승인 시 호출
        startProcess()
    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(this,
                        "권한 승인이 필요합니다.",
                        Toast.LENGTH_LONG).show()
    }
    // 구글 지도 준비 작업
    fun startProcess(){
        // id가 map인 SupportMapFragment를 찾은 후 getMapAsync()를 호출해서
        // 안드로이드에 구글 지도를 그려달라는 요청
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    // 구글 지도가 준비되면 안드로이드가 OnMapReadyCallback 인터페이스의 onMapReady() 메서드를
    // 호출하면서 파라미터로 준비된 GoogleMap을 전달
    override fun onMapReady(googleMap: GoogleMap) {
        // 메서드 안에서 미리 선언된 mMap 프로퍼티에 GoogleMap을 저장해두면 액티비티 전체에서 맵을 사용할 수 있음
        mMap = googleMap
        // 위치 검색 클라이언트 생성
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // 위치 갱신
        updateLocation()


        /* 위치, 마커 표시
        // 위치(위도, 경도) 정보
        val LATLNG = LatLng(37.566418, 126.977943)

        // 카메라 포지션을 설정하며 CameraPosition 객체 생성
        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)
            .zoom(15.0f)
            .build()
        // 카메라 포지션에 지도에서 사용할 수 있는 카메라 정보 생성
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        // 변경된 카메라 포지션이 표시
        mMap.moveCamera(cameraUpdate)

        // 마커 옵션 객체
        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("Marker in Seoul City Hall")
            .snippet("37.566418, 126.977943")
        // 구글 지도에 마커 추가
        mMap.addMarker(markerOptions)
        */
    }

    // 위치 갱신 메서드
    @SuppressLint("MissingPermission")
    fun updateLocation(){
        // 위치 정보를 요청할 정확도와 주기를 설정
        val locationRequest = LocationRequest.create()
        locationRequest.run{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }
        // 해당 주기마다 위치값을 반환받을 콜백 설정
        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?){
                locationResult?.let{
                    for((i, location) in it.locations.withIndex()){
                        Log.d("Location", "$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }
        // 위치 검색 클라이언트가 위치 갱신 요청
        // 권한 처리가 필요한데 현재 코드에서는 확인 불가 -> 메서드 상단에 해당 코드를 체크하지 않아도 된다는 어노테이션 추가
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    // 위치 정보를 받아서 마커를 그리고 카메라을 이동
    fun setLastLocation(lastLocation: Location){
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("Here!")
        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)
            .zoom(15.0f)
            .build()
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}