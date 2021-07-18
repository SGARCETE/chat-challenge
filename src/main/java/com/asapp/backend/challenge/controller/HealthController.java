package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.resources.HealthResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
@Validated
public class HealthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @PostMapping
    public ResponseEntity<HealthResource> getHealth() {
        LOGGER.info(String.format("### GET HEALTH REQUEST"));
        return new ResponseEntity(new HealthResource("ok"), HttpStatus.OK);
    }

}
