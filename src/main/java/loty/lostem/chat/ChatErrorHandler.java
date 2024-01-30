package loty.lostem.chat;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }

    // websocket endpoint에서 에러가 생길 경우 stompSubProtocolErrorHandler 의 handleClientMessageProcessingError 호출하여 처리 후 handlerInternal() 호출하여 메시지 전송하므로 커스텀하려면 오버라이딩
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        if(ex.getCause().getMessage().equals("jwt")) {
            return jwtException(clientMessage, ex);
        }

        if(ex.getCause().getMessage().equals("error")) {
            return messageException(clientMessage, ex);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    //메시지 예외
    private Message<byte[]> messageException(Message<byte[]> clientMessage, Throwable ex) {
        return errorMessage(ErrorCode.INVALID_MESSAGE);
    }

    //jwt 예외
    private Message<byte[]> jwtException(Message<byte[]> clientMessage, Throwable ex) {
        return errorMessage(ErrorCode.INVALID_TOKEN);
    }

    //메시지 생성
    private Message<byte[]> errorMessage(ErrorCode errorCode) {
        String code = String.valueOf(errorCode.getMessage());

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(String.valueOf(errorCode.getStatus()));
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
