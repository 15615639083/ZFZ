package com.gametech.platform.modules.resource.service;

import com.gametech.platform.modules.resource.dto.CreateResourceRequest;
import com.gametech.platform.modules.resource.dto.DownloadResourceResponse;
import com.gametech.platform.modules.resource.dto.ResourceDownloadLogResponse;
import com.gametech.platform.modules.resource.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {

    List<ResourceResponse> listPublished();

    List<ResourceResponse> listAll();

    ResourceResponse detail(Long id);

    DownloadResourceResponse download(Long id);

    ResourceResponse create(CreateResourceRequest request);

    ResourceResponse update(Long id, CreateResourceRequest request);

    List<ResourceDownloadLogResponse> listDownloadLogs();

    List<ResourceResponse> listCurrentUserDownloads();

    List<ResourceResponse> relatedResources(Long resourceId);
}
