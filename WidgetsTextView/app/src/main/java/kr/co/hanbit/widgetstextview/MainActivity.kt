package kr.co.hanbit.widgetstextview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.co.hanbit.widgetstextview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.editText.addTextChangedListener{
            Log.d("EditText", "현재 입력된 값 = ${}")
        }
    }
}