package kr.co.hanbit.widgetimagebutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.hanbit.widgetimagebutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            // TODO("Not implemented yet")
        }
    }
}