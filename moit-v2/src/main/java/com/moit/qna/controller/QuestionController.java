package com.moit.qna.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.service.MeetupServiceImpl;
import com.moit.member.dto.UserDto;
import com.moit.qna.dto.AnswerDto;
import com.moit.qna.dto.QuestionDto;
import com.moit.qna.service.AnswerService;
import com.moit.qna.service.QuestionAiAnalysisService;
import com.moit.qna.service.QuestionService;
import com.moit.security.CustomUserDetails;

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
    //private final MeetupService meetupService;
    private final MeetupServiceImpl meetupService;
    private final QuestionAiAnalysisService questionAiAnalysisService;
    
    //관리자용 선택 삭제
    @PostMapping("/deleteSelected")
    @ResponseBody
    public void deleteSelected(@RequestBody List<Integer> ids){
        questionService.deleteSelected(ids);
    }
    
    // AI 필터 정상 처리
    @PostMapping("/ai/normal")
    @ResponseBody
    public void changeAiNormal(@RequestBody List<Integer> ids){
        questionAiAnalysisService.changeToNormal(ids);
    }
    
    // 내 문의 목록
    @GetMapping("/myQuestion")
    public String myQuestion(@RequestParam(defaultValue="1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            HttpSession session,Model model, Authentication authentication) {
    	//MemberDto loginUser = (MemberDto)session.getAttribute("loginUser");
               
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
    	
        //int memberId = 1; // 임시 나중에 삭제
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
        
        model.addAttribute("dto" , user); 
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalCnt", totalCnt);
        
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "user/qna/questionList";
    }
    // 관리자가 보는 전체 문의 목록
    @GetMapping("/admin")
    public String admin(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(required=false) String type,
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) String status,
            @RequestParam(required=false) String startDate,
            @RequestParam(required=false) String endDate,
            Model model,
            Authentication authentication) {
    	
        UserDto user = null;
        if (authentication != null &&
            authentication.getPrincipal() instanceof CustomUserDetails users) {
            user = users.getUser();
        }
        model.addAttribute("dto", user);
    	
        int pageSize = 10;
        int start = (page - 1) * pageSize;
        List<QuestionDto> list = questionService.getList(start, pageSize, type, keyword, status, startDate, endDate );

        int totalCnt = questionService.getSearchCnt(type, keyword, status, startDate, endDate);
        int totalPage = (int)Math.ceil((double)totalCnt / pageSize);

        int pageBlock = 10;
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;

        if(endPage > totalPage){
            endPage = totalPage;
        }
        
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCnt", totalCnt);
        
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("searchCnt", totalCnt);
        
        // 전체 문의 수
        model.addAttribute("allCnt", questionService.getAllCnt());
        // 답변 대기 문의 수
        model.addAttribute("pendingCnt", questionService.getPendingCnt());
        // 답변 완료 문의 수
        model.addAttribute("answeredCnt", questionService.getAnsweredCnt());
        // 오늘 등록된 문의 수
        model.addAttribute("todayCnt", questionService.getTodayCnt());
        return "admin/qna/adminQuestionList";
    }
    
    // 모임글 문의 등록
    @GetMapping("/write")
    public String write(
    		@RequestParam(required = false) Integer meetupId,
            @RequestParam(defaultValue = "MEETUP") String category,
            Model model) {
        model.addAttribute("category", category);
        if(meetupId != null){
            model.addAttribute("meetupId", meetupId);
        }
        return "user/qna/questionWrite";
    }
   
    @PostMapping("/write")
    public String writePost(QuestionDto dto, RedirectAttributes rttr, Authentication authentication) {
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		dto.setMemberId(memberId);
		// 관리자 문의일 경우 parentId = 0
		if(dto.getParentId() == null){
		    dto.setParentId(0);
		}
        questionService.register(dto);
        rttr.addFlashAttribute("msg", "문의가 등록되었습니다.");
        return "redirect:/questions/" + dto.getQuestionId();
    }
    
    // 문의 상세 화면 + 답변 조회 + 버튼 권한
    @GetMapping("/{id}")
    public String detail(@PathVariable int id, HttpSession session, Model model,  RedirectAttributes rttr, Authentication authentication) {
        QuestionDto data = questionService.getDetail(id);

        boolean canAnswer = canAnswer(data, session, authentication);

        model.addAttribute("data", data);
        model.addAttribute("canAnswer", canAnswer);

        return "user/qna/questionDetail";
    }

    // 문의 수정 화면 이동
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, HttpSession session, Model model, RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(id);
        //System.out.println(question);
        if(!canEdit(question, session, authentication)){
            rttr.addFlashAttribute("msg", "작성자 또는 관리자만 수정할 수 있습니다.");
            return "redirect:/questions/" + id;
        }
        model.addAttribute("data", question);
        return "user/qna/questionEdit";
    }

    @PostMapping("/edit")
    public String edit(QuestionDto dto, HttpSession session, RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());

        if(!canEdit(question, session,authentication)){
            rttr.addFlashAttribute("msg", "작성자 또는 관리자만 수정할 수 있습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        questionService.updateQuestion(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 문의 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes rttr, Authentication authentication) {
	    QuestionDto question =questionService.getDetail(id);
	    
	    if(!canEdit(question, session, authentication)){
	        rttr.addFlashAttribute("msg", "작성자 또는 관리자만 삭제할 수 있습니다.");
	        return "redirect:/questions/" + id;
	    }
	    questionService.deleteQuestion(id);
	    return "redirect:/questions/myQuestion";
	}

    // 답변 등록 + 문의 상태 변경 (관리자 전용)
    @PostMapping("/answer")
    public String answerWrite(AnswerDto dto, HttpSession session, RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());
        
        if(!canAnswer(question, session, authentication)){
            rttr.addFlashAttribute("msg", "모임장 또는 관리자만 답변할 수 있습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        answerService.register(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }
    
    // 답변 작성
    @GetMapping("/answer/write/{id}")
    public String answerForm(@PathVariable int id, HttpSession session, Model model,  RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(id);
        if(!canAnswer(question, session, authentication)){
            rttr.addFlashAttribute("msg", "모임장 또는 관리자만 답변할 수 있습니다.");
            return "redirect:/questions/" + id;
        }
        model.addAttribute("data", question);
        return "user/qna/answerWrite";
    }
    
    // 답변 수정 화면
    @GetMapping("/answer/edit/{questionId}")
    public String answerEditForm(@PathVariable int questionId, HttpSession session, Model model, Authentication authentication) {
        QuestionDto question = questionService.getDetail(questionId);
        
        if(!canAnswer(question, session, authentication)){
            return "redirect:/questions/" + questionId;
        }
        model.addAttribute("data", question);
        model.addAttribute("answer", answerService.getAnswer(questionId));
        return "user/qna/answerEdit";
    }

    // 답변 수정 처리
    @PostMapping("/answer/edit")
    public String answerEdit(AnswerDto dto, HttpSession session, RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(dto.getQuestionId());
        
        if(!canAnswer(question, session, authentication)){
        	rttr.addFlashAttribute("msg", "답변 수정 권한이 없습니다.");
            return "redirect:/questions/" + dto.getQuestionId();
        }
        answerService.update(dto);
        return "redirect:/questions/" + dto.getQuestionId();
    }

    // 답변 삭제
    @GetMapping("/answer/delete/{answerId}/{questionId}")
    public String answerDelete(@PathVariable int answerId,@PathVariable int questionId,
    		HttpSession session, RedirectAttributes rttr, Authentication authentication) {
        QuestionDto question = questionService.getDetail(questionId);
        
        if(!canAnswer(question, session, authentication)){
            rttr.addFlashAttribute("msg", "답변 삭제 권한이 없습니다.");
            return "redirect:/questions/" + questionId;
        }
        answerService.delete(answerId, questionId);
        return "redirect:/questions/" + questionId;
    }

    // 답변 권한 확인 메서드
    private boolean canAnswer(QuestionDto question, HttpSession session, Authentication authentication){ // <- HttpSession session로 수정
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		if(user == null){
		    return false;
		}
    	// 관리자 문의
		if ("ADMIN".equals(question.getCategory())) {
		    return user.getMemberTypeId() == 3 ||
		           user.getMemberTypeId() == 4;
		}
        // 모임 문의
		if(user.getMemberTypeId() == 3 || user.getMemberTypeId() == 4){
		    return true;
			}
			MeetupDto meetup = meetupService.getDetail(question.getParentId());
			return meetup != null && meetup.getMemberId() == memberId;
    }
    
    // 문의 수정/삭제 권한 확인
    private boolean canEdit(QuestionDto question, HttpSession session, Authentication authentication){
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		if(user == null){
		    return false;
		}
		if(question.getMemberId() == memberId){
		    return true;
		}
		if(user.getMemberTypeId() == 3 || user.getMemberTypeId() == 4){
		    return true;
		}
		return false;
    }
}