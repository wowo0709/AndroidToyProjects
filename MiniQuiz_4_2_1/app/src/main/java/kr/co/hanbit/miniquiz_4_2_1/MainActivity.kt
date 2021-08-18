package kr.co.hanbit.miniquiz_4_2_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.hanbit.miniquiz_4_2_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButtonApple -> binding.textView.text = "apple"
                R.id.radioButtonBanana -> binding.textView.text = "banana"
                R.id.radioButtonOrange -> binding.textView.text = "orange"

            }
        }
    }
}