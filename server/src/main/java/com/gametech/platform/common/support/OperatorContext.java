package com.gametech.platform.common.support;

import com.gametech.platform.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class OperatorContext {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    public Long getUserId() {
        HttpServletRequest request = request();
        if (request == null) {
            return 1L;
        }
        String value = request.getHeader(USER_ID_HEADER);
        if (value == null || value.trim().isEmpty()) {
            return 1L;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException exception) {
            throw new BusinessException("invalid X-User-Id header");
        }
    }

    public String getRole() {
        HttpServletRequest request = request();
        if (request == null) {
            return "user";
        }
        String value = request.getHeader(USER_ROLE_HEADER);
        return value == null || value.trim().isEmpty() ? "user" : value.trim().toLowerCase();
    }

    public void requireRole(String role) {
        if (!role.equalsIgnoreCase(getRole())) {
            throw new BusinessException("permission denied");
        }
    }

    public String getRequestIp() {
        HttpServletRequest request = request();
        if (request == null) {
            return "";
        }
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.trim().isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private HttpServletRequest request() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }
}
