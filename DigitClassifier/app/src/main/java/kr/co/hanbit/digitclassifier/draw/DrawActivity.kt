package kr.co.hanbit.digitclassifier.draw

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.co.hanbit.digitclassifier.Classifier
import kr.co.hanbit.digitclassifier.R
import kr.co.hanbit.digitclassifier.databinding.ActivityDrawBinding
import java.io.IOException
import java.util.*

class DrawActivity : AppCompatActivity() {

    // 바인딩 연결
    val binding by lazy {ActivityDrawBinding.inflate(layoutInflater)}
    // classifier 선언
    lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // drawView 초기화
        var drawView = binding.drawView
        drawView.setStrokeWidth(100.0f)
        drawView.setBackgroundColor(Color.BLACK)
        drawView.setColor(Color.WHITE)

        // 결과를 보여줄 텍스트 뷰
        var resultView = binding.resultView

        // 버튼 클릭 리스너 설정
        binding.classifyBtn.setOnClickListener {
            var image = drawView.getBitmap()

            // 추론 메서드를 호출하고 결과를 전달받아 resultView에 출력
            val res = classifier.classify(image)
            val outStr = String.format(Locale.ENGLISH, "%d: %.0f%%",
                res.first, res.second*100.0f)
            resultView.text = outStr
        }
        binding.clearBtn.setOnClickListener {
            drawView.clearCanvas()
        }

        // Classifier 인스턴스를 생성하고 예외 처리까지
        // 이제 DrawActivity가 생성될 때 Classifier도 생성되고 초기화 됨
        try {
            classifier = Classifier(this)
            // classifier = Classifier(this)
        }catch(ioe: IOException){
            Log.d("DigitClassifier", "failed to init Classifier", ioe)
        }

    }
    // 액티비티 종료 시 호출되는 onDestroy 메서드 오버라이드
    override fun onDestroy() {
        // Classifier의 finish() 메서드를 호출하여 액티비티 종료 시 자원 해제제
        classifier.finish()

        super.onDestroy()
    }
}