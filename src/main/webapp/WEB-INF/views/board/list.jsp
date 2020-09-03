<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>

<!-- datepicker -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" />  
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>  
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>  
<!-- /datepicker -->
<script>
$(function(){
	$("#deleteBtn1").click(function(){
		var cnt = $("input[name='checkbox']:checked").length;
		var arr = new Array();
		
		$("input[name='checkbox']:checked").each(function(){
			arr.push($(this).attr('value'));
		});
		alert(cnt+arr);
		if(cnt == 0){
			alert("선택된 글이 없습니다.");
		}	
		else{
			console.log("ddddd");
			$.ajax  ({
				type : "POST",
				url : "delete.do",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				data : "arr=" + arr + "&cnt=" + cnt,
				dataType : "json",
				success : function(jdata){
					if(jdata != 1){
						alert("삭제 오류");
					}
					else{
						alert("삭제 성공");
						location.reload();
					}
				},
				error: function(){
					alert("서버통신 오류");
				}
			});
		}
	})
	
	$("#deleteBtn2").click(function(){
		var cnt = $("input[name='checkbox']:checked").length;
		if(cnt==0){
			alert("삭제할 글을 선택해주세요.");
		}
		else{
			$("#listFrm").attr("action", "delete2.do").attr("method", "post").submit();
		}
	})
	
	$("#searchBtn").click(function(){
		/*  var choice = $('#search_option').val();
		if(choice == 'non_action'){
			alert("검색유형을 선택해주세요.");
			return false;
		}
		else {  */
			/* $("#searchFrm").attr({"action":"list", "method" : "get"}).submit(); */
			$.ajax({
				url : "searchList",
				data : $("#searchFrm").serialize(), 
				//serialize : 키값이 name이 된다.
				type : "post",
				success : function(data){
					alert("ajax성공");
					$("#listFrm").html(data);
				},
				error : function(){
					alert("ajax실패");
				}
			}) 
			
		 /* }  */
	})
	
	$( "#start_date, #end_date" ).datepicker({
		dateFormat: 'yy-mm-dd' //Input Display Format 변경
            ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
            ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
            ,changeYear: true //콤보박스에서 년 선택 가능
            ,changeMonth: true //콤보박스에서 월 선택 가능                
            ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
            ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
            ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
            ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
            ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
            ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
            ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
            ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
            ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
            ,minDate: "-10Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
            ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후) 
	});
	
});

// 페이징 처리
function goPage(num){
	$("#pageNo").val(num);
	$("#searchBtn").click();
}
</script>
</head>
<body>
<a href="uploadList"><button>업로드리스트</button></a><br /><br />
<a href = "fileDown?saveName=1599132950304_7eleven.jpg&realName=7eleven.jpg">7eleven.png 다운로드</a>

<!-- 업로드 다른방법 : 리스트화면 -->
<form name = "frm1" id = "frm1" enctype="multipart/form-data" method = "post" action = "fileUpload1">
	<table border="1">
		<th>
			<input type = "file" name = "txt1" id = "txt1" onchange="fncImageChk(this)">
			<input type = "file" name = "txt2" id = "txt2" onchange="fncImageChk(this)">
			<input type = "file" name = "txt3" id = "txt3" onchange="fncImageChk(this)"><br /><br />
			<input type = "submit" name = "btn" id = "btn" value = "업로드버튼">
		</th>
	</table>
</form><br /><br />

<form id="searchFrm" name="searchFrm">
	<input type="hidden" name="pageNo" id="pageNo" value="1">
	<input type="hidden" name="listSize" id="listSize" value="10">
	<!-- 
		검색기능 
		- 컨트롤러의 list.do로 맵핑되고, mem_name, board_subject, board_content값을 매개값으로 넘긴다.
		- 검색옵션은 작성자, 제목, 내용, 작성자+제목+내용으로 검색할 수 있도록 한다.	
	-->
	<select name="search_option" id="search_option" >
		<option value="non_action" >선택</option>
		<option value="MEM_NAME" >	작성자</option>
		<option value="BOARD_SUBJECT" > 제목</option>
		<option value="BOARD_CONTENT" >제목 + 내용</option>		
	</select>
	<input type="text" id="keyword" name="keyword" value="${keyword}"/>
	<input type="submit" id="searchBtn" name="searchBtn" value="검색">
	<!-- input type="date" name="stDate"도 달력이 된다. -->
	<br /><br />
	<!-- datepicker -->
	<p>조회기간 : 
		<input type="text" id="start_date" name="start_date" value="${start_date }" />
		~
		<input type="text" id="end_date" name="end_date" value="${end_date }"  />
	</p>
