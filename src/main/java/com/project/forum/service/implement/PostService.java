package com.project.forum.service.implement;

import com.project.forum.dto.requests.post.PostShowRequest;
import com.project.forum.dto.responses.post.PostResponse;
import com.project.forum.dto.responses.post.PostTotalResponse;
import com.project.forum.dto.responses.post.TopInteractionPostResponse;
import com.project.forum.enity.*;
import com.project.forum.enums.ErrorCode;
import com.project.forum.enums.RolesCode;
import com.project.forum.exception.WebException;
import com.project.forum.repository.*;
import com.project.forum.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService implements IPostService {

    PostsRepository postsRepository;

    PostPollRepository postPollRepository;

    PostContentRepository postContentRepository;

    UsersRepository usersRepository;

    LikesRepository likesRepository;

    AdvertisementRepository advertisementRepository;

    PostReportsRepository postReportsRepository;

    @Override
    public Page<PostResponse> findAllPost(Integer page, Integer size, String content, String language) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size - 1);
        Page<PostResponse> postPage;

        String userId = null;
        if (!username.equals("anonymousUser")) {
            userId = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND))
                    .getId();
        }

        postPage = postsRepository.findAllPosts(content, userId, language, pageable);
        List<PostResponse> resultList = new ArrayList<>(postPage.getContent());

        if (userId != null) {
            for (PostResponse post : resultList) {
                if (likesRepository.existsByPosts_IdAndUsers_Id(post.getId(), userId)) {
                    post.setUser_like(true);
                }
            }
        }

        Page<Advertisement> randomAdsPage = advertisementRepository.findRandomAdvertisement(PageRequest.of(0, 1), language);
        if (!randomAdsPage.isEmpty() && !postPage.isEmpty()) {
            Advertisement randomAd = randomAdsPage.getContent().get(0);
            Optional<PostResponse> adPostOpt = postsRepository.findPostById(randomAd.getPosts().getId(), userId);
            adPostOpt.ifPresent(adPost -> {
                Advertisement advertisement = advertisementRepository.findAdsByPostId(adPost.getId()).orElseThrow(() -> new WebException(ErrorCode.E_ADS_NOT_FOUND));
                advertisement.setViews(advertisement.getViews() + 1);
                adPost.setAds(true);
                resultList.add(adPost);
            });
            advertisementRepository.save(randomAd);
        }

        long total = postPage.getTotalElements() + (resultList.size() > postPage.getContent().size() ? 1 : 0);
        return new PageImpl<>(resultList, PageRequest.of(page, size), total);
    }


    @Override
    public PostResponse findPostById(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousUser")) {
            return postsRepository.findPostById(id, null).orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));
        }
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        PostResponse postResponse = postsRepository.findPostById(id, users.getId().toString()).orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));
        if (likesRepository.existsByPosts_IdAndUsers_Id(postResponse.getId(), users.getId())) {
            postResponse.setUser_like(true);

        }
        return postResponse;
    }

    @Override
    public boolean deletePostById(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));
        if (posts.getUsers().getId().equals(users.getId()) || users.getRoles().equals(RolesCode.ADMIN) || users.getRoles().equals(RolesCode.EMPLOYEE)) {
            postsRepository.delete(posts);
            return true;
        } else
            return false;
    }

    @Override
    public Page<PostResponse> userPost(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        Page<PostResponse> postPageResponseList = postsRepository.userPost(users.getId().toString(), pageable);
        for (PostResponse post : postPageResponseList) {
            if (likesRepository.existsByPosts_IdAndUsers_Id(post.getId(), users.getId())) {
                post.setUser_like(true);
            }
        }
        return postPageResponseList;
    }

    @Override
    public Page<PostResponse> findPostByIdUser(String IdUser, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Users users = usersRepository.findById(IdUser)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));

        Page<PostResponse> postPageResponseList = postsRepository.userPost(users.getId().toString(), pageable);
        for (PostResponse post : postPageResponseList) {
            if (likesRepository.existsByPosts_IdAndUsers_Id(post.getId(), users.getId())) {
                post.setUser_like(true);
            }
        }
        return postPageResponseList;
    }

    @Override
    public Page<PostResponse> findAllPostAdmin(Integer page, Integer size, String content, String language) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postsRepository.findAllPostsAdmin(content, null, language, pageable);

        return posts;
    }

    @Override
    public PostTotalResponse postTotal(LocalDateTime start, LocalDateTime end) {
        return postsRepository.getPostStats(start, end);
    }

    @Override
    public PostResponse showPostById(String id, PostShowRequest postShowRequest) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));
        if (postShowRequest.getShow() != null) {
            posts.setPostShow(postShowRequest.getShow());
        }

        if (postShowRequest.getDeleted() != null) {
            posts.setDeleted(postShowRequest.getDeleted());
        }
        postsRepository.save(posts);
        return PostResponse.builder()
                .isShow(posts.isPostShow())
                .isDeleted(posts.isDeleted())
                .type_post(posts.getType_post())
                .created_at(posts.getCreated_at())
                .id(posts.getId())
                .build();
    }

    @Override
    public List<TopInteractionPostResponse> getTopInteractionPosts(LocalDateTime from, LocalDateTime to, int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<TopInteractionPostResponse> results = postsRepository.findTopInteractionPosts(from, to, pageable);
            return results != null ? results : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public PostResponse changeStatusPost(String id, PostShowRequest postShowRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new WebException(ErrorCode.E_POST_NOT_FOUND));

        boolean isOwnerOrAdmin = posts.getUsers().getId().equals(users.getId()) || users.getRoles().equals(RolesCode.ADMIN);

        Optional<PostReports> postReport = postReportsRepository.findPostReportsByPosts_Id(id);

        if (postReport.isEmpty() || posts.isPostShow()) {
            if (isOwnerOrAdmin) {
                if (postShowRequest.getShow() != null) {
                    posts.setPostShow(postShowRequest.getShow());
                }

                if (postShowRequest.getDeleted() != null || posts.isPostShow() == false) {
                    posts.setDeleted(postShowRequest.getDeleted());
                }
                postsRepository.save(posts);
                return PostResponse.builder()
                        .isShow(posts.isPostShow())
                        .isDeleted(posts.isDeleted())
                        .type_post(posts.getType_post())
                        .created_at(posts.getCreated_at())
                        .id(posts.getId())
                        .build();
            } else {
                throw new WebException(ErrorCode.E_FORBIDDEN);
            }
        }

        throw new WebException(ErrorCode.E_FORBIDDEN);
    }

}
