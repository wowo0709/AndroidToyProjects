package kr.co.hanbit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.hanbit.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 커스텀 뷰 생성
        val customView = CustomView("커스텀 뷰 만들기", this)
        // 레이아웃.addview() 로 소스 코드에서 생성한 뷰를 레이아웃에 삽입
        binding.frameLayout.addView(customView)
    }
}

// View 를 상속받는 클래스
// 생성자 호출 시 문자열 지정이 가능하도록 파라미터 추가
class CustomView(text: String, context: Context): View(context){
    // init 블록에서 텍스트 초기화
    val text: String
    init{
        this.text = text
    }
    // onDraw 메서드 오버라이딩
    override fun onDraw(canvas: Canvas?){
        super.onDraw(canvas)

        // 텍스트를 출력하는 drawText() 메서드는 출력할 문자열, 가로세로 좌표,
        // 글자 색상이나 두께 정보 등을 가지고 있는 Paint 인스턴스가 필요
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 100f // float

        // drawText(출력할 문자열, x좌표, y좌표, Paint 인스턴스)
        // (x, y) 좌표는 뷰의 좌측 하단 기준
        canvas?.drawText(text, 0f, 100f, paint)

        val cyan = Paint()
        cyan.style = Paint.Style.FILL
        cyan.color = Color.CYAN

        val rect = RectF(300f, 450f, 500f, 650f)

        canvas?.drawRoundRect(rect, 50f, 50f, cyan)

    }
}