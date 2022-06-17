package com.example.crud.service;

import com.example.crud.Exception.CRUDException;
import com.example.crud.dto.PostRequest;
import com.example.crud.dto.PostResponse;
import com.example.crud.model.Post;
import com.example.crud.model.Subreddit;
import com.example.crud.model.User;
import com.example.crud.repository.*;
import com.github.kevinsawicki.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new CRUDException(postRequest.getSubredditName()));
        User user = authService.getCurrentUser();
        postRepository.save(mapPost(postRequest, subreddit, user));
    }

    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CRUDException(id.toString()+" Post Not Found"));
        return mapToDto(post);
    }

    @Transactional
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new CRUDException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .commentCount(countComments(post))
                .voteCount(voteRepository.findByPost(post).size())
                .duration(getDuration(post))
                .build();
    }
    private Post mapPost(PostRequest postRequest, Subreddit subreddit, User user) {
        return Post.builder()
                .postId(postRequest.getPostId())
                .postName(postRequest.getPostName())
                .description(postRequest.getDescription())
                .subreddit(subreddit)
                .url(postRequest.getUrl())
                .createdDate(java.time.Instant.now())
                .user(user)
                .voteCount(0)
                .build();
    }
    Integer countComments(Post post){
        return commentRepository.findByPost(post).size();
    }
    String getDuration(Post post){
        TimeAgo time = new TimeAgo();
        return time.timeAgo(post.getCreatedDate().toEpochMilli());
    }
    @Transactional
    public List<PostResponse> getPostsByUsername(String username) {
        Optional<User> user2 = userRepository.findByUsername(username);
        System.out.println("\n----------------------------");

        System.out.println("\n----------------------------");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));


        return postRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}