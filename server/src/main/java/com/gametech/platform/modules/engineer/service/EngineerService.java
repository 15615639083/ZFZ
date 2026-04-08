package com.gametech.platform.modules.engineer.service;

import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;

import java.util.List;

public interface EngineerService {

    Long apply(EngineerApplyRequest request);

    List<EngineerProfileResponse> listApproved();

    List<EngineerProfileResponse> listAll();
}
