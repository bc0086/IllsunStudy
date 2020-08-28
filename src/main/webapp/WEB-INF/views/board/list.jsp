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
});

</script>
</head>
<body>
<form id=listFrm name=listFrm>
	<a href="${pageContext.request.contextPath}/write"><button>글쓰기</button></a>	
	<button id="deleteBtn1" name="deleteBtn1">삭제1</button>
	<button id="deleteBtn2" name="deleteBtn2">삭제2</button>
	
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
	</table>
</form>
</body>
</html>