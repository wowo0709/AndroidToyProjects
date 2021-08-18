package kr.co.hanbit.digitclassifier

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.drawable.shapes.Shape
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.Tensor
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel


// const val MODEL_NAME = "keras_model.tflite" // tflite 파일명
const val MODEL_NAME = "keras_model_cnn.tflite"

// 1. 모델과 관련된 작업을 할 클래스 생성
// 모델 파일을 로드하고 이미지를 입력하면 추론하여 결과 값을 해석
// 모델을 로드할 때 assets 폴더를 참조하는데, 그 때 앱 컨텍스트가 필요 -> 생성자로 컨텍스트 전달
class Classifier(context: Context) {

    /*전역변수 선언*/
    var context: Context = context
    // 3. Interpreter 생성
    var interpreter: Interpreter
    // 4. 입력 이미지 전처리 - 1. 모델의 입력 크기 확인
    // 변수 선언
    var modelInputWidth: Int = 0
    var modelInputHeight: Int = 0
    var modelInputChannel: Int = 0
    // 5. 추론 및 결과 해석 - 1. 추론
    var modelOutputClasses: Int = 0

    init{
        // 2(cont). 모델 초기화
        val model: ByteBuffer? = loadModelFile(MODEL_NAME)      // ByteBuffer 인스턴스
        // 시스템의 byteOrder와 동일하게 동작
        // DrawActivity에서 Classifier 인스턴스를 생성할 때 예외처리
        model?.order(ByteOrder.nativeOrder())?:throw IOException()
//        model?.order(ByteOrder.nativeOrder())
//        if (model == null) throw IOException()

        // 3. Interpreter 생성
        // Interpreter는 모델에 데이터를 입력하고 추론 결과를 전달받을 수 있는 클래스
        interpreter = Interpreter(model)

        // 4. 입력 이미지 전처리 - 1. 모델의 입력 크기 확인(cont.)
        // 모델의 입출력 크기 계산 메서드 호출
        initModelShape()

    }

    // 2. assets 폴더에서 tflite 파일을 읽어오는 함수 정의
    // tflite 파일명을 입력받아 ByteBuffer 클래스로 모델을 반환
    private fun loadModelFile(modelName: String): ByteBuffer? {

        // AssetManager는 assets 폴더에 저장된 리소스에 접근하기 위한 기능을 제공
        val am = this.context.assets // AssetManager
        // AssetManager.openRd(파일명): AssetFileDescriptor를 반환
        val afd: AssetFileDescriptor? = am.openFd(modelName)// modelName 에 해당하는 파일이 없을 경우 null
        if (afd == null) {
            throw IOException() // 자신을 호출한 쪽에서 예외처리 요구
            return null
        }
        // AssetFileDescriptor.fileDescriptor: 파일의 FileDescriptor 반환 -> 해당 파일의 읽기/쓰기 가능
        // FileInputStream의 생성자에 FileDescriptor를 해당 파일의 입력 스트림 반환
        val fis = FileInputStream(afd.fileDescriptor) // FileInputStream
        // fis.read()로 읽을 수도 있지만 성능을 위해 스트림의 FileChannel 이용
        val fc = fis.channel // FileChannel

        // 파일디스크립터 오프셋과 길이
        val startOffset = afd.startOffset // long
        val declaredLength = afd.declaredLength // long

        // FileChannel.map() 메서드로 ByteBuffer 클래스를 상속한 MappedByteBuffer 인스턴스 생성
        // 파라미터: 참조모드, 오프셋, 길이
        // 최종적으로 tflite 파일을 ByteBuffer 형으로 읽어오는데 성공!
        return fc.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

    }

