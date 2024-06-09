package id.nolimit.api.blogpost.controller;

import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.BaseResponse;
import id.nolimit.api.blogpost.model.PagingResponse;
import id.nolimit.api.blogpost.model.request.PostRequest;
import id.nolimit.api.blogpost.model.request.SearchPostRequest;
import id.nolimit.api.blogpost.model.response.PostResponse;
import id.nolimit.api.blogpost.service.AuthService;
import id.nolimit.api.blogpost.service.PostService;
import id.nolimit.api.blogpost.util.MessageStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController extends BaseController {

    private final PostService postService;

    public PostController(AuthService authService, PostService postService) {
        super(authService);
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<PostResponse>> createPost(@RequestBody @Valid PostRequest postRequest) {
        User user = getCurrentUser();
        PostResponse postResponse = postService.createPost(user, postRequest);
        BaseResponse<PostResponse> postResponseBaseResponse = BaseResponse.<PostResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(postResponse)
                .build();
        return new ResponseEntity<>(postResponseBaseResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<PostResponse>> editPost(@PathVariable Long id, @RequestBody @Valid PostRequest postRequest) {
        User user = getCurrentUser();
        PostResponse postResponse = postService.editPost(user, id, postRequest);
        BaseResponse<PostResponse> postResponseBaseResponse = BaseResponse.<PostResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(postResponse)
                .build();
        return new ResponseEntity<>(postResponseBaseResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Void>> deletePost(@PathVariable Long id) {
        User user = getCurrentUser();
        postService.deletePost(user, id);
        BaseResponse<Void> postResponseBaseResponse = BaseResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(null)
                .build();
        return new ResponseEntity<>(postResponseBaseResponse, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "orderBy", required = false, defaultValue = "asc") String orderBy
    ) {

        SearchPostRequest searchPostRequest = SearchPostRequest.SearchPostRequestBuilder()
                .page(page - 1)
                .size(size)
                .order(orderBy)
                .sort(sortBy)
                .build();

        Page<PostResponse> postResponses = postService.getAllPosts(searchPostRequest);
        BaseResponse<List<PostResponse>> baseResponse = BaseResponse.<List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .page(
                        PagingResponse.builder()
                                .currentPage(postResponses.getNumber() + 1)
                                .totalPage(postResponses.getTotalPages())
                                .size(postResponses.getSize())
                                .totalRecord(postResponses.getTotalElements())
                                .build()
                )
                .data(postResponses.getContent())
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostResponse postResponse = postService.getPostById(id);
        BaseResponse<PostResponse> postResponseBaseResponse = BaseResponse.<PostResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(postResponse)
                .build();
        return new ResponseEntity<>(postResponseBaseResponse, HttpStatus.OK);
    }


}
