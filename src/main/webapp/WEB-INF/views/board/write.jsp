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
	var f = document.getElementById("frm");
	
		// 글쓰기 등록 버튼
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
        	
        	$("#frm").attr("action", "writeAction.do").attr("method", "post").attr("enctype", "multipart/form-data").submit();
        })
        
        // 글수정 버튼
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

// 업로드 유효성검사
function fncImageChk(fileInput){
	
	var reg = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
	if(!reg.test(fileInput.value)){
		alert('이미지 올리시오');
		fileInput.value = "";
	}
	
	var file = fileInput.files[0];
	var _URL = window.URL || window.webkitURL;
		// webkit : url을 읽어오는 방법
	var img = new Image();
	
	img.src = _URL.createObjectURL(file);
	img.onload = function() {
		
		if(img.width > 300 || img.height > 300){
			alert("사이즈 확인하시요");
			fileInput.value = "";
		}
	}
}
</script>
</head>
<body>
	<form name="frm" id="frm" >
	<input type="hidden" name="seq" id="seq" value="${detail.seq }" />
		아이디 : <input type="text" name="memId" id="memId" value="${detail.memId }"/><br/>
		작성자 : <input type="text" name="memName" id="memName" value="${detail.memName }" /><br/>
		제목 : <input type="text" name="boardSubject" id="boardSubject" value="${detail.boardSubject }"/><br/>
		내용 : <textarea name="boardContent" id="boardContent" value="">${detail.boardContent }</textarea><br/>
		
		첨부파일1 : <input type="file" name="userfile1" id="userfile1" onchange="fncImageChk(this)" />
		첨부파일2 : <input type="file" name="userfile2" id="userfile2" onchange="fncImageChk(this)" />
		첨부파일3 : <input type="file" name="userfile3" id="userfile3" onchange="fncImageChk(this)" /> 
		
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