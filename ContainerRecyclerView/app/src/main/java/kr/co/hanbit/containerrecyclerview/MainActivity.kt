package kr.co.hanbit.containerrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.containerrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 리사이클러 뷰에서 사용할 데이터 불러오기
        val data:MutableList<Memo> = loadData()
        // 어댑터를 생성하고 listData 프로퍼티에 출력할 데이터 전달
        var adapter = CustomAdapter()
        adapter.listData = data
        // 리사이클러 뷰의 adapter 프로퍼티에 연결할 어댑터 저장
        binding.recyclerView.adapter = adapter
        // 리사이클러 뷰를 화면에 보여주는 형태를 결정하는 레이아웃 매니저 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    // 목록의 아이템 클래스의 리스트를 반환하는 함수
    fun loadData(): MutableList<Memo>{
        // 리턴할 MutableList 컬렉션
        val data: MutableList<Memo> = mutableListOf()
        // 100개의 가상 데이터 만들기
        for (no in 1..100){
            val title = "이것이 안드로이드다 ${no}"
            val date = System.currentTimeMillis()
            // Memo 인스턴스 생성 후 반환값에 값 추가
            var memo = Memo(no, title, date)
            data.add(memo)
        }

        return data
    }
}