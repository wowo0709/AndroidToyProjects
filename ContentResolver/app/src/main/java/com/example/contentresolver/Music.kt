package com.example.contentresolver

import android.net.Uri
import android.provider.MediaStore

class Music(id: String, title: String?, artist: String?, albumId: String?, duration: Long?) {

    /* 프로퍼티 정의 */
    var id: String = ""
    var title: String?
    var artist: String?
    var albumId: String?
    var duration: Long?
    // 목록을 클릭해서 음원 실행하기: 실행 여부 플래그
    var isPlay = false

    init{
        this.id = id
        this.title = title
        this.artist = artist
        this.albumId = albumId
        this.duration = duration
    }

    // 음원의 URI 생성
    fun getMusicUri(): Uri{
        return Uri.withAppendedPath(
            // 음원 URI는 기본 MediaStore의 주소와 음원 ID를 조합
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
        )
    }

    // 음원 파일별로 썸네일 지정(앨범 아트 Uri 생성)
    fun getAlbumUri(): Uri{
        return Uri.parse(
            "content://media/external/audio/albumart/" + albumId
        )
    }
}