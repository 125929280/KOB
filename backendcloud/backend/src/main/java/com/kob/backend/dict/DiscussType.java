package com.kob.backend.dict;

import jdk.jfr.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscussType {
    @Label("分享")
    BLOG("分享"),
    @Label("题解")
    SOLUTION("题解"),
    @Label("吐槽")
    CHAT("吐槽"),
    @Label("建议")
    ADVICE("建议");

    private String desc;
}
