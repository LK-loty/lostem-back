package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Lost")
@Getter
public class LostChatRoom extends ChatRoom {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false, insertable = false, updatable = false)
    private PostLost postLost;
}
