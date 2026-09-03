package com.rookies6.MySpringBootLabProject;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MyEnvironment {
    private String mode;
}