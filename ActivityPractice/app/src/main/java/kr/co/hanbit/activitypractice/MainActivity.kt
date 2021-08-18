package kr.co.hanbit.activitypractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.co.hanbit.activitypractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 인텐트 생성
        // ::class.java 라고 정확하게 입력, 인텐트 사용 규칙
        val intent = Intent(this, SubActivity::class.java)

        // 인텐트에 값 전달
        intent.putExtra("from1", "Hello Bundle")
        intent.putExtra("from2", 2021)

        // 액티비티 호출 -> startActivity() 메서드는 호출한 액티비티에서 값을 받을 수 없음
        // binding.btnStart.setOnClickListener { startActivity(intent) }
        // 호출한 액티비티에서 값을 받고 싶을 때는 startActivityForResult() 메서드 사용
        // 두번째 파라미터인 requestCode는 메인 액티비티에서 서브 액티비티를 호출하는 버튼이 여러 개 있을 때
        // 어떤 버튼에서 호출된 것인지를 구분
        binding.btnStart.setOnClickListener { startActivityForResult(intent, 99) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // resultCode가 정상인지 체크
        if (resultCode == RESULT_OK){
            when(requestCode) {
                99 -> {
                    // 서브 액티비티에서 보낸 정보 받기
                    val message = data?.getStringExtra("returnValue")
                    // 화면에 토스트로 보여주기. show()를 호출해야 화면에 나타남
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}