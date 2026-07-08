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

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.service.MeetupService;
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
    private final MeetupService meetupService;
    
    // 관리자 페이지
    @GetMapping("/admin")
    public String adminQuestionList(Model model) {

        model.addAttribute("allCnt", questionService.getAllCnt());
        model.addAttribute("pendingCnt", questionService.getPendingCnt());
        model.addAttribute("answeredCnt", questionService.getAnsweredCnt());
        model.addAttribute("todayCnt", questionService.getTodayCnt());

        model.addAttribute("list", questionService.getList(0, 10));

        model.addAttribute("page", 1);
        model.addAttribute("startPage", 1);
        model.addAttribute("endPage", 1);
        model.addAttribute("totalPage", 1);

        return "user/qna/adminQuestionList";
    }
    
    // 내가 쓴 문의 목록
    @GetMapping("/myQuestion")
    public String myQuestion(@RequestParam(defaultValue="1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            HttpSession session,Model model) {
    	//MemberDto loginUser = (MemberDto)session.getAttribute("loginUser");
        
        //int memberId = loginUser.getMemberId();
        int memberId = 1; // 임시 나중에 삭제
        int pageSize = 10;
        int start = (page - 1) * pageSize;
        List<QuestionDto> list = questionService.getMyQuestions(
        		memberId, start, pageSize, type, keyword);
        int totalCnt = questionService.getMyQuestionCnt(
        		memberId, type, keyword);
        int totalPage = (int)Math.ceil((double)totalCnt / pageSize);

        int pageBlock = 10;   // 한 번에 보여줄 페이지 번호 개수
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) { endPage = totalPage; }
        
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

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
    
    // 문의 상세 화면 + 답변 조회 + 버튼 권한
    @GetMapping("/{id}")
    public String detail(@PathVariable int id, HttpSession session, Model model) {
        QuestionDto data = questionService.getDetail(id);

        boolean canAnswer = canAnswer(data, session);

        model.addAttribute("data", data);
        model.addAttribute("canAnswer", canAnswer);

        return "user/qna/questionDetail";
    }

    // 문의 수정 화면 이동
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, HttpSession session, Model model, RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(id);

        if(!canEdit(question, session)){
            rttr.addFlashAttribute("msg", "작성자 또는 관리자만 수정할 수 있습니다.");
            return "redirect:/questions/" + id;
        }
        model.addAttribute("data", question);
        return "user/qna/questionEdit";
    }

    @PostMapping("/edit")
    public String edit(QuestionDto dto, HttpSession session, RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());

        if(!canEdit(question, session)){
            rttr.addFlashAttribute("msg", "작성자 또는 관리자만 수정할 수 있습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        questionService.updateQuestion(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 문의 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes rttr) {
	    QuestionDto question =questionService.getDetail(id);
	    
	    if(!canEdit(question, session)){
	        rttr.addFlashAttribute("msg", "작성자 또는 관리자만 삭제할 수 있습니다.");
	        return "redirect:/questions/" + id;
	    }
	    questionService.deleteQuestion(id);
	    return "redirect:/questions/myQuestion";
	}

    // 답변 등록 + 문의 상태 변경 (관리자 전용)
    @PostMapping("/answer")
    public String answerWrite(AnswerDto dto, HttpSession session, RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());
        
        if(!canAnswer(question, session)){
            rttr.addFlashAttribute("msg", "모임장 또는 관리자만 답변할 수 있습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        answerService.register(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }
    
    // 답변 작성
    @GetMapping("/answer/write/{id}")
    public String answerForm(@PathVariable int id, HttpSession session, Model model,  RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(id);
        
        if(!canAnswer(question, session)){
            rttr.addFlashAttribute("msg", "모임장 또는 관리자만 답변할 수 있습니다.");
            return "redirect:/questions/" + id;
        }
        model.addAttribute("data", question);
        return "user/qna/answerWrite";
    }
    
    // 답변 수정 화면
    @GetMapping("/answer/edit/{questionId}")
    public String answerEditForm(@PathVariable int questionId, HttpSession session, Model model) {
        QuestionDto question = questionService.getDetail(questionId);
        
        if(!canAnswer(question, session)){
            return "redirect:/questions/" + questionId;
        }
        model.addAttribute("data", question);
        model.addAttribute("answer", answerService.getAnswer(questionId));
        return "user/qna/answerEdit";
    }

    // 답변 수정 처리
    @PostMapping("/answer/edit")
    public String answerEdit(AnswerDto dto, HttpSession session, RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());
        
        if(!canAnswer(question, session)){
        	rttr.addFlashAttribute("msg", "답변 수정 권한이 없습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        answerService.update(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 답변 삭제
    @GetMapping("/answer/delete/{answerId}/{questionId}")
    public String answerDelete(@PathVariable int answerId,@PathVariable int questionId,
    		HttpSession session, RedirectAttributes rttr) {
        QuestionDto question = questionService.getDetail(questionId);
        
        if(!canAnswer(question, session)){
            rttr.addFlashAttribute("msg", "답변 삭제 권한이 없습니다.");
            return "redirect:/questions/" + questionId;
        }
        answerService.delete(answerId, questionId);
        return "redirect:/questions/" + questionId;
    }

    // 답변 권한 확인 메서드
    private boolean canAnswer(QuestionDto question, HttpSession session){ // <- HttpSession session로 수정
        // ===== 로그인 연동 시 사용할 코드 =====
//      MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
//      if(loginUser == null){
//          return false;
//      }
//      // 관리자 문의
//      if("ADMIN".equals(question.getCategory())){
//          return loginUser.getMemberTypeId() == 3
//              || loginUser.getMemberTypeId() == 4;
//      }
//      // 모임 문의
//      if(loginUser.getMemberTypeId() == 3
//          || loginUser.getMemberTypeId() == 4){
//          return true;
//      }
//      MeetupDto meetup = meetupService.getDetail(question.getParentId());
//      return meetup != null && meetup.getMemberId() == loginUser.getMemberId();
      // ===== 임시 테스트 코드 =====
    	int loginMemberId = 1;
    	// 관리자 문의
        if("ADMIN".equals(question.getCategory())){
            return true;   // 로그인 붙으면 관리자 권한 체크로 변경
        }
        // 모임 문의
        MeetupDto meetup = meetupService.getDetail(question.getParentId());
        if(meetup != null && meetup.getMemberId() == loginMemberId){
            return true;
        }
        return false;
    }
    
    // 문의 수정/삭제 권한 확인
    private boolean canEdit(QuestionDto question, HttpSession session){
        // ===== 로그인 연동 시 사용할 코드 =====
//        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
//        if(loginUser == null){
//            return false;
//        }
//        return question.getMemberId() == loginUser.getMemberId()
//            || loginUser.getMemberTypeId() == 3
//            || loginUser.getMemberTypeId() == 4;
        // ===== 임시 테스트 코드 =====
        int loginMemberId = 1;
        // 작성자
        if(question.getMemberId() == loginMemberId){ 
        	return true; 
        }
        return false;
    }
    
}