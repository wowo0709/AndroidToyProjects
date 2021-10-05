package com.example.contentresolver

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contentresolver.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 리사이클러 뷰 어댑터 클래스
class MusicRecyclerAdapter: RecyclerView.Adapter<MusicRecyclerAdapter.Holder>() {

    // 앨범 아이템 목록 리스트
    var musicList = mutableListOf<Music>()
    // 목록을 클릭해서 음원 실행하기: MediaPlayer 인스턴스 생성
    var mediaPlayer: MediaPlayer? = null

    // 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return Holder(binding)
    }
    // 뷰 홀더를 화면에 출력
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = musicList.get(position)
        holder.setMusic(music)
    }
    // 아이템 목록 개수 반환
    override fun getItemCount(): Int {
        return musicList.size
    }

    // 목록을 클릭해서 음원 실행하기: 뷰 홀더 클래스를 내부 클래스로
    // 뷰 홀더 클래스
    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        // 현재 선택된 음원(음악 플레이 대비)
        var currentMusic: Music? = null
        // var musicUri: Uri? = null

        // 목록을 클릭해서 음원 실행하기: itemView 에 클릭리스너 연결
        init{
            // 뷰홀더에 클릭 리스너 달기
            itemView.setOnClickListener {
                // 선택된 음악이 실행 중이 아니라면,
                if (currentMusic?.isPlay == false) {
                    // 현재 실행 중인 음악이 있으면 종료
                    if (mediaPlayer != null) {
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }
                    // 선택한 아이템의 음악 플레이
                    mediaPlayer = MediaPlayer.create(itemView.context, currentMusic?.getMusicUri())
                    // mediaPlayer = MediaPlayer.create(itemView.context, musicUri)
                    mediaPlayer?.start()
                    currentMusic?.isPlay = true
                }else{ // 선택된 음악이 실행 중이었다면 현재 음악 중지
                    mediaPlayer?.stop()
                    mediaPlayer = null
                    currentMusic?.isPlay = false
                }
            }
        }

        // 아이템에 음원 정보 세팅
        fun setMusic(music: Music){
            binding.run{
                imageAlbum.setImageURI(music.getAlbumUri())
                textArtist.text = music.artist
                textTitle.text = music.title

                val duration = SimpleDateFormat("mm:ss").format(music.duration)
                textDuration.text = duration
            }
        }
    }
}

/*
// 뷰 홀더 클래스
class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
    // 현재 선택된 음원의 Uri(음악 플레이 대비)
    var musicUri: Uri? = null
    // 아이템에 음원 정보 세팅
    fun setMusic(music: Music){
        binding.run{
            imageAlbum.setImageURI(music.getAlbumUri())
            textArtist.text = music.artist
            textTitle.text = music.title

            val duration = SimpleDateFormat("mm:ss").format(music.duration)
            textDuration.text = duration
        }
        this.musicUri = music.getMusicUri()
    }
}
 */