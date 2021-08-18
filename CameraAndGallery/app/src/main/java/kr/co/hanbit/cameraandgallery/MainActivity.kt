package kr.co.hanbit.cameraandgallery

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kr.co.hanbit.cameraandgallery.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat

// 권한을 처리하는 BaseActivity 상속
// class MainActivity : AppCompatActivity() {
class MainActivity: BaseActivity(){

    // 카메라 관련 상수 선언
    // 클래스 내에서 상수 선언 시 companion object 블록 필요
    companion object{
        const val PERM_STORAGE = 99 // 외부 저장소 권한 처리
        const val PERM_CAMERA = 100 // 카메라 권한 처리
        const val REQ_CAMERA = 101  // 카메라 촬영 요청

        const val REQ_STORAGE = 102 // 갤러리 열람 요청
    }

    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    // 이미지의 Uri를 가져와서 저장할 프로퍼티 선언
    var realUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 카메라에서 찍은 사진을 외부 저장소(포토갤러리)에 저장할 것이기 때문에 저장소 권한을 요청하는 코드 작성
        // 파라미터: 요청할 저장소 권한, requestCode(앞에서 미리 정의)
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)



    }

    override fun permissionGranted(requestCode: Int) {
        // 권한 승인 시 적절한 메서드 호출
        when(requestCode){
            // 외부 저장소 권한 승인 시 카메라 권한 요청
            PERM_STORAGE -> setViews()
            // 카메라 권한 승인 시 카메라 호출
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> {
                Toast.makeText(baseContext,
                                "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                                Toast.LENGTH_LONG).show()
                finish()
            }
            PERM_CAMERA -> {
                Toast.makeText(baseContext,
                                "카메라 권한을 사용해야 카메라를 사용할 수 있습니다.",
                                Toast.LENGTH_LONG).show()
            }
        }
    }

    // 외부 저장소 권한이 승인되었을 때 호출되는 메서드
    fun setViews(){
        // 카메라 권한 요청
        // 권한 요청의 결과에 따라 승인되었을 경우에만 permissionGranted() 메서드에서 카메라 요청
        binding.btnCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA),PERM_CAMERA)
        }
        // 갤러리 열람 메서드 호출
        binding.btnGallery.setOnClickListener {
            openGallery()
        }
    }

    // 실질적인 카메라 호출 메서드
    fun openCamera(){
        // 카메라 호출 시 보낼 인텐트
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 카메라 호출
        // startActivityForResult(intent, REQ_CAMERA)

        // 촬영된 이미지를 바로 사용하지 않고 Uri로 생성하여 미디어스토어에 저장하고 사용하기
        // createImageUri 파라미터: filename, mimetype
        createImageUri(newFileName(), "image/jpg")?.let{uri->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }


    }

    // 카메라로부터 촬영된 사진 정보를 받아서 텍스트뷰에 출력
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK)
            when(requestCode){
                /*
                data에는 이미지의 프리뷰가 들어있기 때문에 이 이미지를 바로 텍스트뷰에 출력하면
                화질이 떨어지는 이미지가 출력됩니다. 대신에 실제 이미지를 미디어스토어에 저장하고
                저장된 이미지를 가져와서 화면에 보여주도록 해야 합니다.
                 */
                REQ_CAMERA -> {
                    realUri?.let { uri ->
                        val bitmap = loadBitmap(uri)
                        binding.imagePreview.setImageBitmap(bitmap)

                        realUri = null
                    }

//                  // data 파라미터를 통해 전달되는 사진은 data.extras.get("data")로 꺼낼 수 있습니다.
//                  if (data?.extras?.get("data") != null){
//                      // Bitmap으로 형변환 (원본 타입은 Object)
//                      val bitmap = data?.extras?.get("data") as Bitmap
//                      binding.imagePreview.setImageBitmap(bitmap)
//                  }


                }
                // 갤러리에서 선택된 이미지를 이미지뷰에 출력
                REQ_STORAGE -> {
                    data?.data?.let{ uri ->
                        binding.imagePreview.setImageURI(uri)
                    }
                }
            }
    }

    // 촬영한 이미지를 저장할 Uri를 미디어스토어에 생성하는 메서드
    fun createImageUri(filename: String, mimeType: String): Uri?{
        // ContentValues 클래스를 사용해 파일명과 파일의 타입을 입력한 후,
        // ContentResolver의 insert() 메서드를 통해 저장
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        // 파라미터: 저장소명, 저장할 콘텐트
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    }

    // 새로운 파일명을 만들어주는 메서드 (파일명이 중복되지 않도록 현재 시각 사용)
    fun newFileName(): String{
        // SimpleDateFormat(java.text) 사용
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        // 연월일_시간.jpg
        return "$filename.jpg"
    }

    // Uri를 이용해서 미디어스토어에 저장된 이미지를 읽어오는 메서드
    // 파라미터로 Uri를 받아 결괏값을 Bitmap으로 반환
    fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        //API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고,
        // 27보다 크면 ImageDecoder를 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                        ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }

        }catch(e: IOException){
            e.printStackTrace()
        }

        return image
    }

    // 갤러리 열람 메서드
    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_STORAGE)
    }

}