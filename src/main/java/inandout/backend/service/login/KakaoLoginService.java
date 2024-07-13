package inandout.backend.service.login;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;


@Service
public class KakaoLoginService {

    @Value("${spring.KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value(("${spring.KAKAO_REDIRECT_URI}"))
    private String kakaoRedirectURI;

    // 토큰 가져오기
    public HashMap<String, String> getAccessToken(String code) throws IOException {
        HashMap<String, String> kakaoToken = new HashMap<>();
        System.out.println("KakaoLoginService/getAccessToken");
        System.out.println("code: "+code);
        String accessToken = "";
        String refreshToken = "";

        String reqUrl = "https://kauth.kakao.com/oauth/token";  //얜 픽스되어있는 주소

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST 전송
        // POST시 outputstream 객체로 데이터 전송 -> setDoOutput(true)로하여 "outputstream 객체로 전송할 데이터 있음!" 설정
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST 요청시 보낼 파라미터 셋팅
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+kakaoApiKey);
        sb.append("&redirect_uri="+kakaoRedirectURI);
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseSb = new StringBuilder();
        while((line = br.readLine()) != null){
            responseSb.append(line);
        }
        String result = responseSb.toString();
        System.out.println("responseBody : "+result);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);
        accessToken = element.getAsJsonObject().get("access_token").getAsString();
        refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

        kakaoToken.put("accessToken",accessToken);
        kakaoToken.put("refreshToken",refreshToken);
        br.close();
        bw.close();

        System.out.println("accessToken: "+accessToken+", refreshToken: "+refreshToken);

        return kakaoToken;
    }


    // accessToken으로 유저정보 가져오기
    public HashMap<String, Object> getUserInfo(String accessToken) throws IOException {
        HashMap<String, Object> kakaoUserInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseSb = new StringBuilder();
        while((line = br.readLine()) != null){
            responseSb.append(line);
        }
        String result = responseSb.toString();

        System.out.println(result);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        //JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject(); //닉네임 등 가져올거면 이거 사용
        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

        String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

        kakaoUserInfo.put("accessToken", accessToken);
        kakaoUserInfo.put("email", email);

        br.close();

        return kakaoUserInfo;
    }

    //토큰 유효한지 확인
    public boolean isValidToken(String token) {

        try{
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();  //API와 통신할 수 있게해줌

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            // 401 Unauthorized 처리
            return false;
        } catch (Exception e) {
            // 기타 예외 처리
            return false;
        }

    }

    //토큰 갱신
    //refreshToken 갱신할 수 있는상태면 자동 갱신해줌
    public HashMap<String, String> updateKakaoToken(String refreshToken) throws IOException {
        HashMap<String, String> newKakaoToken = new HashMap<>();
        System.out.println("KakaoLoginService/getAccessToken");
        System.out.println("refreshToken: "+refreshToken);
        String newAccessToken = "";
        String newRefreshToken = "";

        String reqUrl = "https://kauth.kakao.com/oauth/token";


        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST 요청시 보낼 파라미터 셋팅
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=refresh_token");
        sb.append("&client_id="+kakaoApiKey);
        sb.append("&refresh_token="+refreshToken);
        bw.write(sb.toString());
        bw.flush();

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseSb = new StringBuilder();
        while((line = br.readLine()) != null){
            responseSb.append(line);
        }
        String result = responseSb.toString();
        System.out.println("responseBody : "+result);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);
        newAccessToken = element.getAsJsonObject().get("access_token").getAsString();
        newRefreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

        newKakaoToken.put("accessToken",newAccessToken);
        newKakaoToken.put("refreshToken",newRefreshToken);
        br.close();
        bw.close();

        System.out.println("newaccessToken: "+newAccessToken+", newrefreshToken: "+newRefreshToken);

        return newKakaoToken;

    }


}
