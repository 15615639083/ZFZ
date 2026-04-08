package com.gametech.platform.modules.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.modules.dashboard.dto.AdminDashboardOverviewResponse;
import com.gametech.platform.modules.dashboard.service.DashboardService;
import com.gametech.platform.modules.engineer.entity.EngineerProfile;
import com.gametech.platform.modules.engineer.mapper.EngineerProfileMapper;
import com.gametech.platform.modules.resource.entity.Resource;
import com.gametech.platform.modules.resource.mapper.ResourceMapper;
import com.gametech.platform.modules.serviceorder.entity.ServiceOrder;
import com.gametech.platform.modules.serviceorder.mapper.ServiceOrderMapper;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final EngineerProfileMapper engineerProfileMapper;
    private final ResourceMapper resourceMapper;
    private final ServiceOrderMapper serviceOrderMapper;

    public DashboardServiceImpl(UserMapper userMapper, EngineerProfileMapper engineerProfileMapper,
                                ResourceMapper resourceMapper, ServiceOrderMapper serviceOrderMapper) {
        this.userMapper = userMapper;
        this.engineerProfileMapper = engineerProfileMapper;
        this.resourceMapper = resourceMapper;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    @Override
    public AdminDashboardOverviewResponse overview() {
        AdminDashboardOverviewResponse response = new AdminDashboardOverviewResponse();
        response.setTotalUsers(userMapper.selectCount(new LambdaQueryWrapper<User>()));
        response.setTotalEngineers(engineerProfileMapper.selectCount(new LambdaQueryWrapper<EngineerProfile>()
                .eq(EngineerProfile::getVerificationStatus, "approved")));
        response.setPendingEngineerReviews(engineerProfileMapper.selectCount(new LambdaQueryWrapper<EngineerProfile>()
                .eq(EngineerProfile::getVerificationStatus, "pending")));
        response.setTotalResources(resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()));
        response.setTotalOrders(serviceOrderMapper.selectCount(new LambdaQueryWrapper<ServiceOrder>()));
        response.setPendingDispatchOrders(serviceOrderMapper.selectCount(new LambdaQueryWrapper<ServiceOrder>()
                .eq(ServiceOrder::getStatus, "pending_dispatch")));
        return response;
    }
}
