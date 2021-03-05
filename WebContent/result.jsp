<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<%
	int ebuyid = (int)request.getSession().getAttribute("ebuyid");
%>
	<div id="header" class="wrap">
		<jsp:include page="common/top.jsp" />
	</div>
	<div id="main" class="wrap">
		<div class="wrap">
		<div id="shopping">
			<div class="shadow">
				<em class="corner lb"></em>
				<em class="corner rt"></em>
				<div class="box">
					<div class="msg">
						<p>支付完成</p>
						<form id="form" name="form" action="order?actionName=save" method="post">
						<input type="hidden" id="ebuyid" name="ebuyid" value=<%=ebuyid%>>
						<input type="submit"  value="确认"/>
						</form>>
					</div>
				</div>
			</div>
		</div>
	</div>
		<div class="clear"></div>
	</div>
	<div id="footer">
		<jsp:include page="common/footer.jsp" />
	</div>
</body>
</html>