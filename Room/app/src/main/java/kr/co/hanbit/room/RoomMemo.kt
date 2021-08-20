package kr.co.hanbit.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 2-2. RoomMemo 클래스 생성하기: 클래스 생성하고 @Entity 선언
@Entity(tableName = "room_memo")
class RoomMemo {
    // 2-3. RoomMemo 클래스 생성하기: @ColumnInfo 으로 테이블 컬럼 명시
    // 변수명과 다르게 하고 싶으면 @ColumnInfo(name="컬럼명")으로 작성
    @ColumnInfo
    @PrimaryKey(autoGenerate = true) // 키 명시, 자동 증가 옵션
    var no: Long? = null
    @ColumnInfo
    var content: String = ""
    @ColumnInfo
    var datetime: Long = 0

    // 2-4. RoomMemo 클래스 생성하기: 생성자 작성
    constructor(content: String, datetime: Long){
        this.content = content
        this.datetime = datetime
    }
}