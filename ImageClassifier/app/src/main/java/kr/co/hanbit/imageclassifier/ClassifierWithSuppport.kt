package kr.co.hanbit.imageclassifier

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


/*텐서플로 라이트 서포트 라이브러리를 이용한 Classifier 클래스 구현*/
class ClassifierWithSuppport(context: Context) {
    /* 상수 선언 */
    companion object{
        // 2-1. 모델 로드: tflite 모델을 assets 디렉터리에 추가
        // 2-2. 모델 로드: 모델 파일명을 상수로 선언
        private const val MODEL_NAME = "mobilenet_imagenet_model.tflite"
        // 5-1. 추론 결과 해석: 분류 클래스 라벨을 포함하는 txt 파일을 assets 디렉터리에 추가
        // 5-2. 추론 결과 해석: 라벨 파일명을 상수로 선언
        private const val LABEL_FILE = "imagenet_labels.txt"
    }
    /* 프로퍼티 선언 */
    var context: Context = context
    // 2-4. 모델 로드: interpreter 프로퍼티 선언
    var interpreter: Interpreter
    // 3-1. 입력 이미지 전처리: 모델의 입력 이미지를 저장할 프로퍼티 선언
    lateinit var inputImage: TensorImage
    // 3-2. 입력 이미지 전처리: 모델의 입력 형상 프로퍼티 선언
    var modelInputChannel: Int = 0
    var modelInputWidth: Int = 0
    var modelInputHeight: Int = 0
    // 4-1. 추론: 모델의 추론된 출력 값을 저장할 프로퍼티 선언
    lateinit var outputBuffer: TensorBuffer
    // 5-3. 추론 결과 해석: 라벨 목록을 저장하는 프로퍼티 선언
    private lateinit var labels: List<String>


    init{
        // 2-3. 모델 로드: tflite 파일 로드
        val model: ByteBuffer? = FileUtil.loadMappedFile(context, MODEL_NAME)
        model?.order(ByteOrder.nativeOrder())?:throw IOException()
        interpreter = Interpreter(model)
        // 3-4. 입력 이미지 전처리: 메서드 호출
        initModelShape()
        // 5-4. 추론 결과 해석: 라벨 파일 로드
        labels = FileUtil.loadLabels(context, LABEL_FILE)
    }

    // 3-3. 입력 이미지 전처리: 메서드 정의
    // 모델의 입력 형상과 데이터 타입을 프로퍼티에 저장
    private fun initModelShape(){
        val inputTensor = interpreter.getInputTensor(0)
        val shape = inputTensor.shape()
        modelInputChannel = shape[0]
        modelInputWidth = shape[1]
        modelInputHeight = shape[2]
        // 모델의 입력값을 저장할 TensorImage 생성
        inputImage = TensorImage(inputTensor.dataType())

        // 4-2. 추론: 모델의 출력값을 저장할 TensorBuffer 생성
        val outputTensor = interpreter.getOutputTensor(0)
        outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())

    }

    // 3-5. 입력 이미지 전처리: TensorImage에 bitmap 이미지 입력 및 이미지 전처리 로직 정의
    // Bitmap 이미지를 입력 받아 전처리하고 이를 TensorImage 형태로 반환
    private fun loadImage(bitmap: Bitmap): TensorImage{
        // TensorImage에 이미지 데이터 저장
        inputImage.load(bitmap)
        // 전처리 ImageProcessor 정의
        val imageProcessor =
            ImageProcessor.Builder()                            // Builder 생성
                .add(ResizeOp(modelInputWidth,modelInputHeight, // 이미지 크기 변환
                    ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(NormalizeOp(0.0f, 255.0f))    // 이미지 정규화
                .build()                                       // ImageProcessor 생성
        // 이미지를 전처리하여 TensorImage 형태로 반환
        return imageProcessor.process(inputImage)
    }

    // 4-3. 추론: 추론 메서드 정의
    fun classify(image: Bitmap): Pair<String, Float>{
        inputImage = loadImage(image)
        interpreter.run(inputImage.buffer, outputBuffer.buffer.rewind())

        // 5-5. 추론 결과 해석: 모델 출력값을 라벨에 매핑하여 반환
        val output = TensorLabel(labels, outputBuffer).getMapWithFloatValue() // Map<String, Float>

        return argmax(output)

    }

    // 5-6. 추론 결과 해석: Map에서 확률이 가장 높은 클래스명과 확률 쌍을 찾아서 반환하는 메서드 정의
    private fun argmax(map: Map<String, Float>): Pair<String, Float>{
        var maxKey = ""
        var maxVal = -1.0f

        for(entry in map.entries){
            var f = entry.value
            if(f > maxVal){
                maxKey = entry.key
                maxVal = f
            }
        }

        return Pair(maxKey, maxVal)
    }

    // 6. 자원 해제: 자원 해제 메서드 정의
    fun finish(){
        if(interpreter != null)
            interpreter.close()
    }

}