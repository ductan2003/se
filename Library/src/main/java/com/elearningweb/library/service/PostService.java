package com.elearningweb.library.service;

import com.elearningweb.library.dto.CommentDto;
import com.elearningweb.library.dto.ExamDto;
import com.elearningweb.library.dto.PostDto;
import com.elearningweb.library.dto.UserDto;
import com.elearningweb.library.model.Post;
import com.elearningweb.library.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto insert(PostDto postDto, MultipartFile image) throws Exception;
    void savePost(PostDto postDto, String image);
    PostDto updatePost(PostDto postDto, Long postId);
    boolean deletePost(Long postId);
    List<PostDto> searchByTitle (String title);
    List<Post> getAllPosts();
    Post getPost(Long id);
    PostDto getPostById (Long id);
    Post find(Long id);
    CommentDto createComment(Long postId, String text, String creatorName);
    List<CommentDto> getAllComments(Long postId);
}
