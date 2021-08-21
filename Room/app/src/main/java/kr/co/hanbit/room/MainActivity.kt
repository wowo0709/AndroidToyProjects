// 2-1. RoomMemo 클래스 생성하기: SQLite에서 kt와 xml 파일 복사하고 패키지명 수정
package kr.co.hanbit.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kr.co.hanbit.room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    // SqliteHelper 인스턴스를 생성
    // 6-1. SqliteHelper를 RoomHelper로 변경
    // var helper = SqliteHelper(this, "memo", 1)
    var helper: RoomHelper? = null

    // 7-4. 메모 수정하기: 수정할 데이터를 임시 저장할 프로퍼티 생성
    var updateMemo: RoomMemo? = null

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

        // 7-5. 메모 수정하기: 수정을 위해 어댑터에 메인액티비티 연결
        adapter.mainActivity = this

        // 레이아웃의 리사이클러뷰 위젯에 어댑터를 연결하고 레이아웃 매니저 설정
        binding.recyclerMemo.adapter = adapter
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)

        // 6-3. RoomHelper를 사용하도록 수정
        // 저장 버튼에 클릭 리스너 달기
        binding.btnSave.setOnClickListener {
            // 7-6. 메모 수정하기: 수정 체크 추가
            if (updateMemo != null){
                updateMemo?.content = binding.editMemo.text.toString()
                helper?.roomMemoDAO()?.update(updateMemo!!)
                refreshAdapter(adapter)
                cancelUpdate() // 수정 완료 후 원상태로 복귀
            }
            // 플레인 텍스트에 입력된 내용이 있으면,
            else if(binding.editMemo.text.toString().isNotEmpty()) {
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

        // 7-7. 메모 수정하기: 수정 취소 버튼 클릭 리스너 달기
        binding.btnCancel.setOnClickListener {
            cancelUpdate()
        }

    }

    // 7-8. 메모 수정하기: 수정 작업을 위한 세팅
    fun setUpdate(memo: RoomMemo){
        updateMemo = memo // 수정할 메모 임시 저장

        binding.editMemo.setText(updateMemo!!.content) // 플레인 텍스트 내용을 수정할 텍스트로 설정
        binding.btnCancel.visibility = View.VISIBLE    // 숨겨둔 수정취소 버튼 가시화
        binding.btnSave.text = "수정"                   // 원래 '저장' 이었던 문자열을 '수정' 으로 변경
    }


    // 7-9. 메모 수정하기: 수정을 취소(원상태로 복귀)
    fun cancelUpdate(){
        updateMemo = null

        binding.editMemo.setText("")              // 플레인 텍스트 초기화
        binding.btnCancel.visibility = View.GONE  // 수정취소 버튼 비가시화
        binding.btnSave.text = "저장"              // '수정' 이었던 문자열을 다시 '저장' 으로 변경
    }


    // 7-10. 메모 수정하기: 수정 내용을 리사이클러 뷰 목록에 반영
    fun refreshAdapter(adapter: RecyclerAdapter){
        adapter.listData.clear()
        adapter.listData.addAll(helper?.roomMemoDAO()?.getAll() ?: mutableListOf())
        adapter.notifyDataSetChanged()
    }

}