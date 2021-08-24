package kr.co.hanbit.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MyService : Service() {

    /* Started Service */

    // 명령어를 상수로 선언
    companion object{
        // 명령어는 일반적으로 '패키지명+명령어' 조합으로 생성
        val ACTION_START = "kr.co.hanbit.servicetest.START"
        val ACTION_RUN = "kr.co.hanbit.servicetest.RUN"
        val ACTION_STOP = "kr.co.hanbit.servicetest.STOP"
    }

    // 호출 시 명령어 전달
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Toast.makeText(this, "서비스 시작", Toast.LENGTH_SHORT).show()
        Log.d("StartedService", "action = $action")
        return super.onStartCommand(intent, flags, startId)
    }
    // 서비스 종료 시 호출
    override fun onDestroy(){
        Toast.makeText(this, "서비스 종료", Toast.LENGTH_SHORT).show()
        Log.d("Service", "서비스 종료.")
        super.onDestroy()
    }

    /* Bound Service */

    // 바운드 서비스와 액티비티 연결
    inner class MyBinder: Binder(){
        fun getService(): MyService{
            return this@MyService
        }
    }
    val binder = MyBinder()

    // 바운드 서비스를 이용할 때 사용
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // 바운드 서비스의 메서드(테스트)
    fun serviceMessage(): String{
        return "바운드 서비스 함수 호출됨"
    }


}