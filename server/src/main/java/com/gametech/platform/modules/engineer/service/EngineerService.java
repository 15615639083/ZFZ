package com.gametech.platform.modules.engineer.service;

import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;
import com.gametech.platform.modules.engineer.dto.ReviewEngineerRequest;

import java.util.List;

public interface EngineerService {

    Long apply(EngineerApplyRequest request);

    EngineerProfileResponse current();

    List<EngineerProfileResponse> listApproved();

    List<EngineerProfileResponse> listAll();

    List<EngineerProfileResponse> listPending();

    EngineerProfileResponse review(Long engineerId, ReviewEngineerRequest request);
}
