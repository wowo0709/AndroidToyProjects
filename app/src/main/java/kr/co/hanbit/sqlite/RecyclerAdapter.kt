package kr.co.hanbit.sqlite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.hanbit.sqlite.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 어댑터 클래스
class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    // 삭제 기능 추가 2-2: SqliteHelper 프로퍼티
    var helper: SqliteHelper? = null

    var listData = mutableListOf<Memo>()

    // 삭제 기능 추가: 3. 뷰 홀더 클래스 -> 데이터 삭제 시 어댑터의 helper와 listData 프로퍼티에 접근하기 위해 어댑터 클래스 안에 정의
    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        // 삭제 기능 추가: 5-1. setMemo 메서드로 넘어온 Memo 임시 저장
        var mMemo: Memo? = null

        // 삭제 기능 추가 4: init 초기화 블록 안에 클릭 리스너 생성
        init{
            binding.btnDelete.setOnClickListener {
                // 삭제 기능 추가 6: 데이터베이스와 리사이클러 뷰의 데이터 삭제
                // SQLite의 데이터를 먼저 삭제하고 listData 데이터를 삭제
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)
                notifyDataSetChanged()
            }
        }


        fun setMemo(memo: Memo){
            // 삭제 기능 추가: 5-2. setMemo 메서드로 넘어온 Memo 임시 저장
            this.mMemo = memo

            binding.textNo.text = "${memo.no}"
            binding.textContent.text = memo.content
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.textDatetime.text = "${sdf.format(memo.datetime)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

//// 뷰 홀더 클래스 -> 데이터 삭제 시 어댑터의 helper와 listData 프로퍼티에 접근하기 위해 어댑터 클래스 안에 정의
//class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
//    // init 초기화
//    init{
//        binding.btnDelete.setOnClickListener {
//
//        }
//    }
//
//
//    fun setMemo(memo: Memo){
//        binding.textNo.text = "${memo.no}"
//        binding.textContent.text = memo.content
//        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
//        binding.textDatetime.text = "${sdf.format(memo.datetime)}"
//    }
//}