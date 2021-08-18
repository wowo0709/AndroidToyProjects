package kr.co.hanbit.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLiteOpenHelper를 상속받아 데이터베이스를 사용
// 파라미터: 컨텍스트, DB명, 팩토리, 버전 정보(팩토리는 사용하지 않음)
class SqliteHelper(context: Context, name: String, version: Int): SQLiteOpenHelper(context, name, null, version){

    // 데이터 베이스 최초 생성 시에만 호출
    // 파라미터: 사용할 데이터베이스
    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성 쿼리 작성
        val create = "create table memo" +
                "(" +
                "no integer primary key, " +
                "content text, " +
                "datetime integer" +
                ")"
        // db의 execSQL 메서드에 전달하여 쿼리 실행
        db?.execSQL(create)
    }
    // SqliteHelper에 전달되는 버전 정보가 변경되었을 대 현재 생성되어 있는 데이터베이스의 버전보다 높으면 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /* 사용하지 않음 */
    }

    /* 4개의 기본 메서드 구현 */
    // INSERT
    fun insertMemo(memo: Memo){
        // 삽입할 데이터 작성
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        // 쓰기 전용 데이터베이스 가져오기
        val wd = writableDatabase
        // 데이터베이스에 데이터 삽입
        wd.insert("memo", null, values)
        // 쓰기 전용 데이터베이스 닫기
        wd.close()
    }
    // SELECT
    fun selectMemo(): MutableList<Memo>{
        val list = mutableListOf<Memo>() // 반환할 값 선언

        // 쿼리 작성(여기서는 전체 선택)
        val select = "select * from memo"
        // 읽기 전용 데이터베이스 가져오기
        val rd = readableDatabase
        // 데이터베이스의 rawQuery() 메서드에 쿼리를 담아서 호출하면 커서(cursor) 형태로 값이 반환
        val cursor = rd.rawQuery(select, null)
        // 테이블에 레코드가 있을 때 동안
        while (cursor.moveToNext()){
            // 반복문을 돌면서 테이블에 정으된 3개의 컬럼에서 값 꺼낸 후 변수에 저장
            val no: Long = cursor.getLong(cursor.getColumnIndex("no"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datatime"))
            // 컬럼값들로 Memo 클래스를 생성하고 반환할 목록에 추가
            list.add(Memo(no, content, datetime))
        }
        // 커서와 읽기 전용 데이터베이스 닫기
        cursor.close()
        rd.close()

        return list // 반환
    }
    // UPDATE
    fun updateMemo(memo: Memo){
        // 수정할 데이터 작성
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        // 쓰기 전용 데이터베이스에서 수정
        // 파라미터: 테이블명, 수정할 값, 수정할 조건, 조건파라미터
        val wd = writableDatabase
        wd.update("memo", values, "no = ${memo.no}", null)
        wd.close()
    }
    // DELETE
    fun deleteMemo(memo: Memo){
        // 쿼리 작성
        val delete = "delete from memo where no = ${memo.no}"
        // 쿼리 실행, 데이터 삭제
        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }
}

// Memo 클래스의 INSERT, SELECT, UPDATE, DELETE에 모두 사용
data class Memo(var no: Long?, var content: String, var datetime: Long){

}