package com.project.forum.enity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "post_content")
public class PostContent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String title;

    String content;

    String img_url;

    @OneToOne
    @JoinColumn(name = "post_id")
    Posts posts;

}
