package com.newland.csc.job.impl;


import com.newland.csc.cscdata.itv.dao.ItvUserMapper;
import com.newland.csc.cscdata.itv.dao.ItvWlistMapper;
import com.newland.csc.cscdata.itv.usersync.dao.ItvUsersSyncInfoMapper;
import com.newland.csc.cscdata.itv.usersync.mode.ItvUsersSyncInfo;
import com.newland.csc.job.ItvSyncUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *itv用户同步接口
 * 1.每隔30秒去操作
 * 2.查询宽带业务主查询itv_users_sync_info根据地市分线程执行
 * 3.更新状态为处理中和插入批次号
 * 4.调用计费中心的逻辑判断插入新增这张表返回结果
 *5.根据计费中心的返回结果更新宽带业务表
 * @author 范希树
 * @date 2019/10/10
 */
@Service
public class ItvSyncUserServiceImpl implements ItvSyncUserService {
        @Resource
        ItvUsersSyncInfoMapper itvUsersSyncInfoMapper;
        @Resource
        ItvUserMapper itvUserMapper;
        @Resource
        ItvWlistMapper itvWlistMapper;
        @Value("cityIds")
        private Integer[] cityIds;
        @Value("searchBachId")
        private Long searchBachId;
        @Override
        public void itvSynusers() {
            for(int city :cityIds) {
              List<ItvUsersSyncInfo> itvUsersSyncInfos= itvUsersSyncInfoMapper.selectByCity(city,searchBachId);
             //  List<ItvUsersSyncInfo> itvUsersSyncInfos
             //  itvUsersSyncInfoMapper.updateStatus();
            }

        }


}
