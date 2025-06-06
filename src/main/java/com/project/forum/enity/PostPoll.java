package com.project.forum.enity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.forum.enums.TypePoll;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "post_poll", indexes = {
        @Index(name = "idx_post_poll_post_id", columnList = "post_id")
})
@EntityListeners(AuditingEntityListener.class)
public class PostPoll {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String question;

    String typePoll;

    @OneToOne
    @JoinColumn(name = "post_id")
    Posts posts;

    @OneToMany(mappedBy = "postPoll",cascade = CascadeType.REMOVE)
    List<PollOptions> pollOptions;

}
