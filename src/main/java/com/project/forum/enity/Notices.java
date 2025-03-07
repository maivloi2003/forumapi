package com.project.forum.enity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "notices")
public class Notices {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String message;

    @CreatedDate()
    LocalDateTime created_at;

    boolean status;

    String post_id;

    String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;
}
