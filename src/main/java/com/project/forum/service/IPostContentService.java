package com.project.forum.service;

import com.project.forum.dto.requests.post.CreatePostContentDto;
import com.project.forum.dto.responses.post.PostContentResponse;
import com.project.forum.dto.responses.post.PostResponse;

public interface IPostContentService {

    PostContentResponse findPostContentByPostId(String postId);

    PostResponse create(CreatePostContentDto createPostContentDto);

}
