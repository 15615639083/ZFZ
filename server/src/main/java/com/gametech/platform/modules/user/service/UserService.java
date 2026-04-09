package com.gametech.platform.modules.user.service;

import com.gametech.platform.modules.user.dto.AdminUserResponse;
import com.gametech.platform.modules.user.dto.ToggleUserStatusRequest;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.dto.UpdateUserProfileRequest;

import java.util.List;

public interface UserService {

    UserProfileResponse currentUser();

    UserProfileResponse updateCurrentUser(UpdateUserProfileRequest request);

    List<AdminUserResponse> listAllUsers();

    AdminUserResponse adminUserDetail(Long userId);

    AdminUserResponse updateUserStatus(Long userId, ToggleUserStatusRequest request);
}
