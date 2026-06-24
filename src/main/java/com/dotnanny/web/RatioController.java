package com.dotnanny.web;

import com.dotnanny.dto.RatioRequest;
import com.dotnanny.dto.RatioResult;
import com.dotnanny.service.RatioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratio")
public class RatioController {

    private final RatioService ratio;

    public RatioController(RatioService ratio) {
        this.ratio = ratio;
    }

    /** Evaluate children-in-care against a jurisdiction's ratio limits. */
    @PostMapping("/evaluate")
    public RatioResult evaluate(@RequestBody RatioRequest req) {
        return ratio.evaluate(req.jurisdiction(), req.children());
    }
}
