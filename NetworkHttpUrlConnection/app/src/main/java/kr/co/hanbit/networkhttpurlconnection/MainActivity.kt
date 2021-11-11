package kr.co.hanbit.networkhttpurlconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.hanbit.networkhttpurlconnection.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    // 2-1. 뷰 바인딩 설정
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 2-2. 클릭 리스터 설정
        binding.buttonRequest.setOnClickListener {
            // 2-3. 버튼 클릭 시 네트워크 작업 요청, 백그라운드 처리
            CoroutineScope(Dispatchers.IO).launch{
                // 2-13. try 문으로 예외처리
                try {
                    // 2-4. 플레인 텍스트에 입력된 주소를 가져와 주소 설정
                    // http는 보안 문제가 있어서 AndoridManifest.xml에 부가적인 설정 필요. https 로 설정.
                    var urlText = binding.editUrl.text.toString()
                    if (!urlText.startsWith("https")) {
                        urlText = "https://${urlText}"
                    }
                    // 2-5. 주소를 URL 객체로 변환하고 변수에 저장
                    val url = URL(urlText) // import java.net
                    // 2-6. 서버와의 연결 생성
                    // openConnection() 메서드의 반환값은 URLConnection이라는 추상 클래스이므로
                    // HttpURLConnection 클래스로 형 변환 필요.
                    val urlConnection = url.openConnection() as HttpURLConnection
                    // 2-7. 연결된 커넥션에 요청 방식 설정. (대문자로 입력해야 함)
                    urlConnection.requestMethod = "GET"
                    // 2-8. 응답이 정상이면 응답 데이터 처리
                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        // 2-9. 입력 스트림을 연결하고 버퍼에 담아서 데이터를 읽을 준비
                        val streamReader = InputStreamReader(urlConnection.inputStream)
                        val buffered = BufferedReader(streamReader)
                        // 2-10. 반복문을 돌면서 한 줄씩 읽은 데이터를 content 변수에 저장
                        val content = StringBuilder()
                        while (true) {
                            val line = buffered.readLine() ?: break
                            content.append(line)
                        }
                        // 2-11. 사용한 스트림과 커넥션 해제
                        buffered.close()
                        urlConnection.disconnect()
                        // 2-12. 화면의 텍스트 뷰에 content 변수에 저장된 값 입력
                        launch(Dispatchers.Main) {
                            binding.textContent.text = content.toString()
                        }
                    }
                }catch(e: Exception){
                    e.printStackTrace()
                }

            }
        }
    }
}