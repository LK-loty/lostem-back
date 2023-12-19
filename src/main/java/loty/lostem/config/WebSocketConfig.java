package loty.lostem.config;

import lombok.RequiredArgsConstructor;
import loty.lostem.chat.StompHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 소켓 연결을 위한 설정
        registry.addEndpoint("/api/websocket").setAllowedOriginPatterns("http://localhost:3000").withSockJS();
        //registry.setErrorHandler(chatErrorHandler);
        // stomp 접속 주소, 모든 ip에 대해(실제 서버에서는 서버 주소 origin만 적기. SOP 때문 >> cors 설정 필요), 클라에서 socketJS 사용시 포함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //sub하는 클라이언트에게 메시지 전달
        //메시지 받을 때 경로. 앞에 해당 경로들이 붙으면 messagebroker가 해당 경로 가로챔, 여러 개 등록 도 가능 > queue는 1:1, topic은 1:n
        registry.enableSimpleBroker("/queue", "/topic");
        //클라이언트의 send요청 처리
        //메시지 보낼 때 관련 경로 설정. 클라가 메시지 보낼 때 경로 앞에 /pub 붙어있으면 broker로 보내짐 >> 클라에서 서버 전달 시 /app/api/websocket/주소 형식
        registry.setApplicationDestinationPrefixes("/app");

    }

    @Override  // 일반 통신에서 웹소켓 사용하게 되면 헤더 사용 불가하므로 설정 추가. message 객체가 있으면 가로채서 설정 후 리턴
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}