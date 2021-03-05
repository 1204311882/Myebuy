package com.mage.service;

import com.mage.dao.UserDao;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.util.MD5Util;
import com.mage.util.StringUtil;

public class UserService {
	//创建UserDao对象,提升作用域
	private UserDao userDao = new UserDao();
	public ResultInfo<User> login(String uname, String upwd) {
		ResultInfo<User> result=null;
		//创建回显对象
		User u = new User();
		u.setUserName(uname);
		u.setPassword(upwd);
		//判断非空
		if(StringUtil.isEmpty(uname)||StringUtil.isEmpty(upwd)) {
			//为空返回提示
			result=new ResultInfo<User>(0,"用户名或密码不能为空！",u);
			return result;
		}
		//判断用户名是否存在,用dao查询用户名
		User user = userDao.queryUserByName(uname);
		if(user==null) {
			result=new ResultInfo<User>(0,"用户名不存在",u);
			return result;
		}
		//判断前台传过来的密码是否与数据库里的一致
		if(!upwd.equals(user.getPassword())) {
			result=new ResultInfo<User>(0, "用户名或密码错误！", u);
			return result;
		}
		//一致返回结果和信息
		result=new ResultInfo<User>(1, "登录成功", user);
		return result;
	}
	/**
	 * 注册
	 * @param adduser
	 * @return
	 */
	public ResultInfo<User> register(User adduser) {
		ResultInfo<User> registresult=null;
		//判断是否为空
		if(adduser==null) {
			//为空，注册失败
			registresult =new ResultInfo(0,"注册失败",adduser);
			return registresult;
		}
		//调用dao层的方法接收查询结果
		int row=userDao.register(adduser);
		if(row>0) {
			registresult =new ResultInfo(1,"注册成功",adduser);
			return registresult;
		}else {
			registresult =new ResultInfo(0,"注册失败",adduser);
			return registresult;
		}
	}
	public ResultInfo<User> update(User upuser) {
		ResultInfo<User> updateresult=null;
		//调用dao层的方法接收修改结果
		int row=userDao.updateuser(upuser);
		//判断数据库是否修改成功
		if(row>0) {
			//成功
			updateresult =new ResultInfo(1,"修改成功！！！",upuser);
			return updateresult;
		}else {
			//失败
			updateresult =new ResultInfo(0,"修改失败！！！",null);
			return updateresult;
		}

	}
}


