//<script type="text/javascript" src="js/register.js"></script>
function checkForm(){
		// 获取表单元素
		var userName=$("#userName").val();//用户名
		var password=$("#password").val();//密码
		var rePassWord=$("#rePassWord").val();//确认密码
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
	    var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(publicKey);
	    var enc_username = encrypt.encrypt(loginForm.userName.value);
	    var enc_password = encrypt.encrypt(loginForm.password.value);
	    //先将用户名  密码和身份证号码进行RSA加密
	    var ssign_username = hex_sha256(loginForm.userName.value);
	    var ssign_password = hex_sha256(loginForm.password.value);
	    //进行哈希值的校验
	    //loginForm.userName.value = enc_username;
	    //var username = document.getElementById("userName").value;
		//alert(username);
	    //loginForm.password.value = enc_password;
	    loginForm.sign_username.value = ssign_username;
	    loginForm.sign_password.value = ssign_password;
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
