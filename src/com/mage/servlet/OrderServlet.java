package com.mage.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mage.dao.ShoppingCarDao;
import com.mage.po.Order;
import com.mage.po.OrderProduct;
import com.mage.po.ShoppingCarProduct;
import com.mage.po.User;
import com.mage.service.OrderProductService;
import com.mage.service.OrderService;
import com.mage.service.ProductService;
import com.mage.service.ShoppingCarService;
import com.mage.util.DateUtil;

/**
 * 订单管理
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
	
	public static final String PEM_PUBLICKEY = "PUBLIC KEY";
    public static final String PEM_PRIVATEKEY = "PRIVATE KEY";
	
	private static final long serialVersionUID = 1L;

	private ProductService productService = new ProductService();
	private OrderService orderService = new OrderService();
	private OrderProductService orderProductService = new OrderProductService();
	private ShoppingCarService shoppingCarService = new ShoppingCarService();
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取用户行为
		String actionName = request.getParameter("actionName");
		//判断分流
		if("save".equals(actionName)) {
			//提交订单
			subOrder(request,response);
			return;
		}else if("list".equals(actionName)) {
			//展示订单
			findOrderList(request,response);
			return;
		}
	}


	/**
	 * 查询订单数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void findOrderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取session
		HttpSession session = request.getSession();
		
		//获取用户
		User user = (User)session.getAttribute("user");
		//查询order
		List<Order> orderListOld = orderService.findOrderList(user.getId());
		List<Order> orderList = new ArrayList<Order>();
		List<OrderProduct> orderProductList = new ArrayList<OrderProduct>();
		for (Order order : orderListOld) {
			//根据订单id查询该订单中的商品信息
			orderProductList = orderProductService.findOrderProductList(order);
			//订单对象添加商品集合
			order.setOrderProductList(orderProductList);
			orderList.add(order);
		}
		//存放作用域
		request.setAttribute("orderList", orderList);
		String mainPage = "userCenter/orderList.jsp";
		request.setAttribute("mainPage", mainPage);
		request.setAttribute("orderList", orderList);
		// 请求转发
		request.getRequestDispatcher("/userCenter.jsp").forward(request, response);
		
	}


	/**
	 * 提交订单
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void subOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		/*String id = request.getParameter("id");
		String price = request.getParameter("price");
		String enc_sign_id = request.getParameter("enc_sign_id");
		//String enc_sign_price = request.getParameter("enc_sign_price");
		
		
		//System.out.println("最终enc_sign_id=" + enc_sign_id);
		//System.out.println("enc_sign_price=" + enc_sign_price);
		String pk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";;
		String sk = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==";
		
		String sign_id = null;
		//String sign_price = null;
		
		try {
			//用自己的私钥解密id
			id = com.mage.util.RSAEncrypt.decryptByPrivateKey(sk, id);
			price = com.mage.util.RSAEncrypt.decryptByPrivateKey(sk, price);
            //用商城的公钥解密哈希值
            sign_id = com.mage.util.RSAEncrypt.decryptByPublicKey(pk, enc_sign_id);
            //sign_price = com.mage.util.RSAEncrypt.decryptByPublicKey(pk, enc_sign_price);
		} catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println("id=" + id);
		System.out.println("price=" + price);
		
		if (!com.mage.util.SHADigest.getDigest(id).equalsIgnoreCase(sign_id)) {
				System.out.println("id=" + id);
				System.out.println("sign_id=" + sign_id);
	            request.setAttribute("login_error", "登录失败！报文可能被损坏！请报警！");
	            request.getRequestDispatcher("/index.jsp").forward(request, response);
	            
	            return;
	        }
		System.out.println("---------------------------------------------------------------------");
		System.out.println("已确认是商场发出的信息");
		System.out.println("id和sign_id相等=" + sign_id);
		System.out.println("可以进行转账！");*/
		
		String ebuyid = request.getParameter("ebuyid");
		int eid = Integer.parseInt(ebuyid);
		
		// 获取session
		HttpSession session = request.getSession();
		
		//创建一个order对象
		Order order = new Order();
		//获取用户
		//User user = (User)session.getAttribute("user");
		//获取购物车
		
		ShoppingCarDao dao = new ShoppingCarDao();
		List<ShoppingCarProduct> shoppingCarProductList = dao.findShoppingListByUid(eid);
		
		//List<ShoppingCarProduct> shoppingCarProductList = (List<ShoppingCarProduct>) session.getAttribute("shoppingCarProductList");
		//填充数据
		//order.setUserId(user.getId());
		order.setUserId(eid);
		order.setOrderNo(DateUtil.getCurrentDateStr());
		order.setStatus(1);
		
		int sum = 0; 
		List<OrderProduct> orderProductList = new ArrayList<OrderProduct>();
		for (ShoppingCarProduct shoppingCarProduct : shoppingCarProductList) {
			OrderProduct orderProduct = new OrderProduct();
			//System.out.println(shoppingCarProduct.getNum());
			orderProduct.setNum(shoppingCarProduct.getNum());
			orderProduct.setProductId(shoppingCarProduct.getProductId());
			productService.reduceStock(shoppingCarProduct.getProductId().toString(), shoppingCarProduct.getNum());
			//计算总金额
			sum = sum + (shoppingCarProduct.getPrice())*(shoppingCarProduct.getNum());
			orderProductList.add(orderProduct);
		}
		//设置总价
		//System.out.println(sum);
		order.setCost(sum);
		//设置商品集合
		order.setOrderProductList(orderProductList);
		//状态
		boolean flag = true;
		//调用orderService增加订单数据，返回订单id 
		int orderId = orderService.subOarder(order);
		
		//设置订单商品的订单id
		List<OrderProduct> orderProductList2 = new ArrayList<OrderProduct>();
		for (OrderProduct orderProduct : orderProductList) {
			orderProduct.setOrderId(orderId);
			orderProductList2.add(orderProduct);
		}
		//调用订单商品的service 增加订单商品
		orderProductService.addOrderProduct(orderProductList2);
		
		// 清空购物车
		//shoppingCarService.removeProductAll(user.getId());
		shoppingCarService.removeProductAll(eid);
		session.removeAttribute("shoppingCarProductList");
		//成功跳转
		String mainPage = "shopping/shopping-result.jsp";
		request.setAttribute("mainPage", mainPage);
		
		request.getRequestDispatcher("/shoppingMain.jsp").forward(request, response);
		
	}
	
}
