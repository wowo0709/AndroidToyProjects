package kr.co.hanbit.viewpagerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.hanbit.viewpagerview.databinding.ItemViewpagerBinding

/*리사이클러 뷰를 사용하는 방법과 같습니다. 뷰페이저에 리사이클러뷰 어댑터를 사용하면
기존에 세로로 출력되는 것을 가로로 출력되도록 해준다고 생각하면 이해하기 쉽습니다.
* */

// 뷰 어댑터
class CustomPagerAdapter: RecyclerView.Adapter<Holder>() {

    // 어댑터에서 사용할 목록 변수
    // MainActivity에서 어댑터를 생성한 후 textList 변수로 각각의 페이지에서
    // 보여줄 텍스트를 전달
    var textList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 바인딩을 생성한 후 Holder에 전달
        val binding = ItemViewpagerBinding.inflate(LayoutInflater.from(parent.context),
                                                    parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Holder에 만들어둔 setText 메서드를 호출하여 화면에 출력
        val text = textList[position]
        holder.setText(text)
    }

    override fun getItemCount(): Int {
        return textList.size
    }
}

// 뷰 홀더 클래스
class Holder(val binding: ItemViewpagerBinding): RecyclerView.ViewHolder(binding.root){
    // item_viewpager 레이아웃의 미리 만들어둔 텍스트뷰에 깂을 입력하는 코드
    fun setText(text: String){
        binding.textView.text = text
    }
}