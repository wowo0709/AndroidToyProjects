package kr.co.hanbit.containerrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.co.hanbit.containerrecyclerview.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 어댑터가 정상적으로 동작하려면 미리 정의된 Holder 클래스를 제네릭으로 지정한 후
// 어댑터에 설계되어 있는 3개의 인터페이스를 구현해야 한다.
class CustomAdapter: RecyclerView.Adapter<Holder>() {

    // 어댑터에서 사용할 데이터 목록 변수 선언
    // 앞에서 작성해둔 loadData() 메서드에서 리턴해주는 값을 사용할 것이기 때문에
    // mutableListOf<Memo> 를 사용. 데이터가 담기는 listData 변수에는 나중에
    // 메인 액티비티에서 직접 호출해서 값을 넣는다.
    var listData = mutableListOf<Memo>()

    // 스마트폰의 한 화면에 그려지는 아이템 개수만큼 아이템 레이아웃 생성
    // 안드로이드는 뷰홀더(Holder) 클래스를 메모리에 저장했다가 요청이 있을 때마다
    // 메서드를 실행하여 꺼내서 사용한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 어댑터에서 사용하는 바인딩
        // 파라미터: LayoutInflater.from(parent.content), parent, false
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),
                                                    parent, false)
        return Holder(binding)
    }
    // 생성된 아이템 레이아웃에 값 입력 후 목록에 출력 (생성된 뷰홀더를 출력)
    override fun onBindViewHolder(holder: Holder, position: Int) {
       val memo = listData.get(position)
        holder.setMemo(memo)
    }
    // 목록에 보여줄 아이템 개수
    override fun getItemCount(): Int {
       return listData.size
    }
}

/*아이템 레이아웃은 ViewHolder 자체에서 만들지 않고 어댑터가 만들어서 넘겨주므로
* 다음과 같이 어댑터에서 넘겨주는 바인딩을 Holder 클래스의 생성자에서 받아
* ViewHolder 생성자에게 넘겨준다. 이 때 ViewHolder 의 생성자는 바인딩이 아닌
* View를 필요로 하기 때문에 binding.root를 넘겨준다.
* 또한 binding은 Holder 클래스 안에서 전역변수(프로퍼티)로 사용해야 하므로
* val 키워드를 앞에 붙여준다.
* */
/*뷰홀더가 사용하는 바인딩은 어댑터에서 생성한 후에 넘겨주는데, 이 어댑터에서
사용할 레이아웃의 이름이 item_recycler 이기 때문에 바인딩의 이름이
ItemRecyclerBinding이 됩니다.
* */
class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
    // init 초기화 블록
    init{
        binding.root.setOnClickListener {
            Toast.makeText(binding.root.context, "클릭된 아이템 = ${binding.textTitle.text}",
                            Toast.LENGTH_SHORT).show()
        }
    }

    // 화면에 데이터를 세팅하는 메서드 구현
    fun setMemo(memo: Memo){
        binding.textNo.text = "${memo.no}"

        binding.textTitle.text = memo.title

        // java.text의 SimpleDataFormat을 사용하여 날짜형식으로 변환
        var sdf = SimpleDateFormat("yyyy/MM/dd")
        // 날짜 형식을 사용하고 싶을 때는 Long 형태의 데이터 타입을
        // SimpleDataFormat 인스턴스의 format 메서드로 변환한다.
        var formattedDate = sdf.format(memo.timeStamp)
        binding.textDate.text = formattedDate
    }
}