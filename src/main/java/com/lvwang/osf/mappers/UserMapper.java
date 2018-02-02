package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> getUsers(int count);

    String getPasswordByEmail(String email);

    void updatePasswordByEmail(@Param("password") String password,@Param("email") String email);

    void updateResetPwdKey(@Param("key") String key,@Param("email") String email);

    String getResetPwdKey(String email);

}
