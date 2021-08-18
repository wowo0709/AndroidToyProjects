package kr.co.hanbit.fileio

import java.io.*

class FileUtil {

    // 파일의 경로를 전달받아 파일을 읽은 result 변수 결괏값을 리턴
    // 호출 예시: var content = readTextFile("${filesDir}/파일명.txt")
    fun readTextFile(fullPath: String): String{
        // 1. File 인스턴스 생성
        val file = File(fullPath)
        // 2. 실제 파일이 존재하는지 검사
        if (!file.exists()) return ""
        // 3-1. FileReader로 file을 읽는다
        // 3-2. BufferedReader에 담아서 속도를 향상
        val reader = FileReader(file)
        val buffer = BufferedReader(reader)
        // 4-1. buffer를 통해 한 줄식 읽은 내용을 임시로 저장할 temp 변수를 선언하고
        // 4-2. 모든 내용을 저장할 StringBuffer를 result 변수로 선언
        var temp = ""
        val result = StringBuffer()
        // 5. 파일의 내용을 모두 읽음
        while(true){
            temp = buffer.readLine()
            if (temp == null) break
            else result.append(buffer)
        }
        // 6. buffer를 닫고 결괏값을 리턴
        buffer.close()
        return result.toString()
    }

    /*
    // openFileInput() : 파일을 읽어서 스트림으로 반환해주는 읽기 메서드
    // 줄 단위의 lines 끝에 개행("\n")을 추가하여 문자열로 contents 변수에 저장
    var contents = ""
    context.openFileInput("파일경로").bufferedReader().useLines{lines->
        contents = lines.joinToString("\n")
    }
    */


    // 파일 쓰기
    // 파라미터: 파일을 생성할 디렉터리 경로, 파일명, 작성내용
    // 호출 예시: writeTextFile(filesDir, "filename.txt", "쓸 내용")
    fun writeTextFile(directory: String, filename: String, content: String){
        // 1. File 인스턴스 생성
        val dir = File(directory)
        // 2. 실제 파일이 존재하는지 검사
        if (!dir.exists()) dir.mkdirs()
        // 3-1. 디렉터리가 생성되었으면 파일명을 합해서 FileWriter로 작성
        // 3-2. BufferedWriter에 담아서 속도 향상
        val writer = FileWriter(directory+'/'+filename)
        val buffer = BufferedWriter(writer)
        // 4. buffer로 파일 내용을 씀
        buffer.write(content)
        // 5. buffer 닫기
        buffer.close()
    }

    /*
    // 마찬가지로 openFileOutput() 메서드로 파일 출력 과정을 축약 가능
    // Context.MODE_PRIVATE 대신 Context.MODE_APPEND를 사용하면 기존에 동일한 파일명이 있을 경우 이어서 작성
    // 문자열을 스트림에 쓸 때는 바이트 배열(ByteArray)로 변환해야 함
    val contents = "Hello\nworld!"
    context.openFileOutput("파일명", Context.MODE_PRIVATE).use{stream->
        stream.write(contents.toByteArray())
    }
     */
}