package kr.co.hanbit.fragment_1_3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import kr.co.hanbit.fragment_1_3.databinding.FragmentReceiverBinding


class ReceiverFragment : Fragment() {

    // binding 선언
    lateinit var binding:FragmentReceiverBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 값을 보내는 측 프래그먼트에서 "request"라는 키로 값을 보내면 리스너 안의 코드가 실행됨.
        // 실제 값은 bundle 안에 Map의 형태로 들어있음.
        setFragmentResultListener("request"){key, bundle ->
            // 스코프 함수 let을 이용해 꺼낸 값이 있을 때만 화면의 textView에 값을 세팅
            // "request"는 요청 전체에 대한 키이고, bundle.getString에 입력되는 "valueKey"는 요청에
            // 담겨있는 여러 개 값 중 하나를 가리키는 키이다.
            bundle.getString("valueKey")?.let{
                binding.textView.text = it
            }
        }
    }
}