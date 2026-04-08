package com.gametech.platform.modules.resource.service.impl;

import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.modules.resource.dto.ResourceResponse;
import com.gametech.platform.modules.resource.service.ResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final List<ResourceResponse> resources = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        resources.add(mock(1L, "Windows game deployment package", "Deployment docs and startup scripts.", "Deployment", new BigDecimal("19.90"), false, 128));
        resources.add(mock(2L, "Server environment checklist", "Common environment inspection sheet.", "Ops", BigDecimal.ZERO, true, 206));
    }

    @Override
    public List<ResourceResponse> list() {
        return Collections.unmodifiableList(resources);
    }

    @Override
    public ResourceResponse detail(Long id) {
        for (ResourceResponse resource : resources) {
            if (resource.getId().equals(id)) {
                return resource;
            }
        }
        throw new BusinessException("resource not found");
    }

    private ResourceResponse mock(Long id, String title, String description, String categoryName,
                                  BigDecimal price, boolean free, int downloadCount) {
        ResourceResponse response = new ResourceResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDescription(description);
        response.setCategoryName(categoryName);
        response.setPrice(price);
        response.setFree(free);
        response.setDownloadCount(downloadCount);
        return response;
    }
}
