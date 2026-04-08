package com.gametech.platform.modules.engineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.engineer.entity.EngineerProfile;
import com.gametech.platform.modules.engineer.mapper.EngineerProfileMapper;
import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;
import com.gametech.platform.modules.engineer.dto.ReviewEngineerRequest;
import com.gametech.platform.modules.engineer.service.EngineerService;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EngineerServiceImpl implements EngineerService {

    private final EngineerProfileMapper engineerProfileMapper;
    private final UserMapper userMapper;
    private final OperatorContext operatorContext;

    public EngineerServiceImpl(EngineerProfileMapper engineerProfileMapper, UserMapper userMapper,
                               OperatorContext operatorContext) {
        this.engineerProfileMapper = engineerProfileMapper;
        this.userMapper = userMapper;
        this.operatorContext = operatorContext;
    }

    @Override
    public Long apply(EngineerApplyRequest request) {
        Long userId = operatorContext.getUserId();
        EngineerProfile existing = engineerProfileMapper.selectOne(new LambdaQueryWrapper<EngineerProfile>()
                .eq(EngineerProfile::getUserId, userId)
                .last("limit 1"));
        if (existing != null) {
            throw new BusinessException("engineer application already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        EngineerProfile profile = new EngineerProfile();
        profile.setUserId(userId);
        profile.setRealName(request.getRealName());
        profile.setIdCardMask(request.getIdCardMask());
        profile.setCertificates(request.getCertificates());
        profile.setSkills(request.getSkills());
        profile.setServiceTags(request.getServiceTags());
        profile.setIntro(request.getIntro());
        profile.setHourlyPrice(request.getHourlyPrice() == null ? BigDecimal.ZERO : request.getHourlyPrice());
        profile.setVerificationStatus("pending");
        profile.setVerificationRemark("");
        profile.setOnlineStatus("offline");
        profile.setRating(BigDecimal.ZERO);
        profile.setCompletedOrderCount(0);
        profile.setCreatedAt(now);
        profile.setUpdatedAt(now);
        engineerProfileMapper.insert(profile);
        return profile.getId();
    }

    @Override
    public EngineerProfileResponse current() {
        EngineerProfile profile = engineerProfileMapper.selectOne(new LambdaQueryWrapper<EngineerProfile>()
                .eq(EngineerProfile::getUserId, operatorContext.getUserId())
                .last("limit 1"));
        return profile == null ? null : convert(profile);
    }

    @Override
    public List<EngineerProfileResponse> listApproved() {
        List<EngineerProfile> profiles = engineerProfileMapper.selectList(
                new LambdaQueryWrapper<EngineerProfile>()
                        .eq(EngineerProfile::getVerificationStatus, "approved")
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

    @Override
    public List<EngineerProfileResponse> listPending() {
        List<EngineerProfile> profiles = engineerProfileMapper.selectList(
                new LambdaQueryWrapper<EngineerProfile>()
                        .eq(EngineerProfile::getVerificationStatus, "pending")
                        .orderByAsc(EngineerProfile::getId)
        );
        return convert(profiles);
    }

    @Override
    public EngineerProfileResponse review(Long engineerId, ReviewEngineerRequest request) {
        String status = request.getStatus().trim().toLowerCase();
        if (!"approved".equals(status) && !"rejected".equals(status)) {
            throw new BusinessException("status must be approved or rejected");
        }

        EngineerProfile profile = engineerProfileMapper.selectById(engineerId);
        if (profile == null) {
            throw new BusinessException("engineer application not found");
        }

        profile.setVerificationStatus(status);
        profile.setVerificationRemark(request.getRemark());
        profile.setReviewedBy(operatorContext.getUserId());
        profile.setReviewedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        engineerProfileMapper.updateById(profile);

        User user = userMapper.selectById(profile.getUserId());
        if (user != null) {
            user.setRole("approved".equals(status) ? "engineer" : "user");
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
        }
        return convert(profile);
    }

    private List<EngineerProfileResponse> convert(List<EngineerProfile> profiles) {
        List<EngineerProfileResponse> responses = new ArrayList<>();
        for (EngineerProfile profile : profiles) {
            responses.add(convert(profile));
        }
        return responses;
    }

    private EngineerProfileResponse convert(EngineerProfile profile) {
        EngineerProfileResponse response = new EngineerProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUserId());
        response.setRealName(profile.getRealName());
        response.setIdCardMask(profile.getIdCardMask());
        response.setCertificates(profile.getCertificates());
        response.setSkills(profile.getSkills());
        response.setServiceTags(profile.getServiceTags());
        response.setIntro(profile.getIntro());
        response.setHourlyPrice(profile.getHourlyPrice() == null ? 0D : profile.getHourlyPrice().doubleValue());
        response.setVerificationStatus(profile.getVerificationStatus());
        response.setVerificationRemark(profile.getVerificationRemark());
        response.setOnlineStatus(profile.getOnlineStatus());
        response.setRating(profile.getRating() == null ? 0D : profile.getRating().doubleValue());
        response.setCompletedOrderCount(profile.getCompletedOrderCount() == null ? 0 : profile.getCompletedOrderCount());
        return response;
    }
}
