package com.lvwang.osf.dao;

import java.util.List;

import com.lvwang.osf.model.User;

public interface UserDAO {
	User getUserByEmail(String email);
	User getUserByUsername(String username);
	String getPwdByEmail(String email);

	int activateUser(User user);
	void updateActivationKey(int user_id, String key);
	void updateAvatar(int user_id, String avatar);
	void updateUsernameAndDesc(int user_id, String username, String desc);
	String getRestPwdKey(String email);
	void updateResetPwdKey(String email, String key);
	void updatePassword(String email, String password);
	
	void insertToken(String token, User user);
	void delToken(String token);
	boolean containsToken(String token);
	User getUserByToken(String token);
}
