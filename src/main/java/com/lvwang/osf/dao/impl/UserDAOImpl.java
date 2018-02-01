package com.lvwang.osf.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lvwang.osf.dao.UserDAO;
import com.lvwang.osf.model.User;

@Repository("userDao")
public class UserDAOImpl implements UserDAO{

	private static final String TABLE = "osf_users"; 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("redisTemplate")
	private HashOperations<String, String, Object> mapOps;
	

	@Override
	public String getPwdByEmail(String email) {
		String sql = "select user_pwd from " + TABLE + " where user_email=?";
		return jdbcTemplate.query(sql, new Object[]{email}, new ResultSetExtractor<String>(){

			public String extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				String password = null;
				if(rs.next()){
					password = rs.getString("user_pwd");
				}
				return password;
			}
			
		});
	}

	@Override
	public int activateUser(final User user) {
		final String sql = "update " + TABLE + " set user_status=?, user_activationKey=?"+
					 " where id=?";
		return jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, user.getUserStatus());
				ps.setString(2, user.getUserActivationKey());
				ps.setInt(3, user.getId());
				return ps;
			}
		});
		
	}

	@Override
	public void updateActivationKey(final int user_id, final String key){
		final String  sql = "update " + TABLE + " set user_activationKey=? where id=?";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps =  con.prepareStatement(sql);
				ps.setString(1, key);
				ps.setInt(2, user_id);
				return ps;
			}
		});
	}
	
	public void updateUsernameAndDesc(final int user_id, final String username, final String desc) {
		final String sql = "update " + TABLE + " set user_name=?, user_desc=? where id=?";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps =  con.prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, desc);
				ps.setInt(3, user_id);
				return ps;
			}
		});
		
		//update cahce
		User user = (User)mapOps.get("user", "user:"+user_id);
		user.setUserName(username);
		user.setUserDesc(desc);
		mapOps.put("user", "user:"+user_id, user);
		
	}

	public String getRestPwdKey(String email) {
		String sql = "select resetpwd_key from " + TABLE + " where user_email=?";
		return jdbcTemplate.query(sql, new Object[]{email}, new ResultSetExtractor<String>(){

			public String extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				String key = null;
				if(rs.next()) {
					key = rs.getString("resetpwd_key");
				}
				return key;
			}
			
		});
	}

	public void updateResetPwdKey(final String email, final String key) {
		final String sql = "update " + TABLE + " set resetpwd_key=? where user_email=?";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps =  con.prepareStatement(sql);
				ps.setString(1, key);
				ps.setString(2, email);
				return ps;
			}
		});
		
	}

	public void updatePassword(final String email, final String password) {
		final String sql = "update " + TABLE + " set user_pwd=? where user_email=?";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps =  con.prepareStatement(sql);
				ps.setString(1, password);
				ps.setString(2, email);
				return ps;
			}
		});
	}

	public void insertToken(String token, User user) {
		mapOps.put("tokens:", token, user);
	}

	public void delToken(String token) {
		mapOps.delete("tokens:", token);
	}
	
	public boolean containsToken(String token) {
		return mapOps.hasKey("tokens:", token);
	}

	public User getUserByToken(String token) {
		return (User) mapOps.get("tokens:", token);
	}
	
}
