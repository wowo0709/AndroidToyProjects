package kr.co.hanbit.widgetsprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.hanbit.widgetsprogressbar.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        Thread.sleep(3000)
//        showProgress(false)
        thread(start=true){
            Thread.sleep(3000)
            // showProgress(false) 이대로 실행하면 앱이 죽음
            runOnUiThread{
                showProgress(false)
            }
        }
    }

    fun showProgress(show: Boolean){
//        if(show){
//            binding.progressBar.visibility = View.VISIBLE
//        }
//        else{
//            binding.progressBar.visibility = View.GONE
//        }
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}