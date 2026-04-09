package com.gametech.platform.modules.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.banner.dto.BannerResponse;
import com.gametech.platform.modules.banner.service.BannerService;
import com.gametech.platform.modules.resource.entity.Resource;
import com.gametech.platform.modules.resource.mapper.ResourceMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/front")
public class HomeController {

    private final ResourceMapper resourceMapper;
    private final BannerService bannerService;

    public HomeController(ResourceMapper resourceMapper, BannerService bannerService) {
        this.resourceMapper = resourceMapper;
        this.bannerService = bannerService;
    }

    @GetMapping("/index")
    public ApiResponse<Map<String, Object>> index() {
        Map<String, Object> data = new HashMap<>();
        data.put("logoUrl", "");
        data.put("yzfUrl", "");
        data.put("consumerHotline", "");
        data.put("telephoneServiceSwitch", "close");
        data.put("wxChatIndependent", "false");
        data.put("categoryPageConfig", "default");
        data.put("isShowCategory", "true");
        data.put("projectType", "game_resource_and_service_platform");
        List<Map<String, String>> banners = bannerFromCms();
        data.put("banner", banners);
        data.put("heroBanner", banners.isEmpty() ? defaultHeroBanner() : banners.get(0));
        data.put("platformMenus", defaultMenus());
        return ApiResponse.success(data);
    }

    private List<Map<String, String>> bannerFromResources() {
        List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getStatus, "published")
                .orderByDesc(Resource::getId)
                .last("limit 3"));
        if (resources.isEmpty()) {
            return defaultBanner();
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (Resource resource : resources) {
            list.add(bannerItem(resource.getTitle(), "/pages/resource/detail?id=" + resource.getId(),
                    resource.getCoverUrl() == null || resource.getCoverUrl().trim().isEmpty()
                            ? "https://dummyimage.com/1200x480/1f2937/ffffff&text=" + resource.getTitle().replace(" ", "+")
                            : resource.getCoverUrl()));
        }
        return list;
    }

    private List<Map<String, String>> bannerFromCms() {
        List<BannerResponse> banners = bannerService.listActive();
        if (banners.isEmpty()) {
            return bannerFromResources();
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (BannerResponse banner : banners) {
            Map<String, String> item = bannerItem(banner.getTitle(), banner.getPath(), banner.getImageUrl());
            item.put("badgeText", banner.getBadgeText() == null ? "专业工程师在线待命" : banner.getBadgeText());
            item.put("subtitle", banner.getSubtitle() == null ? "高效解决您的软件疑难。" : banner.getSubtitle());
            item.put("buttonText", banner.getButtonText() == null ? "立即查看" : banner.getButtonText());
            item.put("buttonPath", banner.getButtonPath() == null || banner.getButtonPath().trim().isEmpty() ? banner.getPath() : banner.getButtonPath());
            list.add(item);
        }
        return list;
    }

    private Map<String, String> defaultHeroBanner() {
        Map<String, String> item = bannerItem("纸飞资 · 云服务", "/pages/service/index", "https://dummyimage.com/1200x480/2563eb/ffffff&text=GameTech+Hub");
        item.put("badgeText", "专业工程师在线待命");
        item.put("subtitle", "高效解决您的软件安装、部署、优化与技术疑难。");
        item.put("buttonText", "立即下单");
        item.put("buttonPath", "/pages/service/index");
        return item;
    }

    private List<Map<String, String>> defaultBanner() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(bannerItem("资源中心", "/pages/resource/index", "https://dummyimage.com/1200x480/1f2937/ffffff&text=Resource+Center"));
        list.add(bannerItem("服务市场", "/pages/service/index", "https://dummyimage.com/1200x480/2563eb/ffffff&text=Service+Market"));
        list.add(bannerItem("工程师入驻", "/pages/engineer/index", "https://dummyimage.com/1200x480/059669/ffffff&text=Engineer+Portal"));
        return list;
    }

    private List<Map<String, String>> defaultMenus() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(menuItem("资源中心", "/pages/resource/index"));
        list.add(menuItem("服务市场", "/pages/service/index"));
        list.add(menuItem("工程师", "/pages/engineer/index"));
        return list;
    }

    private Map<String, String> bannerItem(String title, String path, String image) {
        Map<String, String> item = new HashMap<>();
        item.put("title", title);
        item.put("path", path);
        item.put("image", image);
        return item;
    }

    private Map<String, String> menuItem(String title, String path) {
        Map<String, String> item = new HashMap<>();
        item.put("title", title);
        item.put("path", path);
        return item;
    }
}
