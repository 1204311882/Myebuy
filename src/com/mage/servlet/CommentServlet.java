package com.mage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mage.po.Comment;
import com.mage.po.User;
import com.mage.service.CommentService;
import com.mage.util.PageUtil;
import com.mage.util.StringUtil;
/**
 * 	留言模块Servlet层
 * @author Administrator
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentService commentService = new CommentService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接受参数
		String actionName = request.getParameter("oper");
		// 判断用户行为
		if ("list".equals(actionName)&&!"".equals(actionName)) {
			// 分页显示留言信息
			queryComment(request,response);
		}else if("save".equals(actionName)&&!"".equals(actionName)) {
			// 添加留言
			addComment(request,response);
		}
	}
	/**
	 * 	添加留言信息
	 * @param request
	 * @param response
	 */
	private void addComment(HttpServletRequest request, HttpServletResponse response) {
		// 获取Session中的user对象
		User user =(User)request.getSession().getAttribute("user");
		String nickName = user.getUserName();
		String content = request.getParameter("content");
		// 调用service层方法将数据添加进入数据库
		commentService.addComment(nickName,content);
		// 刷新留言信息
		queryComment(request,response);
	}
	/**
	 * 	分页显示所有的留言信息
	 * @param request
	 * @param response
	 */
	private void queryComment(HttpServletRequest request, HttpServletResponse response) {
		String page = request.getParameter("page");
		if(StringUtil.isEmpty(page)) {
			page = "1";
		}
		try {
			// 调用service层方法,获取数据集合
			List<Comment> commentList = commentService.queryListByPage(Integer.parseInt(page), 4);
			// 调用service层方法,获取数据总数
			int totalNum = commentService.queryAllCount();
			// 生成分页代码，4条一页
			String pageCode = PageUtil.getPagingByPageSize(request.getContextPath()+"/comment?oper=list", totalNum, Integer.parseInt(page), 4);
			
			// 将结果存入域对象中
			request.setAttribute("commentList", commentList);
			request.setAttribute("pageCode", pageCode);
			// 请求转发common.jsp页面
			request.getRequestDispatcher("/comment.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
