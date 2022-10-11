package com.kh.heallo.web.sns.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NaverLoginUtile {
    private String clientId;
    private String state;
    private String responseType;

    private String currentURI;

    public String getCurrentURI() {
        return currentURI;
    }

    public void setCurrentURI(String currentURI) {
        this.currentURI = currentURI;
    }

    public String createURL(HttpServletRequest request) {
        this.state = "NAVER_TEST";
        this.clientId = "QwysbK9EuYMOrg33Mipw";
        this.responseType = "code";
        String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        String URL = UriComponentsBuilder
                .fromUriString("https://nid.naver.com")
                .path("/oauth2.0/authorize")
                .queryParam("response_type", responseType)
                .queryParam("client_id", this.clientId)
                .queryParam("state", this.state)
                .queryParam("redirect_uri", domain + "/members/naver-callback/redirect")
                .build()
                .encode()
                .toUriString();

        return URL;
    }

    /**
     * @description Naver 로그인을 위하여 Access_tokin을 발급받음
     * @param resValue
     *          1) code: 토큰 발급용 1회용 코드
     *          2) state: CORS 를 방지하기 위한 임의의 토큰
     * @param grant_type
     *          1) 발급:'authorization_code'
     *          2) 갱신:'refresh_token'
     *          3) 삭제: 'delete'
     * @return
     */
    public NaverLoginDTO accessToken(Map<String, String> resValue, String grant_type) {
        final String uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com")
                .path("/oauth2.0/token")
                .queryParam("grant_type", grant_type)
                .queryParam("client_id", this.clientId)
                .queryParam("client_secret", "FAK_Leusjl")
                .queryParam("code", resValue.get("code"))
                .queryParam("state", resValue.get("state"))
                .queryParam("refresh_token", resValue.get("refresh_token")) // Access_token 갱신시 사용
                .build()
                .encode()
                .toUriString();

        WebClient webClient = WebClient.create();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NaverLoginDTO.class)
                .block();
    }

    // ----- 프로필 API 호출 (Unique한 id 값을 가져오기 위함) -----
    public UserInfo requestUserInfo(NaverLoginDTO naverLoginDTO){
        final String profileUri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/nid/me")
                .build()
                .encode()
                .toUriString();

        WebClient webClient = WebClient.create();

        return webClient
                .get()
                .uri(profileUri)
                .header("Authorization", "Bearer " + naverLoginDTO.getAccess_token())
                .retrieve()
                .bodyToMono(NaverLoginDTOResponse.class)
                .block()
                .getResponse();
    }
}
