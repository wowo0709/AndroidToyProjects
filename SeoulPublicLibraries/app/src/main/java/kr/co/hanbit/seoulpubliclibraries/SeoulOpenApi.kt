package kr.co.hanbit.seoulpubliclibraries

import kr.co.hanbit.seoulpubliclibraries.data.Library
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// 3-1. SeoulOpenApi 클래스를 생성하고 companion object 정의
class SeoulOpenApi {
    companion object{
        val DOMAIN = "http://openAPI.seoul.go.kr:8088/" // 도메인 주소
        val API_KEY = "4c627a6b51776a73383176754f4b45"  // 발급받은 API 키
    }
}

// 3-2. 레트로핏에서 사용할 SeoulOpenService 인터페이스 생성
interface SeoulOpenService{
    // 3-3. 도서관 데이터를 가져오는 getLibrary() 메서드 정의
    // 3-4. @Path 어노테이션을 사용하여 메서드의 파라미터로 넘어온 값을 @GET에 정의된 주소에 동적으로 삽입
    @GET("{api_key}/json/SeoulPublicLibraryInfo/1/200")
    fun getLibrary(@Path("api_key") key: String): Call<Library> // import retrofit2
}
