package kr.co.hanbit.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

// 3-1. RoomMemoDAO 인터페이스 정의하기: 인터페이스 생성하고 @DAO 선언
@Dao
interface RoomMemoDAO {
    // 3-2. RoomMemoDAO 인터페이스 정의하기: 삽입, 조회, 수정, 삭제에 해당하는
    // 3개의 메서드를 만들고 알맞은 어노테이션 선언
    @Query("select * from room_memo")
    fun getAll(): List<RoomMemo>

    @Insert(onConflict = REPLACE)
    fun insert(memo: RoomMemo)

    @Delete
    fun delete(memo: RoomMemo)
}