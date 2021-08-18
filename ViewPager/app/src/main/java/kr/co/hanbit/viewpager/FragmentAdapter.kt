package kr.co.hanbit.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// 뷰 페이저와 연결하기 위한 프래그먼트 어댑터
class FragmentAdapter(fragmentActivity: FragmentActivity) :
                      FragmentStateAdapter(fragmentActivity) {

    // 리사이클러뷰 어댑터처럼 페이저어댑터도 화면에 표시할 아이템의 목록 필요
    // 뷰 페이저의 화면 아이템은 대부분 중간에 개수가 늘거나 줄지 않고
    // 처음 정해진 개수대로 사용하기 때문에 listOf 메서드가 효율적
    var fragmentList = listOf<Fragment>()

    // 어댑터가 화면에 보여줄 전체 프래그먼트의 개수를 반환해야 합니다.
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // 현재 페이지의 position이 파라미터로 넘어옵니다. position에 해당하는
    // 위치의 프래그먼트를 만들어서 안드로이드에 반환해야 합니다.
    override fun createFragment(position: Int): Fragment {
        // 페이지가 요청될 때 getItem으로 요청되는 페이지의 position 값이 넘어옴.
        // position 값을 이용해서 프래그먼트 목록에서 해당 position에 있는 프래그먼트
        // 1개를 리턴함.
        return fragmentList.get(position)
    }

}