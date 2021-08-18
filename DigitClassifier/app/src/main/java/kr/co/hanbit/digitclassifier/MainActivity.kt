package kr.co.hanbit.digitclassifier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.hanbit.digitclassifier.databinding.ActivityMainBinding
import kr.co.hanbit.digitclassifier.draw.DrawActivity

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.drawBtn.setOnClickListener {
            val intent = Intent(this, DrawActivity::class.java)
            startActivityForResult(intent, 99)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            when(requestCode){
                99 -> {
                    // TODO("Not Implemented Yet")
                }
            }
        }else{
            // TODO("Not Implemented Yet")
        }
    }
}