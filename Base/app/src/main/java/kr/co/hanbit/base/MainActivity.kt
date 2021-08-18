package kr.co.hanbit.base

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kr.co.hanbit.base.databinding.ActivityMainBinding

// class MainActivity : AppCompatActivity() {
class MainActivity: BaseActivity(){

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 버튼 리스너 -> 권한 요청 메서드 호출
        binding.btnCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }
    }

    // 추상 메서드 구현
    override fun permissionGranted(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 99)
    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(baseContext, "권한 거부됨", Toast.LENGTH_LONG).show()
    }

    // 호출한 액티비티에서 보내는 인텐트를 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 10){
            if(resultCode == RESULT_OK) {
                Log.d("카메라", "촬영 성공")
            }
            else{
                Log.d("카메라", "촬영 실패")
            }
        }
    }
}