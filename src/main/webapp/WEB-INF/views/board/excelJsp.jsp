<%@ page language="java" contentType="application/vnd.ms-excel;charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>    
<%
    response.setHeader("Content-Disposition","attachment;filename=mainList.xls");    //디폴트 파일명 지정
    response.setHeader("Content-Description", "JSP Generated Data"); 
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<body>
<!-- 엑셀에서 보여줄 부분만 list에서 뽑으면 된다. -->
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

</body>
</html>