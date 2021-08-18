package kr.co.hanbit.customtext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

// 버전 호환성을 위해 기본 위젯인 TextView가 아니라 AppCompatTextView를 상속
class CustomText: AppCompatTextView {

    // delimeter와 입력된 값을 조합해서 처리
    fun process(delimeter: String){
        // 텍스트 자르기
        var one = text.substring(0, 4)
        var two = text.substring(4, 6)
        var three = text.substring(6)

        setText("$one $delimeter $two $delimeter $three")
    }

    // 소스코드에서 사용할 때 호출
    constructor(context: Context): super(context){

    }
    // 레이아웃에서 사용할 때 호출
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        // res/values/attrs.xml에 정의된 어트리뷰트를 가져온다.
        val typed = context.obtainStyledAttributes(attrs, R.styleable.CustomText)
        val size = typed.indexCount

        for (i in 0 until size){
            when(typed.getIndex(i)){
                // 현재 속성을 확인하고 delimeter와 같으면
                R.styleable.CustomText_delimeter->{
                    // XML에 입력된 delimeter 값을 꺼내고
                    val delimeter = typed.getString(typed.getIndex(i)) ?: "-"
                    // 꺼낸 값을 process 메서드에 전달하여 호출한다.
                    process(delimeter)
                }
            }
        }
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

    }


}
