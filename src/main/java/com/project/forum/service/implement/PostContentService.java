package com.project.forum.service.implement;

import com.project.forum.dto.requests.post.CreatePostContentDto;
import com.project.forum.dto.responses.post.PostContentResponse;
import com.project.forum.dto.responses.post.PostResponse;
import com.project.forum.enity.Language;
import com.project.forum.enity.PostContent;
import com.project.forum.enity.Posts;
import com.project.forum.enity.Users;
import com.project.forum.enums.ErrorCode;
import com.project.forum.enums.TypePost;
import com.project.forum.exception.WebException;
import com.project.forum.mapper.PostMapper;
import com.project.forum.repository.LanguageRepository;
import com.project.forum.repository.PostContentRepository;
import com.project.forum.repository.PostsRepository;
import com.project.forum.repository.UsersRepository;
import com.project.forum.service.IPostContentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostContentService implements IPostContentService {

    PostContentRepository postContentRepository;

    PostsRepository postsRepository;

    UsersRepository usersRepository;

    PostMapper postMapper;

    LanguageRepository languageRepository;

    @Override
    public PostContentResponse findPostContentByPostId(String postId) {
        return postContentRepository.findPostContentByPosts_Id(postId).orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));
    }

    @Override
    public PostResponse create(CreatePostContentDto createPostContentDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        Language language = languageRepository.findByName(createPostContentDto.getLanguage()).orElseThrow(() -> new WebException(ErrorCode.E_LANGUAGE_NOT_FOUND) );
        Posts posts = Posts.builder()
                .language(language)
                .type_post(TypePost.CONTENT.toString())
                .users(user)
                .updated_at(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .build();
        postsRepository.save(posts);
        PostContent postContent = PostContent.builder()
                .content(createPostContentDto.getContent())
                .title(createPostContentDto.getTitle())
                .img_url(createPostContentDto.getImg_url())
                .posts(posts)
                .build();
        postContentRepository.save(postContent);

        return postMapper.toPostsResponse(posts);
    }

}
