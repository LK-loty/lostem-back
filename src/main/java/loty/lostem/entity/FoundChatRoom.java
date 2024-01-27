package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("FOUND")
@Getter
public class FoundChatRoom extends ChatRoom {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false, insertable = false, updatable = false)
    private PostFound postFound;
}
