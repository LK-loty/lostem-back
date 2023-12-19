package loty.lostem.chat;

import lombok.RequiredArgsConstructor;
import loty.lostem.jwt.TokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final String BEARER_PREFIX = "Bearer ";

    // 웹소켓으로 들어온 요청이 처리되기 전 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        //// 웹소켓 연결 시 헤더 jwt 검증
        if (StompCommand.CONNECT == headerAccessor.getCommand()) {
            tokenProvider.validateToken(headerAccessor.getFirstNativeHeader("token"));
        }
        // return message;

        // 헤더 토큰 얻기
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        // 토큰 자르기. fixme 토큰 자르는 로직 validate 로 리팩토링

        if(authorizationHeader == null || authorizationHeader.equals("null")){
            throw new MessageDeliveryException("메세지 예외");
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        if(headerAccessor.getCommand() == StompCommand.CONNECT) { //StompCommand.CONNECT.equals(accessor.getCommand())
            if(!tokenProvider.validateToken(headerAccessor.getFirstNativeHeader("token")))
                try {
                    throw new AccessDeniedException("");
                } catch (AccessDeniedException e) {
                    e.printStackTrace();
                }
        }
        return message;
    }

    //
    /*private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority(MemberRole.USER.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
    }*/

}
