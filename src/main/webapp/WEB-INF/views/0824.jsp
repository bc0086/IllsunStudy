<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>
<script>

$(function(){
	
	//id 문자(영어), 숫자만 
	$('#id').keyup(function(){
		id.value = id.value.replace(/[^a-z|^0-9]/gi, '');//영문 숫자만 입력
		//id.value = id.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');//한글입력 막기
	});
	
	//아이디 체크
	$('#idChk').click(function(){
	      var id = $('#id').val();
	      if(id=='test')  {
	        alert("중복된 아이디입니다.");
	        $('#id').focus();
	        return false;
	      }else{
    	  	alert("사용할 수 있는 아이디입니다. ");
	      }
	      
	   });
	


 	//이름
 	
 	//$(this).val().slice(0,5));
	  $("#nameForm").on('keyup', function(){
	      var text = $('#nameForm').val().replace(/[^ㄱ-힣a-zA-Z]/gi, '');//한글, 영어만 입력
	      if(text.length > $(this).attr("maxlength")){
	    	  alert("5글자 이하만 입력 가능합니다.")
	          $("#nameForm").focus();
	          return;
	      }
	        $(this).val(text);
	  });

  
  
  //휴대폰 숫자만
  $('#ph2').keyup(function(){ 
	  ph2.value = ph2.value.replace(/[^0-9]/g,"");//숫자만 입력
	});
  
  $('#ph3').keyup(function(){ 
	  ph3.value = ph3.value.replace(/[^0-9]/g,"");//숫자만 입력
	});
  
  
  
  //주소 문자(한글, 영어), 숫자
  $('#address').keyup(function(){ 
    address.value = address.value.replace(/[^ㄱ-힣a-zA-Z0-9]/gi, '');//한글, 영어, 숫자만 입력
	});
  
  
  
  
  $("#jumin1").on('keyup', function(){
      var text = $('#jumin1').val().replace(/[^0-9]/g,"");//숫자만 입력
      if(text.length >= $(this).attr("maxlength")){
          $("#jumin2").focus();
          return;
      }
        $(this).val(text);
  });

	// $('#jumin2').keyup(function(){
	// 	//jumin2.value = jumin2.value.replace(/[^0-9]/g,"");
  //   jumin2.value = jumin2.value.replace(/(?<=.{1})./gi, "*");
	// });


  $("#jumin2").on('keydown', function(e){
          /*  jumin2.value = jumin2.value.replace(/[^0-9]/g,"");//숫자만 입력 */
            var inVal="";
            if (event.keyCode==8) {             //백스페이스
                if(confirm("주민번호 뒷자리 전체를 삭제 후 다시 입력하시겠습니까?")){
                    $("#juminE").val("");
                    $("#jumin2").val("");
                }       
                     
            }else if(e.keyCode >=96 && e.keyCode <=105){
                switch (e.keyCode) {
                    case 96 : inVal=0; break;
                    case 97 : inVal=1; break;
                    case 98 : inVal=2; break;
                    case 99 : inVal=3; break;
                    case 100 : inVal=4; break;
                    case 101 : inVal=5; break;
                    case 102 : inVal=6; break;
                    case 103 : inVal=7; break;
                    case 104 : inVal=8; break;
                    case 105 : inVal=9; break;
                }
            }else if(e.keyCode >=48 && e.keyCode <= 57 ){
               inVal = String.fromCharCode(e.keyCode);               
            }else{
                e.preventDefault();
                return false;
            }            
            var text = $(this).val();
            if(text.length >= $(this).attr("maxlength")){
                return;
            }
          
            //주민번호에 넣고..
            var jume = $("#juminE").val()+inVal;
            $("#juminE").val(jume.replace(/[^0-9]/g,""));               
        }).on('input',function(e){
            $(this).val($(this).val().replace(/(?<=.{1})./gi, "*"));
        });  
            
  
  //가입신청 버튼 클릭시 실행될 함수
  //비밀번호 확인, 이메일 유효성, 주민번호 유효성
  $('#regi_btn').click(function(){
       //패스워드 입력란에 입력된 내용을 가져온다. 
      var compareStr1 = $('#pass').val();
      var compareStr2 = $('#pass2').val();

      if($('#id').val()=="")  {
        alert("아이디를 입력하세요.")
        $('#id').focus();
        return false;
      }
      
      if(compareStr1=='')  {
        alert("비밀번호를 입력하세요.")
        $('#pass').focus();
        return false;
      }
      if(compareStr1==compareStr2){
          alert("비밀번호가 일치합니다. ");
      }
      else if(compareStr1!=compareStr2){
          alert("비밀번호가 일치하지 않습니다. ");
          $('#pass').focus();
          return false;
      }
      if($('#nameForm').val()=="")  {
          alert("이름을 입력하세요.")
          $('#nameForm').focus();
          return false;
      }
      if($('#email1').val()=="")  {
          alert("이메일을 입력하세요.")
          $('#email1').focus();
          return false;
      }
      if($('#email2').val().indexOf('.')==-1) {
            alert("이메일 형식이 잘못되었습니다.");
            $('#email2').focus();
            return false;
      }
      
      if($('#ph2').val()=="")  {
          alert("휴대폰번호를 입력하세요.")
          $('#ph2').focus();
          return false;
      }
      if($('#ph3').val()=="")  {
          alert("휴대폰번호를 입력하세요.")
          $('#ph3').focus();
          return false;
      }
      if($('#jumin1').val()=="")  {
          alert("주민번호를 입력하세요.")
          $('#jumin1').focus();
          return false;
      }
      if($('#jumin2').val()=="")  {
          alert("주민번호를 입력하세요.")
          $('#jumin2').focus();
          return false;
      }
      if($('#address').val()=="")  {
          alert("주소를 입력하세요.")
          $('#address').focus();
          return false;
      }

               
   });


	
});//제이쿼리


