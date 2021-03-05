package com.mage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mage.dao.UserDao;
import com.mage.po.ShoppingCarProduct;
import com.mage.po.User;
import com.mage.service.OrderProductService;
import com.mage.service.OrderService;
import com.mage.service.ShoppingCarService;

import com.mage.util.RSAEncrypt;
import com.mage.util.SHADigest;

/**
 * 购物车管理
 */
@WebServlet("/shopping")
public class ShoppingServlet extends HttpServlet {
	
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
	public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==";
	//public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMCeMBnPdzMlfP6vfUTSVFjJvQXPvbm4ha2FBbfX/qrkWYuDZgiO6ZOdJwh518VnZbefAZwXubLu2DlP4ZSQMB62Fsh9rJcVX+XDJaZyzSC6E3mHZ76h7MRv1U8wFNbSYOFPnK6CnEvKqmCMI3RTQQx6wpQMw71aFycZxv9g3fsPAgMBAAECgYAeLyvef5JwY2mJB7sbs9If7809Qea7Wd0o6fWRb7mq0gnuQPMWWD7mMPRS8bUMbD2WXsaDzXv+PEuJ/6ib/VRs1eXUerp9dRXepDbITkLO9zYEHx7r7lbOt+PcB0JfWrtpigyUgt8xgM53vYTDklFdbEFDvCZhzQZcrOqCWiC6QQJBAOwX/USl3QCeP9YuWUruY+ZRHPVRrjGy8ZLIL6LmMF/xreGJSNKKO1VYCGaAqbovhB2cUtWFLSMvJ0Bq0+QvjZ8CQQDQ28l2xWngbTPO0ZlOeqZxOS4g14VqBownODL3dfnTO0vSbcS5GftR+dcGkHfDeImR4LVwn5SRCcIMVMKHUryRAkAzX+D/8CvQN59ihwFtOcTJWHs5ssV6ERtaOm+LPhXmutuAiXQsRGI25wok6/iDCFXyb5Li3bD9yAP9k4F6VRK3AkEAodRq+dhk/TwgFHRbcW+fX8OKdnAIM+r21by54PxMotsjGN1svG/hR9dqQO/XnGHdVmbir2021FrXPG7Ae9IjwQJBALH+rAlspHzyq4lsH8VyppyZV2ZNFezq+F9KzKrcz2fvvKTbSvgTZfhcYeaPpmXKdSjVgVU6vQEqCLxpgcQbmTg=";
	
	
	private static final long serialVersionUID = 1L;
	private ShoppingCarService shoppingCarService = new ShoppingCarService();
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取用户行为
		String actionName = request.getParameter("actionName");
		//判断行为
		//查询用户购物车信息
		if("showShopping".equals(actionName)) {
			showShopping(request,response);
			return;
		}else if("findShoppingListByUid".equals(actionName)){
			findShoppingListByUid(request,response);
			return;
		}else if("addNum".equals(actionName)) {
			addNum(request,response);
			return;
		}else if("minNum".equals(actionName)) {
			minNum(request,response);
			return;
		}else if("removeProduct".equals(actionName)) {
			removeProduct(request,response);
			return;
		}else if("addShopping".equals(actionName)) {
			addShopping(request,response);
			return;
		}else if("buy".equals(actionName)) {
			buyProduct(request,response);
			return;
		}
	}


	/**
	 * 
	 * 购买
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void buyProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取产品id
		String productId = request.getParameter("productId");
		//获取用户
		User user = (User)request.getSession().getAttribute("user");
		//创建订单service
		OrderService orderService = new OrderService();
		OrderProductService orderProductService = new OrderProductService();
		//调用订单
		int row = orderService.addOrderByProduct(productId,user);
		response.getWriter().write(row+"");
	}


	/**
	 * 
	 * 加入购物车
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void addShopping(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取产品id
		String productId = request.getParameter("productId");
		//获取用户id
		User user = (User)request.getSession().getAttribute("user");
		int row = shoppingCarService.addShopping(productId,user);
		//刷新购物车中数据
		findShoppingListByUid(request,response);
		response.getWriter().write(row+"");
	}


	/**
	 * 显示购物车页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showShopping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//跳转融合页面
		String mainPage = "shopping/shopping.jsp";
		
		UserDao dao = new UserDao();
		User user = (User)(request.getSession().getAttribute("user"));
		String id = user.getBankid();
		String enc_sign_id = "";
		try {
			enc_sign_id = RSAEncrypt.encryptByPrivateKey(privateKey, SHADigest.getDigest(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getSession().setAttribute("enc_sign_id", enc_sign_id);
		/*System.out.println(enc_sign_id);
		System.out.println(SHADigest.getDigest(id));
		try {
			System.out.println(RSAEncrypt.decryptByPublicKey(publicKey, enc_sign_id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		//存储信息
		request.setAttribute("mainPage", mainPage);
		//请求转发
		request.getRequestDispatcher("/shoppingMain.jsp").forward(request, response);
		
	}


	/**
	 * 删除商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void removeProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取参数
		String productId = request.getParameter("productId");
		//调用service层方法
		int row = shoppingCarService.removeProduct(productId);
		
		//刷新购物车中数据
		findShoppingListByUid(request,response);
		//响应受影响行数
		response.getWriter().write(row+"");

		
	}


	/**
	 * 减少数量
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	private void minNum(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//获取参数
		String productId = request.getParameter("productId");
		//调用service层方法
		int row = shoppingCarService.minNum(productId);
		findShoppingListByUid(request,response);
		response.getWriter().write(row+"");
		
	}


	/**
	 * 增加数量
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void addNum(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取参数
		String productId = request.getParameter("productId");
		//调用service层方法
		int row = shoppingCarService.addNum(productId);
		findShoppingListByUid(request,response);
		response.getWriter().write(row+"");
	}


	/**
	 * 根据用户id查询购物车的中的产品 存入session域对象中
	 * @param request
	 * @param response
	 */
	private void findShoppingListByUid(HttpServletRequest request, HttpServletResponse response) {
		  
		  // 获取用户id
		  User user = (User)request.getSession().getAttribute("user");
		  int userId = user.getId();
		  //调用service层 返回购物车中的产品信息列表 
		  List<ShoppingCarProduct> shoppingCarProductList = shoppingCarService.findShoppingListByUid(userId);
		  /*for(int i = 0; i < shoppingCarProductList.size(); i++) {
			  System.out.println(shoppingCarProductList.get(i).getId());
			  System.out.println(shoppingCarProductList.get(i).getProductId());
			  System.out.println(shoppingCarProductList.get(i).getNum());
			  System.out.println();
		  }*/
		  //将数据存到session域对象中
		  HttpSession session = request.getSession();
		  session.setAttribute("shoppingCarProductList", shoppingCarProductList);
		 
	
	}

}
