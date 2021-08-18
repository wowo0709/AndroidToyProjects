package kr.co.hanbit.sayhello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.hanbit.sayhello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        // setContentView(R.layout.activity_main)
        setContentView(binding.root)
        // R.layout.activity_main을 사용해도 화면에는 동일하게 나타나지만, 뷰 바인딩을
        // 사용하기 위해 필요한 과정

        // '리스너'는 클릭 시 수행될 코드를 가리킴
        binding.btnSay.setOnClickListener{
            binding.textSay.text = "Hello Kotlin!!!"
        }
    }
}