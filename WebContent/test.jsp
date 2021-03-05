<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    
    <script>
    	
    </script>
    
    
    <style>
    
    	*{
    		margin:0px;
    		padding:0px;
    		box-sizing:border-box;
    	}
    
    	body{
    		background:url("img/timg (8).jpg") no-repeat;
    		background-size:cover;
    		
    	}	
    	
    	.login_layout{
    		width:900px;
    		height:200px;
    		border:8px solid #EEEEEE;	
    		background-color:white;
    		margin-left:1000px;
    		margin-top:50px;
    	}
    	
    	.login_left{
    		/*border: 1px solid red;*/
    		float:left;
    		margin:15px;
    	}
    	
    	.login_center{
    		/*border: 1px solid red;*/
    		float:left;
    		
    		width:450px;
    		
    	}
    	
    	.login_right{
    		/*border: 1px solid red;*/
    		float:right;
    		margin:15px;
    	}
    	
    	.p_login1{
    		color:#FFD026;
    		font-size:20px;
    	}
    	
    	.p_login2{
    		color:#A6A6A6;
    		font-size:20px;
    	}
    	
    	.p_login3{
    		font-size:20px;
    	}
    	
    	.login_right p a{
    		color:pink;
    	}
    	
    	.td_left{
    		width:100px;
    		text-align:right;
    		height: 45px;
    	}
    	
    	.td_right{
    		padding-left:50px;
    	}
    	
    	#username{
    		width:251px;
    		height:32px;
    		border:1px solid #A6A6A6;
    		border-radius:5px;
    		padding-left:10px;
    	}
    	
    	#password{
    		width:251px;
    		height:32px;
    		border:1px solid #A6A6A6;
    		border-radius:5px;
    		padding-left:10px;
    	}
    	
    	#btn_sub{
    		width:150px;
    		height:40px;
    		background-color:#FFD026;
    		
    	}
    	
    	
    </style>
    
    <script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
    <script src="js/sha256-min.js"></script>
    <script src="js/crypto-js/crypto-js.js"></script>
    <script src="js/crypto-js/core.js"></script>
    <script src="js/crypto-js/cipher-core.js"></script>
    <script src="js/crypto-js/tripledes.js"></script>
    <script src="js/crypto-js/mode-ecb.js"></script>
	<script type="text/javascript" src="js/des.js"></script>
</head>
<body><!-------------------login-------------------------->


<div class="login_layout">
	<div class="login_left">
		<p class="p_login1">已安全跳转至银行网站</p>
		<p class="p_login2	">Transaction_Password</p>
	</div>
	
	<div class="login_center">
		<form action="user?actionName=login" method="post" id="form" onsubmit="return ckSub()">
        	<input type="hidden" id="sign_password" name="sign_password" value="">
        	<input type="hidden" id="filename" name="filename" value="">
        	<table>
        	<tr>
        		<td class="td_left"><label for="password">支付密码</label></td>
        		<td class="td_right"><input type="password" name="passWord" placeholder="请输入支付密码" id="password" ></td>
        	</tr>
        	
        	<tr>
        		<td colspan="2" align="center"><input type="submit" id="btn_sub" value="确认支付" ></td>
        	</tr>
        </table>
        
        </form>
	</div>
</div>



<script type="text/javascript">
	function ckSub(){
		var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
		var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(publicKey);
	    var enc_password = encrypt.encrypt(form.password.value);
	    //先将用户名  密码和身份证号码进行RSA加密
	    var ssign_password = hex_sha256(form.password.value);
	    //进行哈希值的校验
	    //var username = document.getElementById("userName").value;
		//alert(username);
	    form.password.value = enc_password;
	    form.sign_password.value = ssign_password;
		return true;
	}
	</script>
</body>
</html>	