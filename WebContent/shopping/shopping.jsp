<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.mage.dao.*, com.mage.po.*, java.util.*"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/Myebuy/js/jquery-1.11.1.js"> </script>
<link type="text/css" rel="stylesheet" href="css/style.css" />
<script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
<script src="js/crypto-js/crypto-js.js"></script>
<script src="js/crypto-js/core.js"></script>
<script src="js/crypto-js/cipher-core.js"></script>
<script src="js/crypto-js/tripledes.js"></script>
<script src="js/crypto-js/mode-ecb.js"></script>
<script src="js/crypto-js/sha256.js"></script>
<script src="js/jsrsasign-master/jsrsasign-all-min.js"></script>
<script type="text/javascript" src="js/des.js"></script>
</head>
<body>
<%
	UserDao dao = new UserDao();
	User user = (User)(request.getSession().getAttribute("user"));
	int ebuyid = user.getId();
	String id = user.getBankid();
	String enc_sign_id = (String)(request.getSession().getAttribute("enc_sign_id"));
	//System.out.println("客户端：" + enc_sign_id);
%>

<input type="hidden" id="sign_id" name="sign_id" value="">
<input type="hidden" id="sign_price" name="sign_price" value="">

	<div id="shopping">
		<form id="form" name="form" action="http://172.20.43.65:8080/Bank/pay.jsp" method="get" onsubmit="return openResult()">
		<input type="hidden" id="ebuyid" name="ebuyid" value=<%=ebuyid%>>
		<input type="hidden" id="id" name="id" value=<%=id%>>
		<input type="hidden" id="price" name="price" value="">
        <input type="hidden" id="enc_sign_id" name="enc_sign_id" value=<%=enc_sign_id%>>
        <input type="hidden" id="enc_sign_price" name="enc_sign_price" value="">
			<table>
				<tr>
					<th style="text-align:center">商品名称</th>
					<th>商品单价</th>
					<th>金额</th>
					<th>购买数量</th>
					<th>操作</th>
				</tr>
				<!-- 展示已添加的商品信息 -->
				<c:forEach items="${shoppingCarProductList}" var="shoppingCarProduct">
					<tr class="productTr">
						<td class="thumb">
							<img  src="${shoppingCarProduct.proPic}"  class="imgs"/>
							<a href="product?actionName=lookproduct&productId=${shoppingCarProduct.productId}">${shoppingCarProduct.proName}</a>
						</td>
						<td class="price"><!-- 单价 -->
<span>￥<label class="price_" id="price_${shoppingCarProduct.id}">${shoppingCarProduct.price}</label> </span>
						</td>
						<td class="price"><!-- 单价*个数= 总价-->
