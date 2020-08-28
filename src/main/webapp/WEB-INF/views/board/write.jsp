<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>
<script>
$(function(){

        $("#inserBtn").click(function(){
    
    		if ($('#memId').val()==""){
    			alert("아이디를 입력하세요");
    			$('#memId').focus();
 				return false;	
 			}
    		if ($('#memName').val()==""){
    			alert("이름을 입력하세요");
    			$('#memName').focus();
 				return false;	
 			}
    		if ($('#boardSubject').val()==""){
    			alert("제목을 입력하세요");
    			$('#boardSubject').focus();
 				return false;	
 			}
    		if ($('#boardContent').val()==""){
    			alert("내용을 입력하세요");
    			$('#boardContent').focus();
 				return false;	
 			}
        	
        	$("#frm").attr("action", "writeAction.do").attr("method", "post").submit();
        })
        
        $("#updateBtn").click(function(){
        	
    		if ($('#memId').val()==""){
    			alert("아이디를 입력하세요");
    			$('#memId').focus();
 				return false;	
 			}
    		if ($('#memName').val()==""){
    			alert("이름을 입력하세요");
    			$('#memName').focus();
 				return false;	
 			}
    		if ($('#boardSubject').val()==""){
    			alert("제목을 입력하세요");
    			$('#boardSubject').focus();
 				return false;	
 			}
    		if ($('#boardContent').val()==""){
    			alert("내용을 입력하세요");
    			$('#boardContent').focus();
 				return false;	
 			}
    		
        	$("#frm").attr("action", "editAction.do").attr("method", "post").submit();
        })
       
});
</script>
</head>
<body>

	<form name="frm" id="frm" >
	<input type="hidden" name="seq" id="seq" value="${detail.seq }" />
		아이디 : <input type="text" name="memId" id="memId" value="${detail.memId }"/><br/>
		작성자 : <input type="text" name="memName" id="memName" value="${detail.memName }" /><br/>
		제목 : <input type="text" name="boardSubject" id="boardSubject" value="${detail.boardSubject }"/><br/>
		내용 : <textarea name="boardContent" id="boardContent" value="">${detail.boardContent }</textarea><br/>
		
		<c:if test="${empty detail}">
	 		<button id="inserBtn" >등록</button>&nbsp;
		</c:if>
		<c:if test="${not empty detail}">
	 		<button id="updateBtn" >수정</button>&nbsp;
		</c:if>
			<button type="button" id="cancel" onclick="location.href='${pageContext.request.contextPath}/list'" >취소</button>
			
	</form>	
	
	
</body>
</html>