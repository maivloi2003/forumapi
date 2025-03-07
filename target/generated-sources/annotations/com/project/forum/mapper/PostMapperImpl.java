package com.project.forum.mapper;

import com.project.forum.dto.responses.post.PostResponse;
import com.project.forum.enity.Language;
import com.project.forum.enity.Posts;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-07T14:09:28+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostResponse toPostsResponse(Posts posts) {
        if ( posts == null ) {
            return null;
        }

        PostResponse.PostResponseBuilder postResponse = PostResponse.builder();

        postResponse.language( postsLanguageName( posts ) );
        postResponse.id( posts.getId() );
        postResponse.type_post( posts.getType_post() );
        postResponse.created_at( posts.getCreated_at() );
        postResponse.updated_at( posts.getUpdated_at() );

        return postResponse.build();
    }

    private String postsLanguageName(Posts posts) {
        if ( posts == null ) {
            return null;
        }
        Language language = posts.getLanguage();
        if ( language == null ) {
            return null;
        }
        String name = language.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
