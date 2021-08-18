package kr.co.hanbit.viewpagerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.hanbit.viewpagerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 어댑터에 전달할 목록 변수 생성
        val textList = listOf("View A", "View B", "View C", "View D")
        // 커스텀 어댑터 생성
        val customAdapter = CustomPagerAdapter()
        // 데이터 전달
        customAdapter.textList = textList
        // 뷰페이저와 어댑터 연결
        binding.viewPager.adapter = customAdapter

        // 탭의 메뉴명 생성
        val tabTitles = listOf("View A", "View B", "View C", "View D")

        // TabLayoutMediator를 사용해서 탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()


    }
}