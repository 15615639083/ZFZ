package com.gametech.platform.modules.system.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/front/upload")
public class UploadController {

    private final OperatorContext operatorContext;

    @Value("${server.port:8090}")
    private String serverPort;

    public UploadController(OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    @PostMapping("/image")
    public ApiResponse<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        operatorContext.requireLogin();
        if (file == null || file.isEmpty()) {
            return ApiResponse.fail("file is required");
        }
        String originalName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalName);
        String fileName = UUID.randomUUID().toString().replace("-", "") + (extension == null ? "" : "." + extension);
        File dir = new File(new File(System.getProperty("user.dir"), "uploads"), "avatar");
        if (!dir.exists() && !dir.mkdirs()) {
            return ApiResponse.fail("failed to create upload directory");
        }
        File target = new File(dir, fileName);
        file.transferTo(target);

        Map<String, String> data = new HashMap<>();
        data.put("url", "http://127.0.0.1:" + serverPort + "/uploads/avatar/" + fileName);
        return ApiResponse.successMessage("upload success", data);
    }
}
