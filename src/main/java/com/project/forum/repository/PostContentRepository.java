package com.project.forum.repository;

import com.project.forum.dto.responses.post.PostContentResponse;
import com.project.forum.enity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, String> {

    @Query("SELECT NEW com.project.forum.dto.responses.post.PostContentResponse( " +
            "pc.id, pc.title, pc.content, pc.img_url,pc.posts.id) " +
            "FROM post_content pc " +
            "WHERE pc.posts.id = :postId")
    Optional<PostContentResponse> findPostContentByPosts_Id(@Param("postId") String postId);
}
