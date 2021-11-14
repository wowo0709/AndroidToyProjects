package kr.co.hanbit.seoulpubliclibraries

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kr.co.hanbit.seoulpubliclibraries.data.Library
import kr.co.hanbit.seoulpubliclibraries.databinding.ActivityMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 6. 기본으로 지정되어 있는 코드를 삭제하고 loadLibraries() 메서드 호출
        loadLibraries()
        // 7-2. 지도에 마커 클릭 리스너 설정: 도서관 홈페이지가 있으면 홈페이지로 이동
        mMap.setOnMarkerClickListener {
            if (it.tag != null){
                var url = it.tag as String
                if (!url.startsWith("http")){
                    url = "http://${url}"
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            true
        }
    }

    // 4-1. 데이터 불러오기 메서드 정의
    fun loadLibraries(){
        // 4-2. 도메인 주소와 JSON 컨버터를 설정해서 레트로핏 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .addConverterFactory((GsonConverterFactory.create()))
            .build()
        // 4-3. 서비스 객체 생성
        val seoulOpenService = retrofit.create(SeoulOpenService::class.java)
        // 4-4. API_KEY를 입력하고 enqueue() 메서드로 서버에 요청
        seoulOpenService
            .getLibrary(SeoulOpenApi.API_KEY)
            .enqueue(object: Callback<Library> {
                // 4-5 ~ 4-7. 2개의 필수 메서드 정의
                override fun onResponse(call: Call<Library>, response: Response<Library>) {
                    showLibraries(response.body() as Library)
                }

                override fun onFailure(call: Call<Library>, t: Throwable) {
                    Toast.makeText(baseContext, "서버에서 데이터를 가져올 수 없습니다.",
                                    Toast.LENGTH_LONG).show()
                }
            })
    }

    // 5-1. 지도에 마커를 표시하는 메서드 정의
    fun showLibraries(libraries: Library){
        /* 7. 도서관 이름 클릭 시 홈페이지로 이동하기 */
        // 5-6. 마커의 영역을 저장하는 변수 생성
        // 카메라 위치를 수동으로 지정해주기 보다 마커가 있는 전체 영역을 자동으로 보여주도록 설정
        val latLngBounds = LatLngBounds.Builder()
        // 5-2. 전달받은 정보를 for문으로 하나씩 처리
        for (lib in libraries.SeoulPublicLibraryInfo.row){
            // 5-3. 마커 좌표 생성
            val position = LatLng(lib.XCNTS.toDouble(), lib.YDNTS.toDouble())
            // 5-4. 좌표와 도서관 이름으로 마커를 생성
            val marker = MarkerOptions().position(position).title(lib.LBRRY_NAME)
//            // 5-5. 마커를 지도에 추가
//            mMap.addMarker(marker)
            // 7-1. 마커에 tag 정보를 추가하고 tag 값에는 홈페이지 주소 저장
            val obj = mMap.addMarker(marker)
            obj?.tag = lib.HMPG_URL
            // 5-7. latLngBounds 변수에도 동일한 마커 추가
            latLngBounds.include(marker.position)
        }
        // 5-8. 저장해둔 마커의 영역 반환
        val bounds = latLngBounds.build()
        val padding = 0
        // 5-9. bounds와 padding으로 카메라 업데이트
        val updated = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        // 5-10. 업데이트된 카메라를 지도에 반영
        mMap.moveCamera(updated)

    }
}