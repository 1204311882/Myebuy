<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<html>
<head>
<meta charset="UTF-8">
<title>商品</title>
</head>
<body>
<!-- 盒模型   -->
<link rel="stylesheet" type="text/css" href="css/style.css">
<div id="header" class="wrap">
<!-- 动态导入 -->
	<jsp:include page="common/top.jsp" />
</div>
<div id="main" class="wrap">
	<div class="lefter">
		<jsp:include page="common/left.jsp"/>
	</div>
	<!-- 这个是今日特价的 start -->
	<div class="main">
		<div class="price-off">
		<h2>今日特价</h2>
		<ul class="product clearfix">
			<c:forEach items ="${special}" var="spProduct">
				<li>
					<dl>
						<dt>
							<a href="product?actionName=lookproduct&productId=${spProduct.id}"
								><img src="${spProduct.proPic }"/>
							</a>
						</dt>
						<dd class="title">
							<a  href="product?actionName=lookproduct&productId=${spProduct.id}"
							  >${spProduct.name} </a>
							 </a>
						</dd>
					<dd class="price">￥${spProduct.price}</dd>
					</dl>
				</li>
			</c:forEach>
		</ul>
		</div>
		<!-- 这个是今日特价的 end -->
		<div class="side">
			<div class="news-list">
				<h4>最新公告 </h4>
				<ul>
					<c:forEach items="${noticeList}" var="notice">
						<li>
							<a href="notice?noticeId=${notice.id }">${notice.title}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="spacer"></div>
			<div class="news-list">
				<h4>新闻动态 </h4>
				<ul>
					<c:forEach items="${newsList}" var="news">
						<li>
							<a href="news?newsId=${news.id }">${news.title}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="news-list">
				<h4>第三模态框 </h4>
				<ul>
					<c:forEach items="${newsList}" var="news">
						<li>
							<a href="news?newsId=${news.id }">${news.title}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="spacer clear"></div>
	</div>
	
	<div class="clear"></div>
</div>
<!--热卖推荐  -->
	<div class="ma2" style="margin-top: 10px;margin-left: 18%; width:960px;">
		<div class="hot">
			<h2>特卖推荐</h2>
			<div id="dv1" style="padding-left: 30px;">
				<ul class="product clearfix">
					<c:forEach items="${hotType}" var="hProduct">
						<li>
							<dl>
								<dt>
									<a href="product?actionName=lookproduct&productId=${hProduct.id}" >
									<img  src="${hProduct.proPic}"/></a>
								</dt>
								<dd class="title">
									<a href="product?actionName=lookproduct&productId=${hProduct.id}">
									${hProduct.name}</a>
								</dd>
								<dd class="price">￥${hProduct.price}</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>	
<!-- footer -->
<div id="footer">
	<jsp:include page="common/footer.jsp" />
</div>
</body>
</html>