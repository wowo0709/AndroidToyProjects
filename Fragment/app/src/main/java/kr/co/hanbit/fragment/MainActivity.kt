package kr.co.hanbit.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.hanbit.fragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 메인 액티비티 바인딩 연결
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    // 프래그먼트 전역변수 선언
    lateinit var listFragment: ListFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩된 뷰 전달
        setContentView(binding.root)

        setFragment()

        // 버튼 클릭 시 listFragment의 setValue 호출
        binding.btnSend.setOnClickListener {
            listFragment.setValue("From Activity")
        }
    }



    // 액티비티에 프래그먼트를 삽입하는 메서드
    fun setFragment(){
        /*액티비티에 프래그먼트를 삽입하기 위해서는 프래그먼트 매니저를 통해 삽입할 레이아웃의 id를 지정합니다.
        * 프래그먼트를 삽입하는 과정은 하나의 트랜잭션으로 관리되기 때문에 트랜잭션 매니저를 통해
        * begin transaction -> add fragment -> commit transaction의 순서로 처리된다.
        * */
        // val listFragment: ListFragment = ListFragment() -> 전역변수로 만들기
        listFragment = ListFragment()

        // 번들을 하나 생성하고 전달할 값을 담는다. (인텐트와 동일)
        var bundle = Bundle()
        bundle.putString("key1", "List Fragment")
        bundle.putInt("key2", 20210101)
        // 값이 담긴 번들을 프래그먼트의 arguments에 담는다.
        listFragment.arguments = bundle

        // 액티비티가 가지고 있는 프래그먼트 매니저를 통해 트랜잭션을 시작하고, 시작한 트랜잭션울 변수에 저장해둡니다.
        val transaction = supportFragmentManager.beginTransaction()
        // 트랜잭션의 add() 메서드로 frameLayout을 id로 가지고 있는 레이아웃에 앞에서 생성한 listFragment를 삽입합니다.
        transaction.add(R.id.frameLayout, listFragment)
        // commit() 메서드로 모든 작업이 정상적으로 처리되었음을 트랜잭션에 알려주면 작업이 반영됩니다.
        transaction.commit()
    }



    // DetailFragment를 생성해서 메인 액티비티의 frameLayout에 삽입 -> 화면 전환
    // 버튼은 프래그먼트에 있지만, 프래그먼트를 메인 액티비티에서 생성하고 프래그먼트를 담는 레이아웃도 메인 액티비티에 있으므로
    // 화면 전환 코드는 메인 액티비티에 작성함.
    fun goDetail(){
        // 프래그먼트 인스턴스 저장
        val detailFragment = DetailFragment()
        // 프래그먼트를 액티비티에 삽입
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, detailFragment)
        // addToBackStack을 사용하지 않은 채로 뒤로가기를 하면 액티비티가 종료된다.
        // addToBackStack을 사용하면 프래그먼트를 백스택에 담아둘 수 있다. 따라서 스마트폰의 뒤로가기 버튼으로
        // 트랜잭션 전체를 마치 액티비티처럼 제거할 수 있게 된다. 다만, 개별 프래그먼트가 아닌 트랜잭션 전체가
        // 담기기 때문에 add나 replace와 상관없이 해당 트랜잭션 전체가 제거된다.
        transaction.addToBackStack("detail")
        transaction.commit()
    }



    // 뒤로가기 함수
    fun goBack(){
        // 뒤로가기가 필요할 때 액티비티에서 사용할 수 있는 기본 메서드
        onBackPressed()
    }
}