package id.nolimit.api.blogpost.service;

import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.request.PostRequest;
import id.nolimit.api.blogpost.model.request.SearchPostRequest;
import id.nolimit.api.blogpost.model.response.PostResponse;
import org.springframework.data.domain.Page;

public interface PostService {

    PostResponse createPost(User user, PostRequest postRequest);
    PostResponse editPost(User user, Long id, PostRequest postRequest);
    void deletePost(User user, Long id);
    Page<PostResponse> getAllPosts(SearchPostRequest searchPostRequest);
    PostResponse getPostById(Long id);

}