</form>

<form id=listFrm name=listFrm>
<%-- 	<a href="${pageContext.request.contextPath}/write"><button>글쓰기</button></a>	 --%>
	<button type="button" onclick="location.href='${pageContext.request.contextPath}/write'">글쓰기</button>	
	<!-- form으로 감싸면 잘되던 a태그도 안먹힌다. 그럴때는 button타입으로 onclick=location.href를 사용해라. -->
	<button id="deleteBtn1" name="deleteBtn1">삭제1</button>
	<button id="deleteBtn2" name="deleteBtn2">삭제2</button><br /><br />
	
	<table border="1" style="margin: 10px">
		<tr>
			<th>선택</th>
			<th>글번호</th>
			<th>작성자(ID)</th>
			<th>제목</th>
			<th>작성일</th>
			<th>수정일</th>
			<th>조회수</th>
		</tr>

		<c:forEach items="${list }" var = "list">
			<tr>
				<td><input type="checkbox" id="checkbox" name="checkbox" value="${list.seq }" /></td>
				<td>${list.seq }</td>
				<td>${list.memName }(${list.memId })</td>
				<td><a href="${pageContext.request.contextPath}/detail?seq=${list.seq }">${list.boardSubject }</a></td>
				<td>${list.regDate }</td>
				<td>${list.uptDate }</td>
				<td>${list.viewCnt }</td> 
			</tr>		
		</c:forEach> 
		
			<tr>
				<td colspan="7">
					<c:if test="${pageMap.curBlock > 1}">
	                    <a href="javascript:goPage('1')">[처음]</a>
	                </c:if>
	                
	                <!-- **이전페이지 블록으로 이동 : 현재 페이지 블럭이 1보다 크면 [이전]하이퍼링크를 화면에 출력 -->
	                <c:if test="${pageMap.curBlock > 1}">
	                    <a href="javascript:goPage('${pageMap.prevPage}')">[이전]</a>
	                </c:if>
	                
	                <!-- **하나의 블럭에서 반복문 수행 시작페이지부터 끝페이지까지 -->
	                <c:forEach var="num" begin="${pageMap.blockBegin}" end="${pageMap.blockEnd}">
	                    <!-- **현재페이지이면 하이퍼링크 제거 -->
	                    <c:choose>
	                        <c:when test="${num == pageMap.curPage}">
	                            <span style="color: red">${num}</span>&nbsp;
	                        </c:when>
	                        <c:otherwise>
	                            <a href="javascript:goPage('${num}')">${num}</a>&nbsp;
	                        </c:otherwise>
	                    </c:choose>
	                </c:forEach>
	                
	                <!-- **다음페이지 블록으로 이동 : 현재 페이지 블럭이 전체 페이지 블럭보다 작거나 같으면 [다음]하이퍼링크를 화면에 출력 -->
	                <c:if test="${pageMap.curBlock <= pageMap.totBlock}">
	                    <a href="javascript:goPage('${pageMap.nextPage}')">[다음]</a>
	                </c:if>
	                
	                <!-- **끝페이지로 이동 : 현재 페이지가 전체 페이지보다 작거나 같으면 [끝]하이퍼링크를 화면에 출력 -->
	                <c:if test="${pageMap.curPage <= pageMap.totPage}">
	                    <a href="javascript:goPage('${pageMap.totPage}')">[끝]</a>
	                </c:if>
				</td>
			</tr>
		
	</table>
</form>
</body>
</html>