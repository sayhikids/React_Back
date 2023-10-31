package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

// 이 클래스는 사용자의 정보를 받아서  JWT 토큰을 생성하는 역할을 한다.
// 나중에 이 토큰을 컨트롤러에 응답시 같이 보내게 된다.
// 정의 방법은 형식화 되어 있으니 외우지 않아도 됨 //일반적으로 가져다 쓴다.

@Slf4j
@Service
public class TokenProvider {
    // 제일 먼저 사용자의 PK와 조합될 서버의 SecretKey 선언 // 아무거나 해도 된다. 길게
    private static final String SECRET_KEY = "FALDkekjfdslljkalsfalksdfasdfapo0932189fsdakjlfsadljkfdsjklhreai324u5936798fldjsdsdfkljfsdlkjfsdlkjfsdalkfsdhglkjwerjlsFSDfdsaadkl12332lkeioweduoiu2348reashhjfsadkjkfsdhlkalafsdkkjlsadflkjfDGJLDGSFJDGLGDSJfsadjklsdfalkjhsdfajkhfdshfdshjfdshlfdslfdsahlfsd432ua89fad";

    // 컨트롤러에서 서비스로 등록시켜 사용자 정보를 담고있는 Entity 를 전달해서 암호화된 토큰을 생성하는 메서드 정의하기
    public String create(UserEntity userEntity) {

        // 토큰의 유효기간 설정
        Date expireDate = Date.from(Instant.now().plus(10, ChronoUnit.DAYS)); // 10일간 유효간 토큰 날짜 설정

        /*
         * Jwt.builder() 를 이용해서 암호화된 토큰을 생성하는데 결과는 아래처럼 구성될것
         * {//header 부분
         * "alg": "여러 알고리즘 중 하나"
         * },
         * {//payload 부분
         * "sub": "특정값...",
         * "iss": "스프링부트 생성 시 gruop id",
         * "iat": 특정 숫자값,
         * "exp": long 값
         * },
         * {//Signature 부분 // 위 SECRET_KEY를 이용해서 서명한 값이 여기에 할당된다.
         * }
         */
        return Jwts.builder()
                //header 에 들어갈 내용 및 서명을 위한 비밀키
                            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                            //payload에 들어갈 내용 작성
                            .setSubject(userEntity.getId())// user pk와 비밀키로 생성한 값
                            .setIssuer("fullstack2")
                            .setIssuedAt(expireDate)
                            .compact();
    }

    
    //사용자로부터 전달받은 토큰을 64 Decoding 해서
    //header, payload로 부터 전달 받은 값 들을 서버의 비미킬 이용 서명 후, 넘어온 token 의 서명과 비교한다.
    //만약 위조되지 않았다면 payload(Claims 객체)를  리턴하고 아닌 경우에는 예외를 보낸다.
    //이 중 사용자의 id와 조합해서 token을 생성 했으니 id 값도 필요하다.
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }





}
