<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/bootstrap3.min.css" >  
<link type="text/css" rel="stylesheet" href="css/style.css" />     

</head>
<body>
<!-- NAV start  -->
<div id="logo">
	<img src="images/logo.gif" />
</div>
<!-- 首页右侧注册与登录部分 -->
 <div class="help">
	<c:choose>
		<c:when test="${not empty user}">
			<!-- 真实数据要查，要传过来！ 三目运算符-->
			<a href="shopping?actionName=showShopping" class="shopping">
			购物车(${shoppingCarProductList == null ? 0 : shoppingCarProductList.size() }件商品)</a>
			<a href="user?actionName=userCenter">${user.userName }</a>
			<a href="javascript:logout();">注销</a>
			<a href="register.jsp">注册</a>
			<a href="comment?oper=list">留言</a>
			<c:if test="${user.status==2 }">
				<a href="backindex.jsp">进入后台</a>
			</c:if>
				
		</c:when>
		<c:otherwise>
			<a href="javascript:checkLogin()" class="shopping">购物车</a>
			<a href="login.jsp">登录</a>
			<a href="register.jsp">注册</a>
			<a href="javascript:checkLogin()">留言</a>
		</c:otherwise>
	</c:choose>
	<form action="product" method="post"> 
		 <input type="text" name="product" autocomplete="off" /> 
		 <input type="hidden" name="actionName" value="search"/>
		  <input type="submit" value="搜索"/> 
	</form> 
</div> 
<!-- 大分类的NAV -->
<div class="nav nav-pills" >
		<li><a href="index" >首页</a></li>
		<c:forEach items="${bigTypeList}" var="bType">
			<li>
			<a href="product?actionName=bigType&id=${bType.id}">${bType.name}</a>
			</li>
		</c:forEach>
</div>
<!-- 大分类的NAV -->
<!-- NAV 二级导航 -->
<div id="childNav" >
	<div class="wrap">
		
	</div>
</div>
</body>
<script type="text/javascript">
	function logout(){
		if(confirm('您确定要退出系统吗？')){
			window.location.href="user?actionName=logout";
		}
	}
	
	function checkLogin(){
	 if('${user.userName }' == ''){
			alert("请先登录");
		}else {
			window.location.href="shopping?actionName=list";
	 	}
	}

</script>
</html>