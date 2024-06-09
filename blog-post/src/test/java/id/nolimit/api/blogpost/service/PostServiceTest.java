package id.nolimit.api.blogpost.service;

import id.nolimit.api.blogpost.entity.Post;
import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.request.PostRequest;
import id.nolimit.api.blogpost.model.request.SearchPostRequest;
import id.nolimit.api.blogpost.model.response.PostResponse;
import id.nolimit.api.blogpost.repository.PostRepository;
import id.nolimit.api.blogpost.service.impl.PostServiceImpl;
import id.nolimit.api.blogpost.util.CommonFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository);
    }

    @Test
    void givenValidRequest_whenExecuteCreatePost_shouldGivenValue() {
        PostRequest postRequest = PostRequest.builder()
                .content("Content Example")
                .build();
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(mockPost());
        PostResponse postResponse = postService.createPost(mockUser(), postRequest);
        Assertions.assertEquals("Content Example", postResponse.content());
    }

    @Test
    void givenInvalidPostRequest_whenExecuteEditPost_shouldThrowException() {
        PostRequest postRequest = PostRequest.builder()
                .content("Content Example")
                .build();
        Mockito.when(postRepository.findByIdAndUser(1L, mockUser())).thenReturn(Optional.empty());
        Exception e = Assertions.assertThrows(Exception.class, () -> postService.editPost(mockUser(), 1L, postRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Post does not exists.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenValidPostRequest_whenExecuteEditPost_shouldGivenValue() {
        PostRequest postRequest = PostRequest.builder()
                .content("Content Example")
                .build();
        Mockito.when(postRepository.findByIdAndUser(1L, mockUser())).thenReturn(Optional.of(mockPost()));
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(mockPost());
        PostResponse postResponse = postService.editPost(mockUser(), 1L, postRequest);
        Assertions.assertEquals(1L, postResponse.id());
        Assertions.assertEquals("Content Example", postResponse.content());
    }

    @Test
    void givenInvalidPostDeleteRequest_whenExecuteDeletePost_shouldThrowException() {
        Mockito.when(postRepository.findByIdAndUser(1L, mockUser())).thenReturn(Optional.empty());
        Exception e = Assertions.assertThrows(Exception.class, () -> postService.deletePost(mockUser(), 1L));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Post does not exists.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenValidPostDeleteRequest_whenExecuteDeletePost_shouldGivenValue() {
        Mockito.when(postRepository.findByIdAndUser(1L, mockUser())).thenReturn(Optional.of(mockPost()));
        postService.deletePost(mockUser(), 1L);
    }

    @Test
    void givenValidRequest_whenExecuteGetAllPosts_shouldGivenValue() {
        SearchPostRequest searchPostRequest = SearchPostRequest.SearchPostRequestBuilder()
                .page(0)
                .size(1)
                .order("asc")
                .sort("id")
                .build();

        Pageable pageable = PageRequest.of(
                searchPostRequest.getPage(),
                searchPostRequest.getSize(),
                Sort.by(
                        CommonFunction.getSortDirection(searchPostRequest.getOrder()),
                        searchPostRequest.getSort()
                )
        );

        Mockito.when(postRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(mockPost()), pageable, 1L));
        Page<PostResponse> postResponses = postService.getAllPosts(searchPostRequest);
        Assertions.assertEquals(1L, postResponses.getTotalElements());
        Assertions.assertEquals("Content Example", postResponses.getContent().get(0).content());
    }

    @Test
    void givenInvalidId_whenExecuteGetPostById_shouldThrowException() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());
        Exception e = Assertions.assertThrows(Exception.class, () -> postService.getPostById(1L));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Post does not exists.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenValidId_whenExecuteGetPostById_shouldGivenValue() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost()));
        PostResponse postResponse = postService.getPostById(1L);
        Assertions.assertEquals(1L, postResponse.id());
        Assertions.assertEquals("Content Example", postResponse.content());
    }

    private User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@example.com");
        user.setName("Test Name");
        user.setPassword("admin1234");
        return user;
    }

    private Post mockPost() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Content Example");
        post.setUser(mockUser());
        post.setCreatedAt(System.currentTimeMillis());
        post.setUpdatedAt(System.currentTimeMillis());
        return post;
    }

}
