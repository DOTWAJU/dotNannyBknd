package com.dotnanny.dto;

import com.dotnanny.common.Jurisdiction;

import java.util.List;

public record RatioRequest(Jurisdiction jurisdiction, List<ChildInCare> children) {}
