package com.project.forum.mapper;

import com.project.forum.dto.responses.comment.CommentResponse;
import com.project.forum.enity.Comments;
import com.project.forum.enity.Users;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-07T16:27:48+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponse toCommentResponse(Comments comments) {
        if ( comments == null ) {
            return null;
        }

        CommentResponse.CommentResponseBuilder commentResponse = CommentResponse.builder();

        commentResponse.user_name( commentsUsersName( comments ) );
        commentResponse.user_img( commentsUsersImg( comments ) );
        commentResponse.id( comments.getId() );
        commentResponse.content( comments.getContent() );
        commentResponse.created_at( comments.getCreated_at() );

        return commentResponse.build();
    }

    private String commentsUsersName(Comments comments) {
        if ( comments == null ) {
            return null;
        }
        Users users = comments.getUsers();
        if ( users == null ) {
            return null;
        }
        String name = users.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String commentsUsersImg(Comments comments) {
        if ( comments == null ) {
            return null;
        }
        Users users = comments.getUsers();
        if ( users == null ) {
            return null;
        }
        String img = users.getImg();
        if ( img == null ) {
            return null;
        }
        return img;
    }
}
