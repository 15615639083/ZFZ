package com.gametech.platform.modules.banner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.modules.banner.dto.BannerRequest;
import com.gametech.platform.modules.banner.dto.BannerResponse;
import com.gametech.platform.modules.banner.entity.Banner;
import com.gametech.platform.modules.banner.mapper.BannerMapper;
import com.gametech.platform.modules.banner.service.BannerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerMapper bannerMapper;
    public BannerServiceImpl(BannerMapper bannerMapper) { this.bannerMapper = bannerMapper; }
    @Override
    public List<BannerResponse> listAll() {
        return convert(bannerMapper.selectList(new LambdaQueryWrapper<Banner>().orderByAsc(Banner::getSortOrder).orderByDesc(Banner::getId)));
    }
    @Override
    public List<BannerResponse> listActive() {
        return convert(bannerMapper.selectList(new LambdaQueryWrapper<Banner>().eq(Banner::getStatus, "active").orderByAsc(Banner::getSortOrder).orderByDesc(Banner::getId)));
    }
    @Override
    public BannerResponse create(BannerRequest request) {
        Banner banner = new Banner();
        fill(banner, request);
        banner.setCreatedAt(LocalDateTime.now());
        banner.setUpdatedAt(LocalDateTime.now());
        bannerMapper.insert(banner);
        return convert(banner);
    }
    @Override
    public BannerResponse update(Long id, BannerRequest request) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) throw new BusinessException("banner not found");
        fill(banner, request);
        banner.setUpdatedAt(LocalDateTime.now());
        bannerMapper.updateById(banner);
        return convert(banner);
    }
    private void fill(Banner banner, BannerRequest request) {
        banner.setBadgeText(request.getBadgeText());
        banner.setTitle(request.getTitle());
        banner.setSubtitle(request.getSubtitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setPath(request.getPath());
        banner.setButtonText(request.getButtonText());
        banner.setButtonPath(request.getButtonPath());
        banner.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        banner.setStatus(request.getStatus() == null || request.getStatus().trim().isEmpty() ? "active" : request.getStatus().trim());
    }
    private List<BannerResponse> convert(List<Banner> banners) {
        List<BannerResponse> responses = new ArrayList<>();
        for (Banner banner : banners) responses.add(convert(banner));
        return responses;
    }
    private BannerResponse convert(Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setBadgeText(banner.getBadgeText());
        response.setTitle(banner.getTitle());
        response.setSubtitle(banner.getSubtitle());
        response.setImageUrl(banner.getImageUrl());
        response.setPath(banner.getPath());
        response.setButtonText(banner.getButtonText());
        response.setButtonPath(banner.getButtonPath());
        response.setSortOrder(banner.getSortOrder());
        response.setStatus(banner.getStatus());
        return response;
    }
}
