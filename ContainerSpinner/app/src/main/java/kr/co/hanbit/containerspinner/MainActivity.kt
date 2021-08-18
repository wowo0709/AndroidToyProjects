package kr.co.hanbit.containerspinner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kr.co.hanbit.containerspinner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 스피너에 입력될 가상의 데이터 작성
        var data = listOf("-- 선택하세요 --", "1월", "2월", "3월", "4월", "5월", "6월")

        // 데이터와 스피너를 연결해줄 어댑터 생성
        // 데이터 타입은 제네릭으로 지정.
        // 파라미터: 컨텍스트, 스피너의 목록 하나하나가 그려질 레이아웃, 데이터
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)

        // 어댑터를 스피너에 연결. 스피너의 어댑터 프로퍼티에 연결할 어댑터 인스턴스 저장.
        binding.spinner.adapter = adapter

        // 스피너 리스너 연결
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int, // 선택된 아이템 순서
                id: Long
            ) {
                // 대부분 세번째 파라미터인 position만 사용
                // 데이터는 스피너에서 가져오는 것이 아니라 전달한 데이터에서 직접 가져옴
                binding.result.text = data.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }
}