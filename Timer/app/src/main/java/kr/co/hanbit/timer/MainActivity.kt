package kr.co.hanbit.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import kr.co.hanbit.timer.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 변수 선언
        var total = 0        // 전체 시간
        var started = false  // 시작 여부

        // 화면에 시간 값을 출력하는 Handler 정의
        val handler = object: Handler(Looper.getMainLooper()){
            // 스레드로부터 메시지를 수신하면 실행
            override fun handleMessage(msg: Message){
                val minute = String.format("%02d", total/60)
                val second = String.format("%02d", total%60)
                binding.textTimer.text = "$minute:$second"
            }
        }

        // 시작 코드 구현
        binding.btnStart.setOnClickListener {
            started = true
            thread(start=true){
                while(started){
                    Thread.sleep(1000)
                    if(started){
                        total += 1
                        handler?.sendEmptyMessage(0) // 핸들러에 메시지 전송
                    }
                }
            }
        }

        // 종료 코드 구현
        binding.btnStop.setOnClickListener {
            if(started){
                started = false
                total = 0
                binding.textTimer.text = "00:00"
            }
        }
    }
}