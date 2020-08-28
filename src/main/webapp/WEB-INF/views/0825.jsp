<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>
<script>

  //전체체크박스 클릭할 때 
  $(function(){
	$('#allCheck').click(function(){
		//alert("전체 선택");
    if($("#allCheck").prop("checked")){
        //input태그의 name이 sports인 태그들을 찾아서 checked옵션을 true로 정의
        $("input[name=sports]").prop("checked",true);
        //클릭이 안 되있으면
    }else{
        //input태그의 name이 sports인 태그들을 찾아서 checked옵션을 false로 정의
        $("input[name=sports]").prop("checked",false);
    }
	});
	
  //체크박스 클릭할 때 (체크박스 name값이 sports)
  $('input[name=sports]').click(function(){
    if($("input[name='sports']:checked").length == 9){
        $("#allCheck").prop("checked",true);
    }else {
        $("#allCheck").prop("checked",false);
    }
    var str1 = "";
    var str2 = $(this).val();
    var str3 = "";
   
    if($(this).prop("checked")){
      //alert("체크됨");
      str1 += str2;
      //$('#show').empty();
      $('#show').append(" "+str1);
    }
    else{
      //alert("체크해제");
      str1 = $('#show').text();
      //alert(str1);
      str3 = str1.replace(str2, '');
      $('#show').empty();
      $('#show').append(str3);
    }

  }) 

  //버튼 클릭했을 때 
  $('#btn').click(function(){
    if($("input[name='sports']:checked").length <= 4){
      alert("성공");
    }
    else if($("input[name='sports']:checked").length == 9){
      alert("성공");
    }
    else {
      alert("실패");
    }
  });
	
});//제이쿼리
</script>
</head>
<body>
	<input type="checkbox" id="allCheck"/><label for="allCheck">전체</label>
	<button id="btn">버튼</button> <br/>
      <input type="checkbox"  id="soccer" name="sports" value="축구"/><label for="soccer">축구</label>
      <input type="checkbox"  id="basketball" name="sports" value="농구"/><label for="basketball">농구</label>
      <input type="checkbox"  id="volleyball" name="sports" value="배구"/><label for="volleyball">배구</label>
      <input type="checkbox"  id="baseball" name="sports" value="야구" /><label for="baseball">야구</label>
      <input type="checkbox"  id="foot_volleyball" name="sports" value="족구"/><label for="foot_volleyball">족구</label>
      <input type="checkbox"  id="table_tennis" name="sports" value="탁구"/><label for="table_tennis">탁구</label>
      <input type="checkbox"  id="waterball" name="sports" value="수구"/><label for="waterball">수구</label>
      <input type="checkbox"  id="dodgeball" name="sports" value="피구"/><label for="dodgeball">피구</label>
      <input type="checkbox"  id="billiards" name="sports" value="당구"/><label for="billiards">당구</label> <br/>
  <span id="show"></span>
  
  
</body>
</html>









