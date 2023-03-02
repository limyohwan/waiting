package com.yohwan.waiting.domain.system;

import com.yohwan.waiting.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SystemSetting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_setting_id")
    private Long id;
    private Integer expectedWaitTime;

    @Builder
    public SystemSetting(Integer expectedWaitTime) {
        this.expectedWaitTime = expectedWaitTime;
    }

    public void changeExpectedWaitTime(Integer expectedWaitTime){
        this.expectedWaitTime = expectedWaitTime;
    }
}
