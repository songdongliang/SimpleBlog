package com.lvwang.osf.service;

import com.lvwang.osf.mappers.FollowerMapper;
import com.lvwang.osf.pojo.Follower;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class FollowerService extends BaseService<Follower> {

    @Resource
    private FollowerMapper followerMapper;

}
