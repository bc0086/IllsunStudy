package com.oneline.psi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneline.psi.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	SqlSession sqlSession;
	
	@Resource(name = "service")
	private BoardService boardService;
	
	
	@RequestMapping("list") // 세부적인 url mapping
	public String list(@RequestParam Map<String, Object> map, Model model) {
	
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list = boardService.list(map);
		model.addAttribute("list", list); // 데이터를 저장
		
		
		model.addAttribute("map", map);
		
		System.out.println(map);
			
		return "board/list";
	}
	
	//글쓰기 버튼 눌렀을 때 
	@RequestMapping("write")
	public String write() {
		return "board/write";
	}
	
	
	//게시판에서 제목 클릭했을 때 상세보기로 이동
	@RequestMapping("detail")
	public String detail(@RequestParam int seq, Model model) {
		System.out.println("상세보기");
		Map<String, Object> detailMap = boardService.detail(seq);
		model.addAttribute("detail", detailMap);
		
		int viewCnt = boardService.viewCnt(seq);
		
		return "board/write";
	}
	
	//글쓰기 
	@RequestMapping("writeAction.do")
	public String writeAction(@RequestParam Map<String, Object> map) {
		System.out.println("글쓰기 등록 버튼 누름");
		int insert = boardService.writeAction(map);
		
		System.out.println(insert);
		System.out.println(map);
		//redirect url을 찾아간다. 
		return "redirect:list";
	}
	
	//글 수정하기
	@RequestMapping("editAction.do")
	public String editAction(@RequestParam Map<String, Object> map) {
		System.out.println("글쓰기 수정 버튼 누름");
		int update = boardService.editAction(map);
		
		System.out.println(update);
		System.out.println(map);
		//redirect url을 찾아간다. 
		return "redirect:list";
	}
	
	// 글 삭제하기1
	@RequestMapping("delete.do") 
	@ResponseBody
	public String delete(@RequestParam Map<String, Object> map) throws Exception{
		System.out.println("delete 들어옴");
		int result=1;
		int delete;
			int cnt = Integer.parseInt((String) map.get("cnt"));
			String arr = (String)map.get("arr");
		try {
			String [] strArray = arr.split(",");
			for(int i=0; i<cnt; i++) {
				int temp = Integer.parseInt((String)strArray[i]);
				map.put("seq", temp);
				delete = boardService.delete(map);
			}
		}
		catch (Exception e) {
			result=0;
		}
		return Integer.toString(result);
	}
	
	// 글 삭제하기 2
	@RequestMapping("delete2.do")
	public String delete2(Integer[] checkbox) {
		List<Integer> list = Arrays.asList(checkbox);
		System.out.println(list);
		
		int delete = boardService.delete2(list);
		return "redirect:list";
	}
}

