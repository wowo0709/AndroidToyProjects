package kr.co.hanbit.activitypractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kr.co.hanbit.activitypractice.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    val binding by lazy {ActivitySubBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 텍스트뷰에 인텐트에 담겨온 값 입력
        binding.to1.text = intent.getStringExtra("from1") // 문자열 값을 꺼낼 때는 getStringExtra()
        binding.to2.text = "${intent.getIntExtra("from2", 0)}"

        // 서브 액티비티 종료 시 자신을 호출했던 메인 액티비티로 값을 돌려주기
        binding.btnClose.setOnClickListener {
            // 메인 액티비티에 돌려줄 인텐트 인스턴스
            // 돌려줄 때는 대상을 지정 안해도 됨
            val returnIntent = Intent()
            // 돌려줄 인텐트에 값 전달
            returnIntent.putExtra("returnValue", binding.editMessage.text.toString())
            // setResult(상태 값, 인텐트) 메서드 실행 시 자신을 호출한 액티비티로 인텐트 전달
            setResult(RESULT_OK, returnIntent)
            // 액티비티 종료
            finish()
        }
    }


}