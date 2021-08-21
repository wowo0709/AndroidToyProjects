package kr.co.hanbit.imageclassifier

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import kr.co.hanbit.imageclassifier.databinding.ActivityPhotoBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoActivity : BaseActivity() {
    /* 상수 선언 */
    companion object{
        const val PERM_STORAGE = 99 // 외부 저장소 권한 요청
        const val PERM_CAMERA = 100 // 카메라 권한 요청
        const val REQ_CAMERA = 101  // 카메라 호출
        const val REQ_GALLERY = 102 // 갤러리 호출
    }

    /* 프로퍼티 선언 */
    val binding by lazy{ActivityPhotoBinding.inflate(layoutInflater)}

    var photoUri: Uri? = null
    // 모델 프로퍼티 선언
    lateinit var classifier: ClassifierWithModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)

        // 모델 가져오기
        try{
            classifier = ClassifierWithModel(this)
        }catch(ioe: IOException){
            ioe.printStackTrace()
        }
    }

    override fun permissionGranted(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> {
                setViews()
            }
            PERM_CAMERA -> {
                openCamera()
            }
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
                                "카메라 권한을 승인해야 앱을 사용할 수 있습니다.",
                                Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setViews() {
        binding.btnCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
        binding.btnGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun openCamera(){

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let{ uri ->
            photoUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQ_CAMERA)
        }

    }

    private fun createImageUri(filename: String, mimeType: String): Uri?{

        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun newFileName(): String{

        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.jpg"
    }

    private fun openGallery(){
        // 권장 코드
        val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK)
            when(requestCode){
                // uri를 bitmap으로 변환
                REQ_CAMERA -> {
                    photoUri?.let{ uri ->
                        val capturedImage = loadBitmap(uri)
                        photoUri = null
                        // 비트맵을 모델에 전달하여 추론
                        val result = callClassifier(capturedImage)

                        binding.imageView.setImageBitmap(capturedImage)
                        binding.textView.text = result

                    }
                }
                REQ_GALLERY -> {
                    val selectedImageUri: Uri = data?.data as Uri
                    val selectedImage = loadBitmap(selectedImageUri)
                    // 비트맵을 모델에 전달하여 추론
                    val result = callClassifier(selectedImage)

                    binding.imageView.setImageBitmap(selectedImage)
                    binding.textView.text = result
                }
            }
        else{
            Toast.makeText(baseContext,
                            "Result Canceled!",
                            Toast.LENGTH_LONG).show()
        }
    }

    private fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        //API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고, 27보다 크면 ImageDecoder를 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                        ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        }catch(ioe: IOException){
            ioe.printStackTrace()
        }

        return image
    }

    private fun callClassifier(bitmap: Bitmap?): String{
        val startTime = SystemClock.uptimeMillis() // 모델 성능 측정
        val output: Pair<String, Float> = classifier.classify(bitmap)
        val elapsedTime = SystemClock.uptimeMillis() - startTime // 모델 성능 측정

        val resultStr = String.format(Locale.ENGLISH,
                "class : %s   Prob : %.2f%%",
                output.first, output.second * 100)

        binding.textLog.text = "$elapsedTime ms" // 모델 성능 측정

        return resultStr
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.finish()
    }
}