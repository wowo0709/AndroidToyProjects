package kr.co.hanbit.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    // 스타티드 서비스 시작
    fun serviceStart(view: View){
        val intent = Intent(this, MyService::class.java)
        intent.action = MyService.ACTION_START
        startService(intent)
    }
    // 스타티드 서비스 종료
    fun serviceStop(view: View){
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }

    // 바운드 서비스와 연결할 수 있는 서비스 커넥션 생성
    var myService: MyService? = null // 바운드 서비스
    var isService = false // 현재 서비스가 연결되어 있는지 여부
    val connection = object: ServiceConnection {
        // 서비스 연결 시 호출
        override fun onServiceConnected(name: ComponentName, service: IBinder){
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            isService = true

            Log.d("BoundService", "바운드 서비스 연결")
        }
        // 서비스가 '비정상적으로' 죵료 시 호출
        override fun onServiceDisconnected(name: ComponentName?) {
            isService = false
        }
    }

    // 바운드 서비스를 호출하면서 생성한 커넥션을 전달
    fun serviceBind(view: View){
        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Toast.makeText(this, "바운드 서비스 연결", Toast.LENGTH_SHORT).show()
    }

    // 바운드 서비스 연결 해제
    fun serviceUnbind(view: View){
        // 서비스가 실행 중인지 먼저 체크(실행 중이지 않을 때 호출하면 오류 발생)
        if (isService){
            unbindService(connection)
            isService = false
            Toast.makeText(this, "바운드 서비스 연결 해제", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "바운드 서비스가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 바운드 서비스의 메서드 호출
    fun callServiceFunction(view: View){
        if(isService){
            val message = myService?.serviceMessage()
            Toast.makeText(this, "message = $message", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "바운드 서비스가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}