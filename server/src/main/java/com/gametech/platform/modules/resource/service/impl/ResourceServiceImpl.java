package com.gametech.platform.modules.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.resource.dto.CreateResourceRequest;
import com.gametech.platform.modules.resource.dto.DownloadResourceResponse;
import com.gametech.platform.modules.resource.dto.ResourceResponse;
import com.gametech.platform.modules.resource.entity.Resource;
import com.gametech.platform.modules.resource.entity.ResourceDownloadLog;
import com.gametech.platform.modules.resource.mapper.ResourceDownloadLogMapper;
import com.gametech.platform.modules.resource.mapper.ResourceMapper;
import com.gametech.platform.modules.resource.service.ResourceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final ResourceDownloadLogMapper resourceDownloadLogMapper;
    private final OperatorContext operatorContext;

    public ResourceServiceImpl(ResourceMapper resourceMapper, ResourceDownloadLogMapper resourceDownloadLogMapper,
                               OperatorContext operatorContext) {
        this.resourceMapper = resourceMapper;
        this.resourceDownloadLogMapper = resourceDownloadLogMapper;
        this.operatorContext = operatorContext;
    }

    @Override
    public List<ResourceResponse> listPublished() {
        return convert(resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getStatus, "published")
                .orderByDesc(Resource::getId)));
    }

    @Override
    public List<ResourceResponse> listAll() {
        return convert(resourceMapper.selectList(new LambdaQueryWrapper<Resource>().orderByDesc(Resource::getId)));
    }

    @Override
    public ResourceResponse detail(Long id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource == null) {
            throw new BusinessException("resource not found");
        }
        return convert(resource);
    }

    @Override
    public DownloadResourceResponse download(Long id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource == null || !"published".equalsIgnoreCase(resource.getStatus())) {
            throw new BusinessException("resource not found");
        }
        resource.setDownloadCount(resource.getDownloadCount() == null ? 1 : resource.getDownloadCount() + 1);
        resource.setUpdatedAt(LocalDateTime.now());
        resourceMapper.updateById(resource);

        ResourceDownloadLog log = new ResourceDownloadLog();
        log.setResourceId(resource.getId());
        log.setUserId(operatorContext.getUserId());
        log.setDownloadIp(operatorContext.getRequestIp());
        log.setCreatedAt(LocalDateTime.now());
        resourceDownloadLogMapper.insert(log);

        DownloadResourceResponse response = new DownloadResourceResponse();
        response.setResourceId(resource.getId());
        response.setDownloadCount(resource.getDownloadCount() == null ? 0 : resource.getDownloadCount());
        return response;
    }

    @Override
    public ResourceResponse create(CreateResourceRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Resource resource = new Resource();
        resource.setTitle(request.getTitle());
        resource.setCategoryName(request.getCategoryName());
        resource.setDescription(request.getDescription());
        resource.setCoverUrl(request.getCoverUrl());
        resource.setFileUrl(request.getFileUrl());
        resource.setFileHash(request.getFileHash());
        resource.setPrice(request.getPrice() == null ? BigDecimal.ZERO : request.getPrice());
        resource.setIsFree(Boolean.TRUE.equals(request.getFree()) || BigDecimal.ZERO.compareTo(resource.getPrice()) == 0);
        resource.setStatus(blankToDefault(request.getStatus(), "published"));
        resource.setDownloadCount(0);
        resource.setUploaderId(operatorContext.getUserId());
        resource.setCreatedAt(now);
        resource.setUpdatedAt(now);
        resourceMapper.insert(resource);
        return convert(resource);
    }

    private List<ResourceResponse> convert(List<Resource> resources) {
        List<ResourceResponse> responses = new ArrayList<>();
        for (Resource resource : resources) {
            responses.add(convert(resource));
        }
        return responses;
    }

    private ResourceResponse convert(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setTitle(resource.getTitle());
        response.setDescription(resource.getDescription());
        response.setCategoryName(resource.getCategoryName());
        response.setCoverUrl(resource.getCoverUrl());
        response.setFileUrl(resource.getFileUrl());
        response.setFileHash(resource.getFileHash());
        response.setPrice(resource.getPrice());
        response.setFree(Boolean.TRUE.equals(resource.getIsFree()));
        response.setStatus(resource.getStatus());
        response.setDownloadCount(resource.getDownloadCount() == null ? 0 : resource.getDownloadCount());
        return response;
    }

    private String blankToDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
