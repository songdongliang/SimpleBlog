package com.lvwang.osf.service;

import com.lvwang.osf.mappers.FollowingMapper;
import com.lvwang.osf.model.Following;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Transactional
@Service
public class FollowingService extends BaseService<Following> {

    @Resource
    private FollowingMapper followingMapper;

}
