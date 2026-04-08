package com.gametech.platform.modules.resource.service;

import com.gametech.platform.modules.resource.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {

    List<ResourceResponse> list();

    ResourceResponse detail(Long id);
}
