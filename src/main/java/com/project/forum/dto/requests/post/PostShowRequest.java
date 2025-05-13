package com.project.forum.dto.requests.post;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PostShowRequest {

    Boolean show;

    Boolean deleted;
}
