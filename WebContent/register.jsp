<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<script type="text/javascript">
function checkForm(){
	// 获取表单元素
	var userName=$("#userName").val();//用户名
	var password=$("#password").val();//密码
	var rePassWord=$("#rePassWord").val();//确认密码
	var bankid=$("#bankid").val();//银行账户
	var mobile=$("#mobile").val();//电话
	var address=$("#address").val();//地址
	var sex=$("#sex").val();//性别
	if(userName==""){
		$("#error").html("用户名不能为空"); //向error指定的元素中添加一句话！
		return false;
	}
	if(password==""){
		$("#error").html("密码不能为空");
		return false;
	}
	if(rePassWord==""){
		$("#error").html("确认密码不能为空");
		return false;
	}
	if(rePassWord!=password){
		$("#error").html("密码要与确认密码相同");
		return false;
	}
	if(bankid==""){
		$("#error").html("银行账户不能为空");
		return false;
	}
	if(mobile==""){
		$("#error").html("手机号码不能为空");
		return false;
	}
	if(sex==""){
		$("#error").html("性别不能为空");
		return false;
	}
	if(address==""){
		$("#error").html("地址不能为空");
		return false;
	}
	var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
				  //"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB"
	var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    var enc_username = encrypt.encrypt(regForm.userName.value);
    var enc_password = encrypt.encrypt(regForm.password.value);
    //先将用户名  密码和身份证号码进行RSA加密
    var ssign_username = hex_sha256(regForm.userName.value);
    var ssign_password = hex_sha256(regForm.password.value);
    //进行哈希值的校验
    regForm.userName.value = enc_username;
    //var username = document.getElementById("userName").value;
	//alert(username);
    regForm.password.value = enc_password;
    regForm.rePassWord.value = enc_password;
    regForm.sign_username.value = ssign_username;
    regForm.sign_password.value = ssign_password;
	return true;
}
function checkUserName(){
	//获取你在该元素中写的值
	var userName=$("#userName").val();
	if(userName==""){
		//如果是空的，对不起用户不能空！
		$("#userErrorInfo").html("用户名不能为空 !");
		//鼠标仍然存在此输入框中
		$("#userName").focus();
		return;
	}else{
		//否者我的这个userErrorInfo 什么都不做！
		$("#userErrorInfo").html("");
	}
}


</script>

</head>
<body>
<div id="header" class="wrap">
	<jsp:include page="common/top.jsp"/>
</div>
<div id="register" class="wrap">
	<div class="shadow">
		<em class="corner lb"></em>
		<em class="corner rt"></em>
		<div class="box">
			<h1>欢迎注册易买网</h1>
			<ul class="steps clearfix">
				<li class="current"><em></em>填写注册信息</li>
				<li class="last"><em></em>注册成功</li>
			</ul>													<!-- onsubmit:点击事件  -->
			<form id="regForm" method="post" action="user?actionName=register" onsubmit="return checkForm()">
			<input type="hidden" id="sign_username" name="sign_username" value="">
        	<input type="hidden" id="sign_password" name="sign_password" value="">
				<table>				
					<tr>
						<td class="field">用户名(*)：</td>
						<td><input class="text" type="text" id="userName" name="userName" value="${registresult.result.userName }" onblur="checkUserName()" />&nbsp;<font id="userErrorInfo" color="red"></font></td>
					</tr>
					<tr>
						<td class="field">登录密码(*)：</td>
						<td><input class="text"  type="password" id="password" name="password" value="${registresult.result.password }"  /></td>
					</tr>
					<tr>
						<td class="field">确认密码(*)：</td>
						<td><input class="text" type="password"  id="rePassWord"  name="rePassWord" /></td>
					</tr>
					
					<tr>
						<td class="field">性别(*)：</td>
						<td>
					    <input type="radio"   name="sex" value="男" checked="checked"/>男&nbsp;&nbsp;
					    <input type ="radio" name="sex" value="女"/>女					    					    
					    </td>						
					</tr>
					<tr>
						<td class="field">出生日期(*)：</td>
						<td>
							<input  type="date"  id="birthday"  name="birthday" value="${registresult.result.birthday }" class="Wdate" />	
						</td> 
					</tr>
					<tr>
						<td class="field">身份证号：</td>
						<td><input class="text" type="text" id="dentityCode" name="dentityCode" value="${registresult.result.dentityCode }" /></td>
					</tr>
					<tr>
						<td class="field">Email：</td>
						<td><input class="text" type="text" id="email" name="email" value="${registresult.result.email }"  /></td>
					</tr>
					<tr>
						<td class="field">银行账户(*)：</td>
						<td><input class="text" type="text" id="bankid" name="bankid" value="" /></td>
					</tr>
					<tr>
						<td class="field">手机号码(*)：</td>
						<td><input class="text" type="text" id="mobile" name="mobile" value="${registresult.result.mobile }" /></td>
					</tr>
					<tr>
						<td class="field">收货地址(*)：</td>
						<td><input class="text" type="text" id="address" name="address" value="${registresult.result.address }" /></td>
					</tr>
					<tr>
						<td></td>
						<td><label class="ui-green"><input type="submit" name="submit" value="提交注册" /></label></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><font id="error" color="red">${registresult.msg }</font> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div class="clear"></div>
</div>

<div id="footer">
	<jsp:include page="common/footer.jsp"/>
</div>
<script type="text/javascript" src="js/jquery-1.11.1.js""></script>
<script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
<script src="js/sha256-min.js"></script>
<script src="js/crypto-js/crypto-js.js"></script>
<script src="js/crypto-js/core.js"></script>
<script src="js/crypto-js/cipher-core.js"></script>
<script src="js/crypto-js/tripledes.js"></script>
<script src="js/crypto-js/mode-ecb.js"></script>
<script type="text/javascript" src="js/des.js"></script>
</body>
</html>