package com.gametech.platform.modules.engineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.modules.engineer.entity.EngineerProfile;
import com.gametech.platform.modules.engineer.mapper.EngineerProfileMapper;
import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;
import com.gametech.platform.modules.engineer.service.EngineerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EngineerServiceImpl implements EngineerService {

    private final EngineerProfileMapper engineerProfileMapper;

    public EngineerServiceImpl(EngineerProfileMapper engineerProfileMapper) {
        this.engineerProfileMapper = engineerProfileMapper;
    }

    @Override
    public Long apply(EngineerApplyRequest request) {
        EngineerProfile profile = new EngineerProfile();
        profile.setUserId(1L);
        profile.setRealName(request.getRealName());
        profile.setSkills(request.getSkills());
        profile.setServiceTags(request.getServiceTags());
        profile.setIntro(request.getIntro());
        profile.setVerifyStatus("pending");
        profile.setOnlineStatus("offline");
        profile.setRating(java.math.BigDecimal.ZERO);
        profile.setCompletedOrderCount(0);
        engineerProfileMapper.insert(profile);
        return profile.getId();
    }

    @Override
    public List<EngineerProfileResponse> listApproved() {
        List<EngineerProfile> profiles = engineerProfileMapper.selectList(
                new LambdaQueryWrapper<EngineerProfile>()
                        .eq(EngineerProfile::getVerifyStatus, "approved")
                        .orderByDesc(EngineerProfile::getId)
        );
        return convert(profiles);
    }

    @Override
    public List<EngineerProfileResponse> listAll() {
        List<EngineerProfile> profiles = engineerProfileMapper.selectList(
                new LambdaQueryWrapper<EngineerProfile>().orderByDesc(EngineerProfile::getId)
        );
        return convert(profiles);
    }

    private List<EngineerProfileResponse> convert(List<EngineerProfile> profiles) {
        List<EngineerProfileResponse> responses = new ArrayList<>();
        for (EngineerProfile profile : profiles) {
            EngineerProfileResponse response = new EngineerProfileResponse();
            response.setId(profile.getId());
            response.setUserId(profile.getUserId());
            response.setRealName(profile.getRealName());
            response.setSkills(profile.getSkills());
            response.setServiceTags(profile.getServiceTags());
            response.setIntro(profile.getIntro());
            response.setVerifyStatus(profile.getVerifyStatus());
            response.setOnlineStatus(profile.getOnlineStatus());
            response.setRating(profile.getRating() == null ? 0D : profile.getRating().doubleValue());
            response.setCompletedOrderCount(profile.getCompletedOrderCount() == null ? 0 : profile.getCompletedOrderCount());
            responses.add(response);
        }
        return responses;
    }
}
