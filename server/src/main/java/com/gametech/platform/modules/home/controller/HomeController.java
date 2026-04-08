package com.gametech.platform.modules.home.controller;

import com.gametech.platform.common.api.ApiResponse;
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
        data.put("banner", defaultBanner());
        data.put("platformMenus", defaultMenus());
        return ApiResponse.success(data);
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
