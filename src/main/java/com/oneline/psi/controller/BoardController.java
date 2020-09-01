package com.oneline.psi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
		
		
		
		if(map.isEmpty()) {
			map.put("pageNo", 1);
			map.put("listSize", 10);
		}
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list = boardService.list(map);
		
		// 전체 게시물 갯수
		int total = boardService.totalCount(map);
		// 현재 페이지
		int curPage = Integer.parseInt(map.get("pageNo").toString());
		// 게시물 크기
		int listSize = Integer.parseInt(map.get("listSize").toString());
		// 위에서 만든  변수들을 하나의 맵으로 묶음.
		Map<String, Object> pageMap = pageMap(curPage, total, listSize);
		
		model.addAttribute("list", list); // 데이터를 저장
		model.addAttribute("pageMap", pageMap); // 데이터를 저장
		
		
		String keyword = (String)map.get("keyword");
		model.addAttribute("keyword", keyword );
		String search_option = (String)map.get("search_option");
		model.addAttribute("search_option", search_option);
		String start_date = (String)map.get("start_date");
		model.addAttribute("start_date", start_date);
		String end_date = (String)map.get("end_date");
		model.addAttribute("end_date", end_date);
		System.out.println(end_date);
		
		
		
		
		
		
		
		System.out.println(map);
			
		return "board/list";
	}
	
	public Map<String, Object> pageMap(int curPage, int count, int listSize) {
		int BLOCK_SCALE = 7;
		int totPage = (int) Math.ceil(count*1.0 / listSize);
		int totBlock = (int) Math.ceil(totPage / BLOCK_SCALE);
		int curBlock = (int) Math.ceil((curPage-1) / BLOCK_SCALE)+1;
		int blockBegin = (curBlock-1)*BLOCK_SCALE+1;
		int blockEnd = blockBegin+BLOCK_SCALE-1;
		if(blockEnd > totPage) blockEnd = totPage;
		int prevPage = (curPage == 1)? 1:(curBlock-1)*BLOCK_SCALE;
        int nextPage = curBlock > totBlock ? (curBlock*BLOCK_SCALE) : (curBlock*BLOCK_SCALE)+1;
        if(nextPage >= totPage) nextPage = totPage;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totBlock", totBlock);
        map.put("curBlock", curBlock);
        map.put("blockBegin", blockBegin);
        map.put("blockEnd", blockEnd);
        map.put("prevPage", prevPage);
        map.put("nextPage", nextPage);
        map.put("curPage", curPage);
        map.put("totPage", totPage);
        map.put("BLOCK_SCALE", BLOCK_SCALE);
		
		return map;
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

