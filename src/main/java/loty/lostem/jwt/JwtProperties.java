package loty.lostem.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Getter
@Component
public class JwtProperties implements InitializingBean {

    @Value("${spring.jwt.issuer}")
    private String issuer;
    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.token-validity}")
    private long tokenValidity;

    @Value(("${spring.jwt.refresh-validity}"))
    private long refreshTokenValidity;

    @Value("${spring.jwt.header}")
    private String header;

    private Key key;

    @Override  // InitializingBean 이용한 이유 : 빈이 생성되고 주입받은 후에 base64 decode해서 key 변수에 할당
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
