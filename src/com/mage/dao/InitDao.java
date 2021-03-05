package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.BigType;
import com.mage.po.Product;
import com.mage.po.SmallType;
import com.mage.po.Tag;
import com.mage.util.DBUtil;

public class InitDao {

	//获取大类型分类
	public List<BigType> querybigType() {
		//查询数据库
		Connection conn=null;
		PreparedStatement sta=null;
		ResultSet res=null;
		List<BigType> bigTypeList = new ArrayList<>();
		try {
			conn=DBUtil.getConnection();
			String sql="select * from t_bigtype";
			sta=conn.prepareStatement(sql);
			//执行编译，获取结果集
			res=sta.executeQuery();
			while(res.next()) {
				BigType bType=new BigType();
				bType.setId(res.getInt("id"));
				bType.setName(res.getString("name"));
				bType.setRemarks(res.getString("remarks"));
				int uid=res.getInt("id");
				bType.setSmallTypeList(querysamllType(uid));
				bigTypeList.add(bType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bigTypeList;
	}
	//根据大类型id获取小类型分类
	public static List<SmallType> querysamllType(int uid) {
		// 查询数据库
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		List<SmallType> smallTypeList = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from t_smalltype where bigTypeId = ?";
			sta = conn.prepareStatement(sql);
			sta.setInt(1, uid);
			// 执行编译，获取结果集
			res = sta.executeQuery();
			while (res.next()) {
				SmallType smallType = new SmallType();
				smallType.setId(res.getInt("id"));
				smallType.setName(res.getString("name"));
				smallType.setRemarks(res.getString("remarks"));
				smallType.setBigTypeId(res.getInt("bigTypeId"));
				smallTypeList.add(smallType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smallTypeList;
	}
	//通过specialPrice查询特价
	public List<Product> queryspecial() {
		// 查询数据库
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		List<Product> specialList = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from t_product where specialPrice = ?";
			sta = conn.prepareStatement(sql);
			sta.setInt(1, 1);
			// 执行编译，获取结果集
			res = sta.executeQuery();
			while (res.next()) {
				Product special = new Product();
				special.setId(res.getInt("id"));
				special.setName(res.getString("name"));
				special.setDescription(res.getString("description"));
				special.setHot(res.getInt("hot"));
				special.setHotTime(res.getDate("hotTime"));
				special.setPrice(res.getInt("price"));
				special.setProPic(res.getString("proPic"));
				special.setSpecialPrice(res.getInt("specialPrice"));
				special.setSpecialPriceTime(res.getDate("specialPriceTime"));
				special.setStock(res.getInt("stock"));
				special.setBigTypeid(res.getInt("bigTypeId"));
				special.setSmallTypeid(res.getInt("smallTypeId"));
				specialList.add(special);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return specialList;
	}
	
	public List<Product> queryhotType() {
		// 查询数据库
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		List<Product> hotTypeList = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from t_product where hot = ?";
			sta = conn.prepareStatement(sql);
			sta.setInt(1, 1);
			// 执行编译，获取结果集
			res = sta.executeQuery();
			while (res.next()) {
				Product hotType = new Product();
				hotType.setId(res.getInt("id"));
				hotType.setName(res.getString("name"));
				hotType.setDescription(res.getString("description"));
				hotType.setHot(res.getInt("hot"));
				hotType.setHotTime(res.getDate("hotTime"));
				hotType.setPrice(res.getInt("price"));
				hotType.setProPic(res.getString("proPic"));
				hotType.setSpecialPrice(res.getInt("specialPrice"));
				hotType.setSpecialPriceTime(res.getDate("specialPriceTime"));
				hotType.setStock(res.getInt("stock"));
				hotType.setBigTypeid(res.getInt("bigTypeId"));
				hotType.setSmallTypeid(res.getInt("smallTypeId"));
				hotTypeList.add(hotType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hotTypeList;
	}
	//获取标签栏数据	
	public List<Tag> queryTag() {
		
		//查询数据库
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		List<Tag> tagList = new ArrayList<>();
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from t_tag";
			sta = conn.prepareStatement(sql);
			res = sta.executeQuery();
			
			while(res.next()) {
				Tag tag = new Tag();
				tag.setId(res.getInt("id"));
				tag.setName(res.getString("name"));
				tag.setUrl(res.getString("url"));
				tagList.add(tag);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// 关闭连接
			DBUtil.close(res, sta, conn);
		}
		return tagList;
	}
}
