package kr.co.hanbit.room

import androidx.room.Database
import androidx.room.RoomDatabase

// 4-1. RoomHelper 클래스 정의하기: RoomDatabase를 상속하는 추상 클래스 생성, @Database 선언
@Database(entities = arrayOf(RoomMemo::class), version = 1, exportSchema = false)
abstract class RoomHelper: RoomDatabase() {
    // 4-2. RoomHelper 클래스 정의하기: RoomMemoDAO 인터페이스의 구현체를 사용할 수 있는 메서드명 정의
    abstract fun roomMemoDAO(): RoomMemoDAO
}