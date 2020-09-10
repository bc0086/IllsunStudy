package com.oneline.psi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oneline.psi.BoardVO;
import com.oneline.psi.service.BoardService;
import com.tobesoft.platform.PlatformRequest;
import com.tobesoft.platform.PlatformResponse;
import com.tobesoft.platform.data.ColumnInfo;
import com.tobesoft.platform.data.Dataset;
import com.tobesoft.platform.data.DatasetList;
import com.tobesoft.platform.data.Variable;
import com.tobesoft.platform.data.VariableList;

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
	
	@RequestMapping("searchList")
	public String searchlist(@RequestParam Map<String, Object> map, Model model) {
		
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
		
		return "board/searchList";
	}

	//글쓰기 페이지로 이동
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
	
	// 업로드 1단계 : uuid생성
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("생성된UUID-1:"+ uuid);

		uuid = uuid.replaceAll("-", "");
		System.out.println("생성된UUID-2:"+ uuid);
		return uuid;
	}
	
	// 글쓰기
	@RequestMapping(value="writeAction", method=RequestMethod.POST)
	public String writeAction(@RequestParam Map<String, Object> map, Model model, MultipartHttpServletRequest req) {
		System.out.println("글쓰기 등록 버튼 누름");
		int insert = boardService.writeAction(map);
		
		// 업로드 2단계 : 업로드처리
		// 서버의 물리적경로 가져오기
		 String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		// 폼값과 파일명을 저장후 View로 전달하기 위한 맵 생성
		Map returnObj = new HashMap();
		try
		{
			// 업로드폼의 file 속성의필드를 가져온다.(여기서는 2개임)
			Iterator itr = req.getFileNames();
			
			MultipartFile mfile = null;
			String fileName = "";
			List resultList = new ArrayList();
			// 파일외의 폼값 받음(여기서는 제목만 있음)
			String title = req.getParameter("title");
			System.out.println("title="+title);
			
			/*
			  	물리적경로를 기반으로 File 객체를 생성한 후 지정된
			  	디렉토리가 존재하는지 확인함. 만약 없다면 생성함.
			 */
			File directory = new File(path);
			if(!directory.isDirectory()) {
				directory.mkdirs();
			}
			
			//업로드폼의 file속성의 필드갯수만큼 반복
			while(itr.hasNext())
			{
				// 전송된 파일의 이름을 읽어옴
				fileName = (String)itr.next();
				mfile = req.getFile(fileName);
				System.out.println("mfile="+ mfile);
				
				// 한글깨짐방지 처리 후 전송된 파일명을 가져옴.
				String originalName = new String(mfile.getOriginalFilename().getBytes(),"UTF-8");
				
				// 서버로 전송된 파일이 없다면 while문의 처음으로 돌아간다.
				if("".equals(originalName)) {
					continue;
				}
				
				// 파일명에서 확장자 부분을 가져옴
				String ext = originalName.substring(originalName.lastIndexOf('.'));
				
				// UUID를 통해 생성된 문자열과 확장자를 합침
				String saveFileName = getUuid()+ext;
				
				// 물리적경로에 새롭게 생성된 파일명으로 파일저장
				File serverFullName = new File(path + File.separator + saveFileName);
				
				mfile.transferTo(serverFullName);
				
				// 서버에 파일업로드 완료후...
				Map file = new HashMap();
				file.put("originalName", originalName); //원본파일명
				file.put("saveFileName", saveFileName); //저장된 파일명
				file.put("serverFullName", serverFullName); // 서버의 전체경로
				file.put("title", title); // 제목
				// 위 4가지 정보를 저장한 Map을 ArrayList에 저장한다.
				resultList.add(file);
			}
			returnObj.put("files", resultList);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("returnObj", returnObj);	
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
			
	// 파일 업로드 리스트
	@RequestMapping("uploadList")
	public String uploadList(HttpServletRequest req, Model model) {
		
		// 서버의 물리적 경로 가져오기 
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		// 경로를 기반으로 File객체 생성
		File file = new File(path);
		
		// 파일의 목록을 배열형태로 얻어옴
		File[] fileArray = file.listFiles();
		
		// View로 파일목록을 전달하기 위해 Map생성함.
		Map<String, Integer> fileMap = new HashMap<String, Integer>();
		for(File f : fileArray) {
			
			/*
			 	Map의 key값은 파일명, value값은 파일의 용량을 저장함.
			 */
			fileMap.put(f.getName(), (int)Math.ceil(f.length()/1024.0));
		}
		
		model.addAttribute("fileMap", fileMap);
		return "board/uploadList";
	}
		
	// 파일 다운로드
//	@RequestMapping("download")
//	public ModelAndView download(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		String fileName = req.getParameter("fileName");
//		String oriFileName = req.getParameter("oriFileName");
//		
//		String saveDirectory = req.getSession().getServletContext().getRealPath("/resources/upload");
//		
//		File downloadFile = new File(saveDirectory+"/"+fileName);
//		
//		if(!downloadFile.canRead()) {
//			throw new Exception("파일을 찾을 수 없습니다.");
//		}
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("fileDownloadView");
//		mv.addObject("downloadFile", downloadFile);
//		mv.addObject("oriFileName", oriFileName);
//		return mv;			
//	}
	
	
	// 업로드 다른방법
		private final static String filePath = "C:/Users/giant/Desktop/chanupload/";
			// 업로드 할 경로설정
		
		@RequestMapping("fileUpload1")
		public String fileUpload1(MultipartHttpServletRequest mRquest) throws IllegalStateException, IOException {
			
			Iterator<String> itr =  mRquest.getFileNames();
			
			while (itr.hasNext()) {
				MultipartFile mFile = mRquest.getFile(itr.next());
				
				String realName = mFile.getOriginalFilename();
				String saveFileName = System.currentTimeMillis() + "_" + realName;
				
				mFile.transferTo(new File(filePath + saveFileName));
			}
			return "redirect:list";
		}
	
	
	
	// 다운로드 다른방법
	@RequestMapping("fileDown")
	public void fileDown(@RequestParam String saveName,
							@RequestParam String realName
							,HttpServletResponse response
							,HttpServletRequest request) {
		
	 
	    InputStream in = null;
	    OutputStream os = null;
	    File file = null;
	    boolean skip = false;
	    String client = "";
	 
	    
	   
	    try{
	 
	        // 파일을 읽어 스트림에 담기
	        try{
	            file = new File(filePath, saveName);
	            System.out.println("before stram file=" + file);
	            in = new FileInputStream(file);
	            System.out.println("stream in =" + in);
	        }catch(FileNotFoundException fe){
	            skip = true;
	        }
	 
	        client = request.getHeader("User-Agent");
	 
	        // 파일 다운로드 헤더 지정
	        response.reset() ;
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Description", "JSP Generated Data");
	 
	 
	        if(!skip){
	 
	             
	            // IE
	            if(client.indexOf("MSIE") != -1){
	                response.setHeader ("Content-Disposition", "attachment; filename="+new String(realName.getBytes("KSC5601"),"ISO8859_1"));
	 
	            }else{
	                // 한글 파일명 처리
	            	realName = new String(realName.getBytes("utf-8"),"iso-8859-1");
	                response.setHeader("Content-Disposition", "attachment; filename=\"" + realName + "\"");
	                response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
	            } 
	             
	            os = response.getOutputStream();
	            byte b[] = new byte[(int)file.length()];
	            int leng = 0;
	             
	            while( (leng = in.read(b)) > 0 ){
	                os.write(b,0,leng);
	            }
	 
	        }else{
	            response.setContentType("text/html;charset=UTF-8");
	        }
	         
			in.close();
	        os.close();
	        System.out.println("성공");
	 
	    }catch(Exception e){
	      e.printStackTrace();
	      System.out.println("실패");
	    }

	}

	@RequestMapping("excelDown")
	public String excelDown(Model model, @RequestParam Map<String, Object> map) {
		
		 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		 System.out.println(list); 
		 
		 list = boardService.excelDown(map);
		 System.out.println(list); 
		 model.addAttribute("list", list);
		 
		 return "board/excelJsp";
		/*
		 * List<Map<String, Object>> list = sqlSession.selectList("mapper.excelDown",
		 * map);
		 * 
		 * model.addAttribute("list", list); return "board/excelJsp";
		 */

	}
	
	// MiPlatform 연동하기
	@RequestMapping("miConnector")
	public void miConnector(@RequestParam Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println("마이플랫폼");
		
		// 검색하기
		PlatformRequest pReq = new PlatformRequest(request, "UTF-8");
		pReq.receiveData();
		
		DatasetList javaDsl = pReq.getDatasetList();
		
		Dataset dSearch = javaDsl.getDataset("javaSearch");
		System.out.println("dSearch :" + dSearch);
		
		String searchType = null;
		String searchTxt = null;
		String startDate = null;
		String endDate = null;
		
		
		for(int i=0; i<dSearch.getRowCount(); i++) { 
			searchType = dSearch.getColumnAsString(i, "searchType"); 
			searchTxt = dSearch.getColumnAsString(i, "keyword"); 
			startDate = dSearch.getColumnAsString(i, "startDate"); 
			endDate = dSearch.getColumnAsString(i, "endDate"); 
		}
		
		if(searchType.equals("null")){
			searchType="";
		}
		if(searchTxt.equals("null")){
			searchTxt="";
		}
		if(startDate.equals("null")){
			startDate="";
		}
		if(endDate.equals("null")){
			endDate="";
		}		
		
		System.out.println("searchType : " + searchType);
		System.out.println("searchTxt : " + searchTxt);
		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		
		map.put("searchType", searchType);
		map.put("searchTxt", searchTxt);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		System.out.println("map : " + map);
		/*
		 	두번째 방법
		VariableList javaVl = pReq.getVariableList();
		
		String searchTypeVl = javaVl.getValueAsString("searchKey");
		String searchTxtVl = javaVl.getValueAsString("txt");
		System.out.println(searchTypeVl);
		System.out.println(searchTxtVl);
		*/
		
		// 전체리스트
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list = boardService.mipList(map);
		
		Dataset ds = new Dataset("javaDs");
		// ds.setid("javaList")랑 같은 의미
		ds.addColumn("seq", ColumnInfo.COLUMN_TYPE_INT, 100);
		// ds.addColumn("seq", ColumnInfo.COLUMN_TYPE_INT, 100); 랑 같은 결과
		ds.addColumn("memId", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("memName", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("boardSubject", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("regDate", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("uptDate", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("viewCnt", ColumnInfo.COLUMN_TYPE_STRING, 100);
		
		for(int i=0; i<list.size(); i++) {
			int row = ds.appendRow();
			int seq = Integer.parseInt(list.get(i).get("seq").toString());
				// list.get은 list의 값 즉 map을 의미. 여기서 한번더 get을 하면 map의 값.
			ds.setColumn(row, "seq", seq);
				// get으로 꺼내오면 무조건 toString으로 형변환할 것.
			ds.setColumn(row, "memId", list.get(i).get("memId").toString());
			ds.setColumn(row, "memName", list.get(i).get("memName").toString());
			ds.setColumn(row, "boardSubject", list.get(i).get("boardSubject").toString());
			ds.setColumn(row, "regDate", list.get(i).get("regDate").toString());
			// ds.setColumn(row, "uptDate", list.get(i).get("uptDate").toString());
			String uptDate = list.get(i).get("uptDate") == null ? "" : list.get(i).get("uptDate").toString();
			ds.setColumn(row, "uptDate", uptDate);
			ds.setColumn(row, "viewCnt", list.get(i).get("viewCnt").toString());
		}
		
		DatasetList dsl = new DatasetList();
		dsl.add(ds);
		System.out.println(dsl);
		
		VariableList vl = new VariableList();
		System.out.println(vl);
		
		PlatformResponse pRes = new PlatformResponse(response, PlatformRequest.JSP_XML, "UTF-8");
		pRes.sendData(vl, dsl);
		
		System.out.println("연결은 됬다.");	
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 마이플랫폼 testSpring과의 연결
	@RequestMapping("mipGetList")
	public void mipGetList(@RequestParam Map<String, Object> map, HttpServletResponse response) throws IOException {
		// RequestParam : 단일 파라미터를 전달받을때 사용하는 어노테이션 
			// map은 model과 달리 내장객체가 아니기 때문에 즉, 외부에서 들어오는 데이터를 저장하기 때문에 RequestParam필요
		// HttpServletRequest : 웹브라우저가 요청할때 담겨있던 정보를 담기위해 생성된 내장객체.
		// 즉 얘한테 요청할 때의 정보가 담겨있기 때문에 물어보면 다 알려줌.
		// HttpServletResponse : 요청을 한 웹브라우저에게 응답을 보낼때 사용하기 위해 생성된 내장객체
		List<Map<String, Object>> boardList = new ArrayList<Map<String,Object>>();
		boardList = boardService.mipGetList(map);
		
		// 1. 데이터셋을 만들자
		Dataset ds = new Dataset("javaDs");
		
		// 2. 컬럼을 추가하자 (컬럼명, 컬럼타입, 사이즈)
		ds.addColumn("seq", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("mem_id", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("mem_name", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("board_subject", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("board_content", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("reg_date", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("upt_date", ColumnInfo.COLUMN_TYPE_STRING, 100);
		ds.addColumn("view_cnt", ColumnInfo.COLUMN_TYPE_STRING, 100);
		
		// 3. 컬럼에 데이터를 넣자 (어느행, 어느컬럼명, 어떤데이터)
			// 행은 boardList의 사이즈만큼 즉 데이터의 줄만큼 반복문 돌려준다.
			// 컬럼명은 마이플랫폼의 컬럼명
			// 데이터는 자바단에서 가져온 데이터
		for(int i=0; i<boardList.size(); i++) {
			int row = ds.appendRow();
				// ds.addRow();도 같이 열을 추가하는 메소드이나 자바단에서는 안먹힘.
			int seq = Integer.parseInt(boardList.get(i).get("seq").toString());
				// map에서 get으로 꺼내오면 자료형이 object이므로 무조건 toString으로 형변환 할 것.
			String mem_id = boardList.get(i).get("memId").toString();
			String mem_name = boardList.get(i).get("memName").toString();
			String board_subject = boardList.get(i).get("boardSubject").toString();
			String board_content = boardList.get(i).get("boardContent").toString();
			String reg_date = boardList.get(i).get("regDate").toString();
			String upt_date = boardList.get(i).get("uptDate") == null ? "" : boardList.get(i).get("boardContent").toString();
			String view_cnt = boardList.get(i).get("viewCnt").toString();
			
			ds.setColumn(row, "seq", seq);
			ds.setColumn(row, "mem_id", mem_id);
			ds.setColumn(row, "mem_name", mem_name);
			ds.setColumn(row, "board_subject", board_subject);
			ds.setColumn(row, "board_content", board_content);
			ds.setColumn(row, "reg_date", reg_date);
			ds.setColumn(row, "upt_date", upt_date);
			ds.setColumn(row, "view_cnt", view_cnt);
		}
		
		// 4. 데이터셋 리스트 만들기
			// 데이터셋리스트, 베리에이블리스트, 플랫폼리스폰스, 센드데이터
		DatasetList dsl = new DatasetList();
		dsl.add(ds);
		System.out.println("dsl: " + dsl);
		
		VariableList vl = new VariableList();
		System.out.println("vl: " + vl);
		
		PlatformResponse pRes = new PlatformResponse(response, PlatformRequest.JSP_XML, "utf-8");
		pRes.sendData(vl, dsl);
		
	}
	
	
	
	
		
}