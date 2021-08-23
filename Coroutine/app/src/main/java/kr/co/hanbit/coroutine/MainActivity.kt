package kr.co.hanbit.coroutine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.hanbit.coroutine.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {

    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 2. 버튼 클릭 리스너 달기
        binding.btnDownload.setOnClickListener {
            // 3. 코루틴 스코프 추가, 컨텍스트는 Main
            CoroutineScope(Dispatchers.Main).launch{
                binding.progress.visibility = View.VISIBLE
                val url = binding.editUrl.text.toString()
                // 파일 다운로드는 IO 컨텍스트에서 진행
                val bitmap = withContext(Dispatchers.IO){
                    loadImage(url)
                }
                binding.imageView.setImageBitmap(bitmap)
                binding.progress.visibility = View.GONE
            }
        }

        /* binding.run 블록을 이용해 반복되는 'binding' 을 제거
        binding.run{
            btnDownload.setOnClickListener {
                // 3. 코루틴 스코프 추가, 컨텍스트는 Main
                CoroutineScope(Dispatchers.Main).launch{
                    progress.visibility = View.VISIBLE
                    val url = binding.editUrl.text.toString()
                    val bitmap = withContext(Dispatchers.IO){
                        loadImage(url)
                    }
                    imageView.setImageBitmap(bitmap)
                    progress.visibility = View.GONE
                }
            }
        }
         */

    }
}

// 1. URL 객체를 만들고 URL이 가지고 있는 openStream을 Bitmap 이미지로 반환하는 서스펜드 함수
suspend fun loadImage(imageUrl: String): Bitmap {
    val url = URL(imageUrl) // java.net
    val stream = url.openStream()
    return BitmapFactory.decodeStream(stream)
}