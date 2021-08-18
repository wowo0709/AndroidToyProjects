package kr.co.hanbit.miniquiz_4_2_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.hanbit.miniquiz_4_2_3.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var value: Int = 0
        while (true){
            thread(start=true) {
                Thread.sleep(1000)
                runOnUiThread {
                    progress(value = value)
                }
            }
            if (binding.textView.text == "10"){
                progress(false,0)
                break
            }

            value += 1
        }
    }

    fun progress(run: Boolean = true, value: Int){
        if(run) binding.textView.text = "${value}"
        else binding.progressBar.visibility = View.GONE
    }
}