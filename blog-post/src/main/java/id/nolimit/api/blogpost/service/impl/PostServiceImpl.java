package id.nolimit.api.blogpost.service.impl;

import id.nolimit.api.blogpost.entity.Post;
import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.exception.CustomRuntimeException;
import id.nolimit.api.blogpost.model.request.PostRequest;
import id.nolimit.api.blogpost.model.request.SearchPostRequest;
import id.nolimit.api.blogpost.model.response.PostResponse;
import id.nolimit.api.blogpost.repository.PostRepository;
import id.nolimit.api.blogpost.service.PostService;
import id.nolimit.api.blogpost.util.CommonFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class, CustomRuntimeException.class, ResponseStatusException.class})
    public PostResponse createPost(User user, PostRequest postRequest) {
        Post post = new Post();
        post.setContent(postRequest.content());
        post.setUser(user);
        Post createPost = postRepository.save(post);
        return PostResponse.builder()
                .id(createPost.getId())
                .content(createPost.getContent())
                .createdAt(createPost.getCreatedAt())
                .updatedAt(createPost.getUpdatedAt())
                .authorId(createPost.getUser().getId())
                .build();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, CustomRuntimeException.class, ResponseStatusException.class})
    public PostResponse editPost(User user, Long id, PostRequest postRequest) {
        Optional<Post> post = postRepository.findByIdAndUser(id, user);
        if (post.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post does not exists.");
        }
        post.get().setContent(postRequest.content());
        post.get().setUser(user);
        Post editPost = postRepository.save(post.get());
        return PostResponse.builder()
                .id(editPost.getId())
                .content(editPost.getContent())
                .createdAt(editPost.getCreatedAt())
                .updatedAt(editPost.getUpdatedAt())
                .authorId(editPost.getUser().getId())
                .build();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, CustomRuntimeException.class, ResponseStatusException.class})
    public void deletePost(User user, Long id) {
        Optional<Post> post = postRepository.findByIdAndUser(id, user);
        if (post.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post does not exists.");
        }
        postRepository.delete(post.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(SearchPostRequest searchPostRequest) {
        Pageable pageable = PageRequest.of(
                searchPostRequest.getPage(),
                searchPostRequest.getSize(),
                Sort.by(
                        CommonFunction.getSortDirection(searchPostRequest.getOrder()),
                        searchPostRequest.getSort()
                )
        );

        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = posts.stream()
                .map(this::toPostResponse)
                .toList();

        return new PageImpl<>(postResponses, pageable, posts.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post does not exists.");
        }
        return toPostResponse(post.get());
    }

    private PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .authorId(post.getUser().getId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
