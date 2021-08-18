package kr.co.hanbit.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.co.hanbit.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 버튼 리스너 -> 권한 확인
        binding.btnCamera.setOnClickListener {
            checkPermission()
        }

    }

    // 1단계: 권한에 대한 사용자 승인 확인
    fun checkPermission(){
        // 카메라의 권한의 승인 상태 가져오기
        // 권한은 모두 Manifest(android) 클래스에 문자열 상수로 정의되어 있음
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        // 승인 상태인지 아닌 지에 따라 분기
        if (cameraPermission == PackageManager.PERMISSION_GRANTED){
            // 승인이면 프로그램 진행
            startProcess()      // 사용자 정의 함수
        }
        else{
            // 미승인이면 권한 요청
            requestPermission() // 사용자 정의 함수
        }
    }

    // 승인 상태일 때 호출.
    fun startProcess(){
        Toast.makeText(this, "카메라를 실행합니다.", Toast.LENGTH_LONG).show()
    }

    // 2단계: 사용자에게 승인 요청

    // 미승인 시 권한 요청
    fun requestPermission(){
        // 미승인 권한을 사용자에게 요청 -> 팝업창 표시
        // 파라미터: 액티비티, 요청권한 배열, 리퀘스트 코드
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 99)
    }

    // 3단계: 사용자 승인 후 처리

    override fun onRequestPermissionsResult(
        requestCode: Int,               // 요청한 주체를 확인(requestPermission()의 세번째 파라미터)
        permissions: Array<out String>, // 요청한 권한 목록(requestPermission()의 두번째 파라미터)
        grantResults: IntArray          // 권한 목록에 대한 승인/미승인 값.
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // requestCode 확인
        when(requestCode){
            99 -> {
                // 권한 결괏값을 체크해서 승인 여부를 체크하고, 승인이면 startProcess() 메서드를 호출하고 미승인이면 앱을 종료
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startProcess()
                }
                else{
                    finish()
                }
            }
        }
    }
}