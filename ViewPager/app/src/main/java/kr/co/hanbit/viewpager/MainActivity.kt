package kr.co.hanbit.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.hanbit.viewpager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 프래그먼트 목록 생성
        val fragmentList = listOf(FragmentA(), FragmentB(), FragmentC(), FragmentD())

        // 어댑터를 생성하고 앞에서 생성해둔 프래그먼트 목록 저장
        val adapter = FragmentAdapter(this)
        adapter.fragmentList = fragmentList

        // 레이아웃의 viewPager에 어댑터 연결
        binding.viewPager.adapter = adapter

        // 탭레이아웃에 사용할 메뉴명
        val tabTitles = listOf<String>("A", "B", "C", "D")

        // TabLayoutMediator를 사용해서 TabLayout과 ViewPager 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            // tab 파라미터의 text 속성에 앞에서 정의해둔 메뉴명 입력
            tab.text = tabTitles[position]
        }.attach() // attach() 메서드를 호출하여 적용

    }
}