<span>￥<label class="price_sum_" id="price_sum_${shoppingCarProduct.id}">${shoppingCarProduct.price*shoppingCarProduct.num}</label> </span>					
						</td>
						<td class="number"><!-- 商品的数量 操作-->
						
						<input type="hidden" id="productId" value="${shoppingCarProduct.id}"/>
						
						<input type="button" class="min" value=" - "/>
		<input  class="text_box" id="text_box_${shoppingCarProduct.id}" type="text" style="width:30px;text-align:center;" value="${shoppingCarProduct.num}"  disabled="disabled"/>
						<input type="button" class="add" value=" + "/>
						</td>
						<td class="delete">
						<a href="javascript:removeShopping(${shoppingCarProduct.id})">删除</a>
						</td>
					</tr>
				</c:forEach>
				
			</table>
			<!-- 订单内容显示 -->
			<div class="shopping_list_end">
				<ul>
					<li class="shopping_list_end_2">
						￥<lable id="product_total"></lable>
					</li>
					<li class="shopping_list_end_3">商品金额总计:</li>
				</ul>
			</div>
			<div class="button">
				<input type="submit"  value=""/>
			</div>
		</form>
	</div>

	
	<!-- 个人信息的显示 -->
	<div style="background-color:#cddbb8;margin-top:10px;font-size:20px;height:40px;text-align:center;">
		<div style="padding:5px">
			<b>收件人:</b>${user.trueName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<b>收货地址:</b>${user.address}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<b>联系电话:</b>${user.mobile}
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){  //自动加载
		
		setTotal();//调用计量方法
		
		//商品的总价
		function setTotal(){
			var sub = 0;
			$(".productTr").each(function(){
				
				//获取商品的数量
				var number = $(this).find('input[class=text_box]').val();
				//获取商品的价钱
				var price = $(this).find('label[class=price_]').html();
				//计算  数量*价钱=总价钱
				sub += number * price;
				
			});
			 //把总结显示出去
			$("#product_total").html(sub);
			form.price.value = sub;
		}
		
		
		//对 “ + ”按钮进行操作
		$(".add").click(function(){
			//获取参数
			var productId = $(this).parent().find("#productId").val();
			
			//调用ajax修改购物车记录的数量
			$.ajax({
				type:"post",
				url:"shopping",
				data:{
					"productId":productId,
					"actionName":"addNum"
				},
				success:function (row){
					//返回商品记录id
					if(row==1){
						//修改前台数目显示
						var t = $("#text_box_"+productId);
						t.val(parseInt(t.val())+1);
						
						//修改金额显示
						var price = $("#price_"+productId).html();
						var sum = $("#price_sum_"+productId).html(parseInt(price)*t.val()); 
						
						//调用总价
						setTotal(); 
					}else{
						alert("增加失败");
					}
				}
				
			});
			
		})
		
		//对 “ - ” 按钮进行操作
		$(".min").click(function(){
			
			//获取参数
			var productId = $(this).parent().find("#productId").val();
			var t = $("#text_box_"+productId);
			//判断数量是否大于0
			if(t.val()>1){
				//调用ajax修改购物车记录的数量
				$.ajax({
					type:"post",
					url:"shopping",
					data:{
						"productId":productId,
						"actionName":"minNum"
					},
					success:function (row){
						//返回商品记录id
						if(row==1){
							//修改前台数目显示
							t.val(parseInt(t.val())-1);
							//修改金额显示
							var price = $("#price_"+productId).html();
							var sum = $("#price_sum_"+productId).html(parseInt(price)*t.val()); 
							//调用总价
							setTotal(); 
						}else{
							alert("减少失败");
						}
					}
					
				});
			}
			
			
		})
		
		
	});
	//删除购物车中的订单
	function removeShopping(productId){
		if(confirm("您确定要删除这个商品吗？")){
			$.ajax({
				type:"post",
				url:"shopping",
				data:{
					"productId":productId,
					"actionName":"removeProduct"
				},
				success:function (row){
					//返回商品记录id
					if(row==1){
						location.reload(); 
						alert("删除成功");
					}else{
						alert("删除失败");
					}
				}
				
			});
		}
	}
	
	function openResult(){    /* 绑定事件 */
		
		//<form id="form" name="form" action="http://172.20.43.65:8080/Bank/outerface" method="get" onsubmit="return openResult()">
		//<form id="form" name="form" action="order?actionName=save" method="post" onsubmit="return openResult()">
		var price = document.getElementById("price").value;
		var id = document.getElementById("id").value;
		var total = price;
		var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
		//var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDbLfetH+n4+HWqj2hkUnswFDyAvosu7UxBqLp+5nURtEYcSBaXqF/5t+0ufi6cqfWpE5b0g7gULIWksbiAil/HKGWRnDyV9D3e2hB97yZlVGSRA3b/eweBXl79Mz5r3Xf54/Zph1NkcN7RgG9EsAYo2jHLxnjryhkIWu+CKkTFwIDAQAB"
		var privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw=="
		var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(publicKey);
	    var enc_id = encrypt.encrypt(document.getElementById("id").value);
	    var enc_price = encrypt.encrypt(document.getElementById("price").value);
	    
	    //先将用户名  密码和身份证号码进行RSA加密
	    //var sign_id = hex_sha256(document.getElementById("id").value);
	    //var sign_price = hex_sha256(document.getElementById("price").value);
	    //document.getElementById("sign_id").value = sign_id;
	    //document.getElementById("sign_price").value = sign_price;
	    //var sign = new JSEncrypt();
	    //sign.setPrivateKey(privateKey);
	    //var enc_sign_id = sign.sign(document.getElementById("id").value, CryptoJS.SHA256);
	    //var enc_sign_price = sign.sign(sign_price, CryptoJS.SHA256, "sha256");
	    //进行哈希值的校验
	    
	    form.id.value = enc_id;
	    form.price.value = enc_price;
	    //alert(form.price.value);
	    //form.enc_sign_id.value = enc_sign_id;
	    //form.enc_sign_price.value = enc_sign_price;
	  	//var username = document.getElementById("enc_sign_id").value;
		//alert(username);
		var r = confirm("总金额为" + total + "元，是否确认订单");

		if (r == true) {
			return true;
		} else {
			return false;
		}
	}
	

</script>












</html>