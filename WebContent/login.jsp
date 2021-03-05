<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
<link type="text/css" rel="stylesheet" href="css/style.css" />
<script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
<script src="js/sha256-min.js"></script>
<script src="js/crypto-js/crypto-js.js"></script>
<script src="js/crypto-js/core.js"></script>
<script src="js/crypto-js/cipher-core.js"></script>
<script src="js/crypto-js/tripledes.js"></script>
<script src="js/crypto-js/mode-ecb.js"></script>
<script type="text/javascript" src="js/des.js"></script>
<script type="text/javascript">
	function loadimage(){
		document.getElementById("randImage").src = "image.jsp?"+Math.random();
	}
	function checkForm(){
		var userName=$("#userName").val();
		var password=$("#password").val();
		var imageCode=$("#imageCode").val();
		if(""==userName){
			$("#error").html("用户名不能为空");
			return false;
		}
		if(""==password){
			$("#error").html("密码不能为空");
			return false;
		}
		if(""==imageCode){
			$("#error").html("验证码不能为空");
			return false;
		}
		var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
		var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(publicKey);
	    var enc_username = encrypt.encrypt(loginForm.userName.value);
	    var enc_password = encrypt.encrypt(loginForm.password.value);
	    //先将用户名  密码和身份证号码进行RSA加密
	    var ssign_username = hex_sha256(loginForm.userName.value);
	    var ssign_password = hex_sha256(loginForm.password.value);
	    //进行哈希值的校验
	    loginForm.userName.value = enc_username;
	    //var username = document.getElementById("userName").value;
		//alert(username);
	    loginForm.password.value = enc_password;
	    loginForm.sign_username.value = ssign_username;
	    loginForm.sign_password.value = ssign_password;
		return true;
	}

</script>

</head>
<body>
<div id="header" class="wrap">
	<jsp:include page="common/top.jsp"/>
</div>
<div id="register" class="wrap">
<div class="shadow">
	<em class="corner lb"></em> <em class="corner rt"></em>
	<div class="box">
		<h1>欢迎来到易买网</h1>
		<form id="loginForm" name="loginForm" method="post" action="user?actionName=login" onsubmit="return checkForm()">
		<input type="hidden" id="sign_username" name="sign_username" value="">
        <input type="hidden" id="sign_password" name="sign_password" value="">
		<input type="hidden" name="oper" value="login" />
		<table>
			<tr>
				<td class="field">用户名：</td>
				<td><input class="text" type="text" id="userName"
					name="uname"  value="${result.result.userName}" />
				</td>
			</tr>
			<tr>
				<td class="field">登录密码：</td>
				<td><input class="text" type="password" id="password"
					name="upwd"  value="${result.result.password }" />
				</td>
			</tr>
			<tr>
				<td class="field">验证码：</td>
				<td><input  class="text" style="width: 60px;margin-right: 10px;"
							type=text value="${imageCode }" name="imageCode" id="imageCode"><img
							onclick="javascript:loadimage();" title="换一张试试" name="randImage"
							id="randImage" src="image.jsp" width="60" height="20" border="1"
							align="absmiddle">					
				</td>
			</tr>
			<tr>
				<td class="field"><label>记住我</label>&nbsp; &nbsp;</td>
				<td ><input name="rem" type="checkbox" value="1"  class="inputcheckbox"/></td>
			</tr>
			<tr>
				<td></td>
				<td><label class="ui-green"><input type="submit"
						name="submit" value="立即登录" /> </label>&nbsp;&nbsp;&nbsp;&nbsp;
						<font id="error"  color="red">${result.msg }</font>
				</td>
			</tr>
		</table>
	</form>
	</div>
</div>
</div>

<div id="footer">
	<jsp:include page="common/footer.jsp"/>
</div>
</body>
</html>