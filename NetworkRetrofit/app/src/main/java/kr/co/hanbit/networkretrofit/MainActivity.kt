package kr.co.hanbit.networkretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.networkretrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    // 4-1. 뷰바인딩 설정
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 4-4. 어댑터 설정하고 리사이클러 뷰 연결
        val adapter = CustomAdapter()
        binding.recyclerView.adapter = adapter
        // 4-5. 레이아웃 매니저 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // 4-6. 레트로핏 생성. 도메인 주소와 컨버터 입력.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 4-7. 요청 버튼 클릭 리스터 설정
        binding.btnRequest.setOnClickListener {
            // 4-8. 레트로핏의 create() 메서드에 앞에서 정의한 인터페이스를 넘기면 실행 가능한 서비스 객체 반환
            val githubService = retrofit.create(GithubService::class.java)
            // 4-9. 서비스 객체의 users() 메서드(인터페이스에서 직접 정의) 안에 생성된 enqueue() 메서드 호출
            // enqueue() 메서드는 비동기 통신으로 데이터를 가져온다.
            // 4-10. Github API 서버로부터 응답을 받으면 작동할 콜백 인터페이스를 파라미터로 구현
            githubService.users().enqueue(object: Callback<Repository>{
                // 4-11. 필수 메서드 구현
                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    // 4-12. response.body()로 전송된 데이터를 가져와서 Repository로 형변환 한 후 어댑터에 추가
                    adapter.userList = response.body() as Repository
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Repository>, t: Throwable) {

                }
            })
        }
    }
}

// 4-2. GithubService 인터페이스 정의
// 레트로핏 인터페이스는 호출 방식, 주소 데이터 등을 정의하고 Retrofit 라이브러리에서 인터페이스를 해석해 HTTP 통신 처리
interface GithubService{
    // 4-3. Github API를 호출할 메서드를 만들고 @GET 어노테이션을 사용해 요청 주소 설정(요청주소에는 도메인은 제외)
    @GET("users/Kotlin/repos")
    fun users(): Call<Repository> // import retrofit2

}