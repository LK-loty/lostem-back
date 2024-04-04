package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING, length = 10)
//@DiscriminatorValue("POST")
@Getter
@Builder
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private String postType;

    @Column
    private Long postId;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestUserId")
    private User guestUser;*/

    @Column
    private String hostUserTag;

    @Column
    private String guestUserTag;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;



    /*public static ChatRoom createChatRoom(String hostUserTag, String guestUserTag) {
        return ChatRoom.builder()
                .hostUserTag(hostUserTag)
                .guestUserTag(guestUserTag)
                .build();
    }*/

    public ChatRoom(String postType, Long postId, String hostUserTag, String guestUserTag) {
        this.postType = postType;
        this.postId = postId;
        this.hostUserTag = hostUserTag;
        this.guestUserTag = guestUserTag;
    }

    public String getLastMessage() {
        return chatMessages.stream()
                .reduce((first, second) -> second)
                .map(ChatMessage::getMessage)
                .orElse(null);
    }

    public LocalDateTime getLastMessageTime() {
        return chatMessages.stream()
                .map(ChatMessage::getTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
    /*public static class Builder {
        private String hostUserTag;
        private String guestUserTag;

        public Builder hostUserTag(String hostUserTag) {
            this.hostUserTag = hostUserTag;
            return this;
        }

        public Builder guestUserTag(String guestUserTag) {
            this.guestUserTag = guestUserTag;
            return this;
        }

        public Builder initializeCommonFields(ChatRoom chatRoom) {
            this.hostUserTag = chatRoom.getHostUserTag();
            this.guestUserTag = chatRoom.getGuestUserTag();
            return this;
        }

        public ChatRoom build() {
            return new ChatRoom(this);
        }
    }

    protected ChatRoom(Builder builder) {
        this.hostUserTag = builder.hostUserTag;
        this.guestUserTag = builder.guestUserTag;
    }*/
}
