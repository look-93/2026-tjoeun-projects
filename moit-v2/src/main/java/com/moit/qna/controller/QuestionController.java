package com.moit.qna.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.qna.dto.AnswerDto;
import com.moit.qna.dto.QuestionDto;
import com.moit.qna.service.AnswerService;
import com.moit.qna.service.QuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
문의 화면 요청 처리 Controller
*/
@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    
    // 내가 쓴 문의 목록
    @GetMapping("/myQuestion")
    public String myQuestion(@RequestParam(defaultValue="1") int page,
            HttpSession session,Model model) {
    	//MemberDto loginUser = (MemberDto)session.getAttribute("loginUser");
        
        //int memberId = loginUser.getMemberId();
        int memberId = 1; // 임시 나중에 삭제
        int pageSize = 10;
        int start = (page - 1) * pageSize;
        List<QuestionDto> list =
                questionService.getMyQuestions(
                        memberId,
                        start,
                        pageSize);
        int totalCnt =
                questionService.getMyQuestionCnt(memberId);
        int totalPage =
                (int)Math.ceil((double)totalCnt / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);

        return "user/qna/questionList";
    }
    // 관리자가 보는 전체 문의 내역
    @GetMapping
    public String list( @RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        int start = (page - 1) * pageSize;
        List<QuestionDto> list = questionService.getList(start, pageSize);

        int totalCnt = questionService.getAllCnt();
        int totalPage = (int)Math.ceil((double)totalCnt / pageSize);

        model.addAttribute("list", list);

        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        
        // 전체 문의 수
        model.addAttribute("allCnt", questionService.getAllCnt());
        // 답변 대기 문의 수
        model.addAttribute("pendingCnt", questionService.getPendingCnt());
        // 답변 완료 문의 수
        model.addAttribute("answeredCnt", questionService.getAnsweredCnt());
        // 오늘 등록된 문의 수
        model.addAttribute("todayCnt", questionService.getTodayCnt());
        return "user/qna/qnaList";
    }
    
    // 모임글 문의 등록
    @GetMapping("/write")
    public String write(QuestionDto dto,
                        @RequestParam(defaultValue="MEETUP") String category, Model model) {
        model.addAttribute("category", category);
        return "user/qna/questionWrite";
    }
   
    @PostMapping("/write")
    public String writePost(QuestionDto dto, RedirectAttributes rttr) {
        questionService.register(dto);
        rttr.addFlashAttribute("msg", "문의가 등록되었습니다.");
        return "redirect:/questions/" + dto.getQuestionId();
    }
    
    // 문의 상세 화면 + 답변 조회
    @GetMapping("/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("data", questionService.getDetail(id));
        return "user/qna/questionDetail";
    }

    // 문의 수정 화면 이동
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("data", questionService.getDetail(id));
        return "user/qna/questionEdit";
    }

    // 문의 수정 처리
    @PostMapping("/edit")
    public String edit(QuestionDto dto) {
        questionService.updateQuestion(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 문의 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        questionService.deleteQuestion(id);
        return "redirect:/questions/myQuestion";
    }

    // 답변 등록 (관리자 전용)
    @PostMapping("/answer")
    public String answerWrite(AnswerDto dto) {
    	// 답변 등록 + 문의 상태 변경
        answerService.register(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }
    
    //
    @GetMapping("/answer/write/{id}")
    public String answerForm(@PathVariable int id, Model model) {
        model.addAttribute("data", questionService.getDetail(id));
        return "user/qna/answerWrite";
    }
    
    // 답변 수정 화면
    @GetMapping("/answer/edit/{questionId}")
    public String answerEditForm(@PathVariable int questionId, Model model) {
    	 model.addAttribute("data", questionService.getDetail(questionId));
    	 model.addAttribute("answer", answerService.getAnswer(questionId));
        return "user/qna/answerEdit";
    }

    // 답변 수정 처리
    @PostMapping("/answer/edit")
    public String answerEdit(AnswerDto dto) {
        answerService.update(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 답변 삭제
    @GetMapping("/answer/delete/{answerId}/{questionId}")
    public String answerDelete(@PathVariable int answerId, @PathVariable int questionId) {
    	answerService.delete(answerId, questionId);
        return "redirect:/questions/" + questionId;
    }

}