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

    public List<Map<String,Object>> listPosts(Long boardId) { return repo.findPostsByBoardId(boardId); }

    public Long createPost(Long boardId, PostDTO p) { return repo.createPost(boardId, p); }

    public List<Map<String,Object>> activity(Long boardId, int hours) { return repo.getActivity(boardId, hours); }
}
