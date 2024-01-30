package loty.lostem.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.chat.StompHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 소켓 연결을 위한 설정
        registry.addEndpoint("/api/websocket").setAllowedOriginPatterns("*").withSockJS(); // ws://localhost:8080/api/websocket 으로 접근
        log.info("소켓 연결을 설정합니다.");
        //registry.setErrorHandler(chatErrorHandler);
        // stomp 접속 주소, ("*")이라면 모든 ip에 대해(실제 서버에서는 서버 주소 origin만 적기. SOP 때문 >> cors 설정 필요), 클라에서 socketJS 사용시 포함
        // 엔드포인트 "/api/websocket"로 설정, ws://localhost:8080/api/websocket 으로 요청이 들어오면 웹소켓 통신 진행
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //sub하는 클라이언트에게 메시지 전달 >> sub 로 시작하는 stomp 메세지는 브로커로 라우팅함
        //메시지 받을 때 경로. 앞에 해당 경로들이 붙으면 messagebroker가 해당 경로 가로챔, 여러 개 등록 도 가능 > queue는 1:1, topic은 1:n
        registry.enableSimpleBroker("/sub");
        //클라이언트의 send요청 처리 >> pub 으로 시작하는 stomp 메세지의 경로는 @controller @MessageMaping 메서드로 라우팅
        //메시지 보낼 때 관련 경로 설정. 클라가 메시지 보낼 때 경로 앞에 /pub 붙어있으면 broker로 보내짐 >> 클라에서 서버 전달 시 /app/api/websocket/주소 형식
        //applicationDestinationPrefixes와 @MessageMapping 경로 병합
        registry.setApplicationDestinationPrefixes("/pub");

        /*registry.enableStompBrokerRelay("/topic") // Redis를 통해 메시지 브로커 설정
                .setRelayHost("localhost")
                .setRelayPort(6379);
        registry.setApplicationDestinationPrefixes("/app");*/
    }

    @Override  // 일반 통신에서 웹소켓 사용하게 되면 헤더 사용 불가하므로 설정 추가. message 객체가 있으면 가로채서 설정 후 리턴. client 로부터 들어오는 MessageChannel 구성하는 역할
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}