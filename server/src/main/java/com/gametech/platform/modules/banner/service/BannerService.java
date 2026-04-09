package com.gametech.platform.modules.banner.service;

import com.gametech.platform.modules.banner.dto.BannerRequest;
import com.gametech.platform.modules.banner.dto.BannerResponse;

import java.util.List;

public interface BannerService {
    List<BannerResponse> listAll();
    List<BannerResponse> listActive();
    BannerResponse create(BannerRequest request);
    BannerResponse update(Long id, BannerRequest request);
}
