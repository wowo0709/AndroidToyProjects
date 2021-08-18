package kr.co.hanbit.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.hanbit.fragment.databinding.FragmentListBinding

/*주석 처리되어 있는 부분은 현재 실습에서 사용하지 않는 코드입니다. */
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    // 메인 액티비티에 작성된 goDetail 메서드를 호출해야 하므로 MainActivity를 먼저 전달받아야 한다.
    // MainActivity를 담아둘 멤버 변수 선언
    var mainActivity: MainActivity? = null

    //    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    // 프래그먼트 바인딩을 전역변수로 만들기
    lateinit var binding:FragmentListBinding


    // 액티비티가 프래그먼트를 요청하면 onCreateView() 메서드를 통해 뷰를 만들어서 보여줌(리사이클러뷰의 onCreateViewHolder 메서드와 유사)
    // 파라미터 1: 레이아웃 파일을 로드하기 위한 레이아웃 인플레이터를 기본 제공
    // 파라미터 2: 프래그먼트 레이아웃이 배치되는 부모 레이아웃 (액티비티의 레이아웃)
    // 상태값 저장을 위한 보조 도구. 액티비티의 onCreate의 파라미터와 동일.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // 리사이클러뷰에서와 동일하게 동작
        /*원본 코드: inflater로 생성한 뷰를 바로 리턴*/
        // return inflater.inflate(R.layout.fragment_list, container, false)
        /*수정 코드: 바인딩으로 생성한 후 레이아웃에 있는 btnNext 버튼에 리스너를 등록한 후에 binding.root 리턴*/
        // val binding = FragmentListBinding.inflate(inflater, container, false) -> 전역변수로 만들기
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.btnNext.setOnClickListener { mainActivity?.goDetail() }

        // arguments에서 값을 꺼내고 텍스트뷰에 입력하기
        binding.textTitle.text = arguments?.getString("key1")
        binding.textValue.text = "${arguments?.getInt("key2")}"

        return binding.root // 바인딩이 가지고 있는 root view를 반환
    }

    /*메서드를 오버라이드 할 때는 onCreate() 메서드 아래에 한다!!*/
    // MainActivity 전달 받기
    override fun onAttach(context: Context) { // Context에는 부모 액티비티 전체가 담겨있음
        super.onAttach(context)
        // context의 타입이 MainActivity인 것을 확인하고 프로퍼티에 저장
        if (context is MainActivity) mainActivity = context
    }

    // 액티비티로부터 전달받을 문자열을 출력
    fun setValue(value: String){
        binding.textFromActivity.text = value
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ListFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}