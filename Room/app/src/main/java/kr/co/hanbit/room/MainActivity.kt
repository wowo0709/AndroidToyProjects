// 2-1. RoomMemo 클래스 생성하기: SQLite에서 kt와 xml 파일 복사하고 패키지명 수정
package kr.co.hanbit.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kr.co.hanbit.room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    // SqliteHelper 인스턴스를 생성
    // 6-1. SqliteHelper를 RoomHelper로 변경
    // var helper = SqliteHelper(this, "memo", 1)
    var helper: RoomHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 6-2. RoomHelper 생성
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_memo")
            .allowMainThreadQueries().build()

        // 리사이클러 뷰 어댑터 생성
        val adapter = RecyclerAdapter()
        // 삭제 기능 추가: 2-2. 어댑터에 SqliteHelper 전달
        adapter.helper = helper

        // 어댑터의 listData에 데이터베이스에서 가져온 데이터 세팅
        // 6-3. RoomHelper를 사용하도록 수정
        // adapter.listData.addAll(helper.selectMemo())
        adapter.listData.addAll(helper?.roomMemoDAO()?.getAll()?:listOf())

        // 레이아웃의 리사이클러뷰 위젯에 어댑터를 연결하고 레이아웃 매니저 설정
        binding.recyclerMemo.adapter = adapter
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)

        // 6-3. RoomHelper를 사용하도록 수정
        // 저장 버튼에 클릭 리스너 달기
        binding.btnSave.setOnClickListener {
            // 플레인 텍스트에 입력된 내용이 있으면,
            if(binding.editMemo.text.toString().isNotEmpty()) {
                val memo = RoomMemo(binding.editMemo.text.toString(), System.currentTimeMillis())
                helper?.roomMemoDAO()?.insert(memo)
                adapter.listData.clear()
                adapter.listData.addAll(helper?.roomMemoDAO()?.getAll()?:listOf())
//                // 데이터베이스에 INSERT
//                val memo = RoomMemo(null, binding.editMemo.text.toString(), System.currentTimeMillis())
//                helper.insertMemo(memo)
//                // 데이터베이스 데이터가 변하면 리사이클러뷰에 업데이트
//                adapter.listData.clear()
//                adapter.listData.addAll(helper.selectMemo())
                adapter.notifyDataSetChanged()
                // 플레인 텍스트 초기화
                binding.editMemo.setText("")
            }
        }


    }
}