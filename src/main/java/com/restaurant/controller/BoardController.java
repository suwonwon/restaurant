package com.restaurant.controller;


import com.restaurant.constant.Role;
import com.restaurant.entity.Board;
import com.restaurant.entity.Comment;
import com.restaurant.entity.Member;
import com.restaurant.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value ="/board")
public class BoardController {

    @Autowired
    private final BoardService boardService;

    //전체조회
    @GetMapping("")
    public String BoardList(Pageable pageable, Model model) {
        Page<Board> BoardList = boardService.BoardList(pageable);
        model.addAttribute("BoardList", BoardList.getContent());
        model.addAttribute("page", BoardList);

        return "board/board";
    }

    //상세페이지,조회
    @GetMapping("/{p_id}")
    public String BoardDetail(@PathVariable("p_id") int p_id, Model model) {
        int State=0;
        Member member = boardService.findMember();
        Board board = boardService.BoardDetail(p_id);
        List<Comment> comments = boardService.CommentList(board);
        if ((board.getM_id()==member)||(member.getRole()== Role.ADMIN)){
            State=1;
        }
        model.addAttribute("State",State);
        model.addAttribute("board", board);
        model.addAttribute("comments",comments);

        return "board/board-detail";
    }

    //작성페이지
    @GetMapping("/write")
    public String BoardWrite(){
        return "board/board-write";
    }

    //게시물작성
    @PostMapping("/write")
    public String BoardWrite(@RequestParam("title") String title, @RequestParam("contents") String contents){
        System.out.println("title:"+title +"contents:"+contents);
        Board board = boardService.BoardWrite(title, contents,1);

        return "redirect:/board/" + board.getP_id();
    }

    //게시물수정
    @GetMapping("/update/{p_id}")
    public String  BoardUpdate(@PathVariable("p_id") int p_id, Model model){
        Board board1 = boardService.BoardDetail(p_id);
        model.addAttribute("board",board1);
        return "board/board-update";
    }

    //게시물수정
    @PostMapping("/update/{p_id}")
    public String  BoardUpdate1(@PathVariable("p_id") int p_id, @ModelAttribute Board board){

        boardService.BoardUpdate(board);
        return "redirect:/board/" + board.getP_id();
    }

    //게시물삭제
    @GetMapping("/delete/{p_id}")
    public String BoardDelete(@PathVariable("p_id") int p_id){
        boardService.BoardDelete(p_id);
        return "redirect:/board";
    }

    //댓글조회
    @GetMapping("/{p_id}/comment")
    public String CommentList(@PathVariable int p_id,Model model){
        Board board = boardService.BoardDetail(p_id);
        List<Comment> comments = boardService.CommentList(board);
        model.addAttribute("comments",comments);
        return "comment-list";
    }
    
    //댓글작성
    @PostMapping("/comment/write")
    public String  CommentDetail(@RequestParam("comment") String comment,
                                 @RequestParam("p_id") int p_id){
        boardService.CommentWrite(comment,p_id);
        return "redirect:/board/" + p_id;
    }
   
    //댓글삭제
    @GetMapping("/comment/delete/{c_id}")
    public ResponseEntity<String> commentDelete(@PathVariable int c_id) {
        try {
            boardService.CommentDelete(c_id);
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/notice/write")
    public String NoticeWrite(){
        return "notice/notice-write";
    }


    //게시판검색
    @GetMapping("/search")
    public String search(@RequestParam(name = "search", required = false) String search,Pageable pageable,Model model){
        if (search==null||search.isEmpty()){
            BoardList(pageable,model);
            return "board/board";
        }else {
            Page<Board> boards = boardService.search(search, pageable);
            model.addAttribute("BoardList",boards.getContent());
            model.addAttribute("page", boards);
            return "board/board";
        }

    }

}

