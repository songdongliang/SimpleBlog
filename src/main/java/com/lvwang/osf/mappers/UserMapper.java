package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.model.User;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> getUsers(int count);
}
