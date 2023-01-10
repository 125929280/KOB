package com.kob.backend.service.user.account;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface InfoService {
    Map<String, String> getInfo();

    Map<String, String> updatePassword(Map<String, String> data);

    Map<String, String> updatePhoto(MultipartFile data);
}
