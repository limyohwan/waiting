package com.yohwan.waiting.repository;

import com.yohwan.waiting.domain.system.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
}