    // 4. 입력 이미지 전처리 - 1. 모델의 입력 크기 확인(cont.)
    // 모델의 입출력 크기 계산 메서드 정의
    private fun initModelShape(){
        // getInputTensor()로 입력 텐서 가져오기
        val inputTensor: Tensor = interpreter.getInputTensor(0)
        // shape()로 입력 텐서 형상 가져오고 프로퍼티에 저장
        val inputShape = inputTensor.shape()
        modelInputChannel = inputShape[0]
        modelInputWidth = inputShape[1]
        modelInputHeight = inputShape[2]

        // 5. 추론 및 결과 해석 - 1. 추론(cont.)
        // 출력 텐서의 형태를 이용하여 출력 클래스 수 가져오기
        val outputTensor = interpreter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        modelOutputClasses = outputShape[1]


    }

    // 4. 입력 이미지 전처리 - 2. 입력 이미지 크기 변환
    private fun resizeBitmap(bitmap: Bitmap): Bitmap{
        // 파라미터: 비트맵 인스턴스, 새로운 너비, 새로운 높이, 이미지 보간법
        // 이미지 보간법: 이미지를 늘릴 때(true로 설정, 양선형보간법)/이미지를 줄일 때(false로 설정, 최근접 보간법)
        return Bitmap.createScaledBitmap(bitmap, modelInputWidth, modelInputHeight, false)
    }

    // 4. 입력 이미지 전처리 - 3, 4. 입력 이미지 채널과 포맷 변환 (하나의 메서드에서 처리)
    // 변환된 ByteBuffer를 반환
    private fun convertBitmapToGrayByteBuffer(bitmap: Bitmap): ByteBuffer{
        // 바이트 크기만큼 ByteBuffer 메모리를 할당
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(bitmap.byteCount)
        // 모델과 동일한 바이트 순서로 설정
        byteBuffer.order(ByteOrder.nativeOrder())

        // 비트맵의 픽셀 값 가져오기
        // 파라미터: 저장할 배열, offset, stride, x, y, width, height
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // grayscale로 변환
        for(pixel in pixels){
            // 예시: Pixel(int, ARGB): 11111111 00001111 11110000 01010101 (4바이트, 32비트)
            val r = pixel shr 16 and 0xFF
            val g = pixel shr 8 and 0xFF
            val b = pixel and 0XFF

            // 픽셀의 평균값을 구하고 0~1 사이의 값으로 정규화(***모델을 훈련시킬 때와 동일하게 정규화***)
            val avgPixelValue = (r + g + b) / 3.0f
            val normalizedPixelValue = avgPixelValue / 255.0f

            // 반환할 ByteBuffer에 정규화된 픽셀값을 추가
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }


    // 5. 추론 및 결과 해석 - 1. 추론(cont.)
    // 출력 클래스 수를 이용하여 출력 값을 담을 배열을 생성하고 interpreter의 run() 메서드에 전달하여 추론을 수행
    // 5. 추론 및 결과 해석 - 2. 추론 결과 해석(cont.)
    // 추론 결과에서 확률이 가장 높은 클래스와 그 확률을 반환
    public fun classify(image: Bitmap): Pair<Int, Float>{
        // 전처리된 입력 이미지
        val buffer = convertBitmapToGrayByteBuffer(resizeBitmap(image))
        // 추론 결과를 담을 이차원 배열
        val result = Array(1) { FloatArray(modelOutputClasses) { 0.0f } }
        // 추론 수행
        interpreter.run(buffer, result)

        // 5. 추론 및 결과 해석 - 2. 추론 결과 해석(cont.)
        // 확률이 가장 높은 클래스와 확률을 반환
        return argmax(result[0])

    }

    // 5. 추론 및 결과 해석 - 2. 추론 결과 해석
    // 추론 결과값을 확인하여 확률이 가장 높은 클래스를 반환
    private fun argmax(array: FloatArray): Pair<Int, Float>{
        var argmax: Int = 0
        var max: Float = array[0]

        for(i in 1 until array.size){
            val f = array[i]
            if(f > max){
                argmax = i
                max = f
            }
        }

        return Pair(argmax, max)
    }


    // 6. 자원 해제
    // interpreter 자원 정리
    public fun finish(){
        if(interpreter != null)
            interpreter.close()
    }


}