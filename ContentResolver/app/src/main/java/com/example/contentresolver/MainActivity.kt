package com.example.contentresolver

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentresolver.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    companion object{
        const val PERM_STORAGE = 99
    }

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 외부 저장소 권한 요청
        requirePermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_STORAGE)
    }

    override fun permissionGranted(requestCode: Int) {
        startProcess()
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> {
                Toast.makeText(this,
                    "외부 저장소 권한 승인이 필요합니다. 앱을 종료합니다.",
                    Toast.LENGTH_LONG).show()

                finish()
            }
        }
    }

    // 음원 목록을 불러오는 메서드
    // 어댑터와 화면, 데이터를 가져와서 연결
    fun startProcess(){
        // 어댑터 생성
        val adapter = MusicRecyclerAdapter()
        // 읽어온 음원 리스트를 어댑터에 전달
        adapter.musicList.addAll(getMusicList())
        // 리사이클러 뷰에 어댑터와 레이아웃 매니저 연결
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    // 음원을 읽어오는 메서드
    fun getMusicList(): List<Music>{
        // 음원 정보의 테이블 주소를 listUrl 변수에 저장
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // 음원 정보 테이블에서 읽어올 컬럼명을 배열로 정의
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        // 콘텐트 리졸버의 query() 메서드에 테이블 주소와 컬럼명을 전달하여 호출(커서 반환)
        val cursor = contentResolver.query(listUrl, proj, null, null, null)
        // 커서로 전달받은 데이터를 꺼내서 저장할 목록 변수 생성
        val musicList = mutableListOf<Music>()
        // 데이터를 읽어서 musicList 에 담기
        while(cursor?.moveToNext() == true){
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)
        }
        // 데이터가 담긴 musicList 반환
        return musicList

    }


}