</script>
</head>
<body>
	<form>
		<table width="1400" height="650">
			<tr>
				<td width="100%" height="10%">회원가입</td>
			</tr>
			<tr>
				<td height="60%" align="center" valign="top">
					<hr>
					<br>
					<p align="left" style="padding-left: 160px">
						<br>
						<br> ID : <input type="text" size="10" maxlength="15"
							name="id" id="id"> <input type="button" name="idChk" id="idChk"
							value="중복체크"> <br>
            <br> 
            비밀번호 : <input type="password" size="15" maxlength="20" name="pass" id="pass"><br>
            <br> 
            비밀번호 확인 : <input type="password" size="15" maxlength="20" name="pass2" id="pass2"><br>
						<br> 이름 : <input type="text" size="13" name="nameForm" id="nameForm" maxlength="5"><br>
						<br> 이메일 : <input type="text" size="15" name="email1"
							id="email1">@<input type="text" size="15" name="email2"
							id="email2"><br>
						<br> 휴대폰 : <select name="ph1">
							<option value="010">010</option>
							<option value="011">011</option>
							<option value="016">016</option>
							<option value="017">017</option>
							<option value="019">019</option>
						</select> - <input type="text" name="ph2" size="5" maxlength="4" id="ph2">
						- <input type="text" name="ph3" size="5" maxlength="4" id="ph3"><br>
						<br> 성별 : <input type="radio" name="gender" value="남자">
						남자&nbsp;&nbsp; <input type="radio" name="gender" value="여자">
						여자<br>
            <br> 
            주민번호 : <input type="text" name="jumin1" id="jumin1" maxlength="6">
            - <input type="tel" name="jumin2" id="jumin2" maxlength="7"><br>
              <input type="hid-den" id="juminE" name="inputValue" >
						<br> 주소 : <input type="text" name="address" id="address" size="15"
							maxlength="15"><br> *주소는 (시/도)만 입력해주세요 (예: 경기도,
						서울특별시, 경상남도 등)
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<hr>
          <br> 
          <input type="button" id="regi_btn" value="가입신청">&nbsp;
          <input type="reset" value="다시입력">&nbsp; 
          <input type="button" value="취소">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>