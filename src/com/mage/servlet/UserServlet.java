package com.mage.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
//import org.apache.tomcat.util.codec.binary.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mage.po.ShoppingCarProduct;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.ShoppingCarService;
import com.mage.service.UserService;
import com.mage.util.StringUtil;
import com.mage.util.Ende;
import com.mage.util.SHADigest;
import com.mage.util.RSAEncrypt;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收参数，判断执行事务
		String actionName = request.getParameter("actionName");
		if("login".equals(actionName)) {
			//登录功能
			login(request,response);
		}else if("logout".equals(actionName)) {
			//注销
			logout(request,response);
		}else if("register".equals(actionName)) {
			//注册
			try {
				register(request,response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("userCenter".equals(actionName)){
			//展示个人中心
			userCenter(request,response);
		}else if("updateuser".equals(actionName)) {
			//修改个人信息
			try {
				updateuser(request,response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("userSave".equals(actionName)) {
			//转跳到修改个人信息页面
			userSave(request,response);
		}
	}
	/**
	 * 修改信息页面
	 * @param request
	 * @param response
	 */
	private void userSave(HttpServletRequest request, HttpServletResponse response) {
		// 页面的动态镶嵌
		String mainPage = "userCenter/userSave.jsp";
		request.setAttribute("mainPage", mainPage);
		try {
			// 请求转发
			request.getRequestDispatcher("/userCenter.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 修改个人信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws ParseException 
	 */
	private void updateuser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		// 接收参数
		String uid = request.getParameter("uid");
		String userName = request.getParameter("userName");// 用户名
		String trueName = request.getParameter("trueName");// 姓名
		String password = request.getParameter("password");// 密码
		String mobile = request.getParameter("mobile");// 电话
		String address = request.getParameter("address");// 地址
		String sex = request.getParameter("sex");// 性别
		String birthday = request.getParameter("birthday");// 生日
		String dentityCode = request.getParameter("dentityCode");// 身份证号码
		String email = request.getParameter("email");// 电子邮
		// 日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date biryhdate = sdf.parse(birthday);
		//创建对象储存参数对象
		User upuser=new User();
		upuser.setId(Integer.parseInt(uid));
		upuser.setTrueName(trueName);
		upuser.setUserName(userName);
		upuser.setPassword(password);
		upuser.setMobile(mobile);
		upuser.setAddress(address);
		upuser.setSex(sex);
		upuser.setBirthday(biryhdate);
		upuser.setDentityCode(dentityCode);
		upuser.setEmail(email);
		upuser.setStatus(1);
		
		
		// 调用service方法获取结果信息
		UserService userService = new UserService();
		ResultInfo<User> result = userService.update(upuser);
		if(result.getCode()==1) {
			//修改成功，更新session
			User user=result.getResult();
			request.getSession().setAttribute("user", user);
			//请求转发到个人中心
			request.getRequestDispatcher("/user?actionName=userCenter").forward(request, response);
		}else {
			//不更新session，
			request.setAttribute("result", result);
			request.getRequestDispatcher("/user?actionName=userSave").forward(request, response);
		}
	}
	/**
	 * 展示个人中心
	 * @param request
	 * @param response
	 */
	private void userCenter(HttpServletRequest request, HttpServletResponse response) {
		// 页面的动态镶嵌
		String mainPage = "userCenter/userInfo.jsp";
		request.setAttribute("mainPage", mainPage);
		try {
			// 请求转发
			request.getRequestDispatcher("/userCenter.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ServletException 
	 * 
	 */
	private void register(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, ServletException {
		//接收参数
		String userName = request.getParameter("userName");//用户名
		String password = request.getParameter("password");//密码
		
		String sign_username = request.getParameter("sign_username");
        String sign_password = request.getParameter("sign_password");
        PrivateKey privateKey = null;
		try {
			privateKey =
	            Ende.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0Y4V5HSA3ZiDynKX0bfVdr9HKD6zEqlWvgMzqG/bXBJr5jMep3Z8Rkc+xeX6TwGgQyg7G5maIYDhwtJXm1axVoXxY5gAAv8vQJeC9UofgElzN+TweldC43dUcwKWCnHzimY2gkdSo3aQ78cB1y/vGfVjuSgBrG5AWIKq/eg1SlAgMBAAECgYEAk/1W04wBziUCxEUm6SyMadCpHL41YOMs0aJDNXjUMlyZz8KeHBua8E6VktVBETp/ge1ut7bDj+I3mMGUZK4gwOXfyYjRuZa01f3Z89X5aLDRkjOwIm0PmTgEiAEuAIQaYfj/c6Iru+TwCjq5QZyqjLY7ASJ0muzai/0tAOo30QECQQDf6ETVqHZ7WClnoE3JT2ZmQ8CUK/yDzPVnL1Q+HSn8StaUKuZavHGbU5KgrC9dnGQudNEOGPHQGT2tEcSxM2+FAkEA2DNW6YpmxlTUvbHRytMemXkjNVpdnEma5y8osUWDq+IeWnJXmBJu4D6T5K1gwyign5HjmNksNgYK1Fquv0MKoQJAMy0ARqE5a1msJP4zqTZXnjoQEw22ql03Hb1okMXTqdFlF/pyKfz2Ll08nzKbpNaw4xlaCtHSuxB500vDXAj4jQJAclErKo+o6kPevXLxyDo7mtEHweVHTCVLR+SSsrFb/x2wCQkeseVFRUMxdiAK4wZvcBB29NIYY3Rsc36DmdQ8IQJAPSpFpwru315UT4/QCCrq8b/wVA6BbKeptG+ySmhC6cpZVvk2kqtcQekJliGRqUOP0M9EeTofCALJ/Fvm7oD9Mg==");
			//username =
            //	new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(username)), "utf-8");
            //password =
            //    new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(password)), "utf-8");
			userName = RSAEncrypt.decryptByPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==", userName);
													//"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw=="
			password = RSAEncrypt.decryptByPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==", password);
		} catch (Exception e) {
            e.printStackTrace();
        }
		if (!SHADigest.getDigest(userName).equalsIgnoreCase(sign_username)
	            || !SHADigest.getDigest(password).equalsIgnoreCase(sign_password)) {
				System.out.println("user:" + SHADigest.getDigest(userName));
				System.out.println("pass:" + SHADigest.getDigest(password));
				System.out.println("user:" + sign_username);
				System.out.println("pass:" + sign_password);
				System.out.println("");
	            request.setAttribute("login_error", "登录失败！报文可能被损坏！请报警！");
	            request.getRequestDispatcher("register.jsp").forward(request, response);
	            return;
	    }
		
		String bankid = request.getParameter("bankid");//电话
		String mobile = request.getParameter("mobile");//电话
		String address = request.getParameter("address");//地址
		String sex = request.getParameter("sex");//性别
		String birthday = request.getParameter("birthday");//生日
		String dentityCode = request.getParameter("dentityCode");//身份证号码
		String email = request.getParameter("email");//电子邮
		//日期格式
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		Date biryhdate = sdf.parse(birthday); 
		//数据返显
		
		password = SHADigest.getDigest(password);
		
		User adduser= new User();
		adduser.setUserName(userName);
		adduser.setPassword(password);
		adduser.setBankid(bankid);
		adduser.setMobile(mobile);
		adduser.setAddress(address);
		adduser.setSex(sex);
		adduser.setBirthday(biryhdate);
		adduser.setDentityCode(dentityCode);
		adduser.setEmail(email);
		//调用service方法获取结果信息
		UserService userService = new UserService();
		ResultInfo<User> registresult = userService.register(adduser);
		//判断是否注册成功
		if(registresult.getCode()==1) {
			//注册成功，转跳到登录界面
			response.sendRedirect("login.jsp");
		}else {
			//注册失败，将信息存储到作用域中
			request.setAttribute("registresult", registresult);
			//请求转发到注册界面
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
	}
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 清除Session
		request.getSession().invalidate();
		// 清除Cookie
		Cookie cookie = new Cookie("user", null);
		cookie.setMaxAge(0);
		// 重定向到登录界面
		response.addCookie(cookie);
		response.sendRedirect("login.jsp");
		
	}
	/**
	 * 登录功能
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("uname");
        String password = request.getParameter("upwd");
        String sign_username = request.getParameter("sign_username");
        String sign_password = request.getParameter("sign_password");
        PrivateKey privateKey = null;
        /*try {
			//用自己的私钥解密id
        	username = RSAEncrypt.decryptByPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0Y4V5HSA3ZiDynKX0bfVdr9HKD6zEqlWvgMzqG/bXBJr5jMep3Z8Rkc+xeX6TwGgQyg7G5maIYDhwtJXm1axVoXxY5gAAv8vQJeC9UofgElzN+TweldC43dUcwKWCnHzimY2gkdSo3aQ78cB1y/vGfVjuSgBrG5AWIKq/eg1SlAgMBAAECgYEAk/1W04wBziUCxEUm6SyMadCpHL41YOMs0aJDNXjUMlyZz8KeHBua8E6VktVBETp/ge1ut7bDj+I3mMGUZK4gwOXfyYjRuZa01f3Z89X5aLDRkjOwIm0PmTgEiAEuAIQaYfj/c6Iru+TwCjq5QZyqjLY7ASJ0muzai/0tAOo30QECQQDf6ETVqHZ7WClnoE3JT2ZmQ8CUK/yDzPVnL1Q+HSn8StaUKuZavHGbU5KgrC9dnGQudNEOGPHQGT2tEcSxM2+FAkEA2DNW6YpmxlTUvbHRytMemXkjNVpdnEma5y8osUWDq+IeWnJXmBJu4D6T5K1gwyign5HjmNksNgYK1Fquv0MKoQJAMy0ARqE5a1msJP4zqTZXnjoQEw22ql03Hb1okMXTqdFlF/pyKfz2Ll08nzKbpNaw4xlaCtHSuxB500vDXAj4jQJAclErKo+o6kPevXLxyDo7mtEHweVHTCVLR+SSsrFb/x2wCQkeseVFRUMxdiAK4wZvcBB29NIYY3Rsc36DmdQ8IQJAPSpFpwru315UT4/QCCrq8b/wVA6BbKeptG+ySmhC6cpZVvk2kqtcQekJliGRqUOP0M9EeTofCALJ/Fvm7oD9Mg==", username);
			//price = RSAEncrypt.decryptByPrivateKey(sk, price);
            //用商城的公钥解密哈希值
            //sign_id = RSAEncrypt.decryptByPublicKey(pk, enc_sign_id);
		} catch (Exception e) {
            e.printStackTrace();
        }*/
		try {
			privateKey =
	            Ende.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==");
			//username =
            	//new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.decodeBase64(username)), "utf-8");
            //password =
                //new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.decodeBase64(password)), "utf-8");
			//username =
            //	new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(username)), "utf-8");
            //password =
            //    new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(password)), "utf-8");
			username = RSAEncrypt.decryptByPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==", username);
			password = RSAEncrypt.decryptByPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==", password);
			//                                        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw=="
        } catch (Exception e) {
            e.printStackTrace();
        }
		if (!SHADigest.getDigest(username).equalsIgnoreCase(sign_username)
	            || !SHADigest.getDigest(password).equalsIgnoreCase(sign_password)) {
				System.out.println("user:" + SHADigest.getDigest(username));
				System.out.println("pass:" + SHADigest.getDigest(password));
				System.out.println("user:" + sign_username);
				System.out.println("pass:" + sign_password);
				System.out.println("");
	            request.setAttribute("login_error", "登录失败！报文可能被损坏！请报警！");
	            request.getRequestDispatcher("login.jsp").forward(request, response);
	            return;
	    }
		
		password = SHADigest.getDigest(password);
		
		//创建购物车service
		ShoppingCarService shoppingCarService = new ShoppingCarService();
		// 接收参数
		String uname = username;
		String upwd = password;
		//调用service方法获取结果信息
		UserService userService = new UserService();
		ResultInfo<User> result = userService.login(uname, upwd);
		if(result.getCode()==1) {
			//登录成功，存session，存cookie
			User user=result.getResult();
			request.getSession().setAttribute("user", user);
			
			String rem=request.getParameter("rem");
			if("1".equals(rem)) {
				String cuname=URLEncoder.encode(uname,"UTF-8");
				String cupwd=URLEncoder.encode(upwd,"gbk");
				Cookie cookie=new Cookie("user", cuname+"-"+cupwd);
				cookie.setMaxAge(7*24*60*60);
				response.addCookie(cookie);
			}
			
			//加载用户购物车数据
			//调用service层  返回购物车中的产品信息列表
			List<ShoppingCarProduct> shoppingCarProductList = shoppingCarService.findShoppingListByUid(user.getId());
			//将数据存到session域对象中
			HttpSession session = request.getSession();
			session.setAttribute("shoppingCarProductList", shoppingCarProductList);	
			request.getRequestDispatcher("index").forward(request, response);
		}else {
			//失败，将信息存储到作用域中
			request.setAttribute("result", result);
			//请求转发到登录界面
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	public static String byteToString(byte[] resouce){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < resouce.length; i++) {
    		if (i == resouce.length-1) {
    			sb.append(Byte.toString(resouce[i]));
			}else{
				sb.append(Byte.toString(resouce[i]));	
				sb.append(" ");
			}			
		}
    	return sb.toString();
    	
    }
	
	public static byte[] stringToByte(String resouce){
    	String[] strArr = resouce.split(" ");
    	int len = strArr.length;
    	byte[] clone = new byte[len];
    	for (int i = 0; i < len; i++) {
			clone[i] = Byte.parseByte(strArr[i]);
    	}
    	
    	return clone;
    	
    }
	
}
