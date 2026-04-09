package com.gametech.platform.modules.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.engineer.entity.EngineerProfile;
import com.gametech.platform.modules.engineer.mapper.EngineerProfileMapper;
import com.gametech.platform.modules.resource.entity.Resource;
import com.gametech.platform.modules.resource.mapper.ResourceMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/front")
public class SearchController {

    private final ResourceMapper resourceMapper;
    private final EngineerProfileMapper engineerProfileMapper;

    public SearchController(ResourceMapper resourceMapper, EngineerProfileMapper engineerProfileMapper) {
        this.resourceMapper = resourceMapper;
        this.engineerProfileMapper = engineerProfileMapper;
    }

    @GetMapping("/search")
    public ApiResponse<Map<String, Object>> search(@RequestParam String keyword) {
        String value = keyword == null ? "" : keyword.trim();
        Map<String, Object> data = new HashMap<>();
        if (value.isEmpty()) {
            data.put("resources", java.util.Collections.emptyList());
            data.put("engineers", java.util.Collections.emptyList());
            return ApiResponse.success(data);
        }
        List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getStatus, "published")
                .and(wrapper -> wrapper.like(Resource::getTitle, value).or().like(Resource::getDescription, value))
                .orderByDesc(Resource::getDownloadCount)
                .last("limit 8"));
        List<EngineerProfile> engineers = engineerProfileMapper.selectList(new LambdaQueryWrapper<EngineerProfile>()
                .eq(EngineerProfile::getVerificationStatus, "approved")
                .and(wrapper -> wrapper.like(EngineerProfile::getRealName, value)
                        .or().like(EngineerProfile::getSkills, value)
                        .or().like(EngineerProfile::getServiceTags, value))
                .orderByDesc(EngineerProfile::getId)
                .last("limit 8"));
        data.put("resources", resources);
        data.put("engineers", engineers);
        return ApiResponse.success(data);
    }
}
