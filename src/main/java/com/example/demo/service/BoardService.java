package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PostDTO;
import com.example.demo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private BoardRepository repo;

    public List<Map<String,Object>> listBoards() { return repo.findAllBoards(); }

    public Long createBoard(BoardDTO b) { return repo.createBoard(b); }

    public Long findBoardIdByGrid(int gridX, int gridY) { return repo.findBoardIdByGrid(gridX, gridY); }

    /**
     * Ensure a board exists for given grid coordinates. If not, create the posts table and a board record.
     * Returns the board id (existing or newly created).
     */
    public Long ensureBoardForGrid(int gridX, int gridY, Double centerLng, Double centerLat) {
        Long existing = repo.findBoardIdByGrid(gridX, gridY);
        if (existing != null) return existing;
        // create posts table for this grid
        repo.ensurePostsTableForGrid(gridX, gridY);
        // create a board record
        BoardDTO b = new BoardDTO();
        b.setName("grid_" + gridX + "_" + gridY);
        b.setGrid_x(gridX);
        b.setGrid_y(gridY);
        if (centerLng != null) b.setCenter_lng(java.math.BigDecimal.valueOf(centerLng));
        if (centerLat != null) b.setCenter_lat(java.math.BigDecimal.valueOf(centerLat));
        Long id = repo.createBoard(b);
        return id;
    }

    public List<Map<String,Object>> listPosts(Long boardId) { return repo.findPostsByBoardId(boardId); }

    public Long createPost(Long boardId, PostDTO p) { return repo.createPost(boardId, p); }

    // Hash password (SHA-256 hex) helper
    private String hashPassword(String pw) {
        if (pw == null) return null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (Exception e) { return null; }
    }

    public Long createPostWithPassword(Long boardId, PostDTO p) {
        if (p != null && p.getPassword() != null) {
            String hashed = hashPassword(p.getPassword());
            // store hashed password in content of DTO's password field
            p.setPassword(hashed);
        }
        return repo.createPost(boardId, p);
    }

    public boolean verifyPostPassword(Long boardId, Long postId, String plainPassword) {
        String hashed = hashPassword(plainPassword);
        return repo.verifyPostPassword(boardId, postId, hashed);
    }

    public boolean updatePost(Long boardId, Long postId, String author, String content) {
        return repo.updatePost(boardId, postId, author, content);
    }

    public boolean deletePost(Long boardId, Long postId) {
        return repo.deletePost(boardId, postId);
    }

    public List<Map<String,Object>> activity(Long boardId, int hours) { return repo.getActivity(boardId, hours); }
}
