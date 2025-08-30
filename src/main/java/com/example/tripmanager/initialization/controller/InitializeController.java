package com.example.tripmanager.initialization.controller;

import com.example.tripmanager.auth.service.AuthService;
import com.example.tripmanager.initialization.config.NoUsersCondition;
import com.example.tripmanager.initialization.dto.AdminInitRequest;
import com.example.tripmanager.initialization.shutdowner.AppShutdowner;
import com.example.tripmanager.shared.model.messageResponse.MessageResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Conditional(NoUsersCondition.class)
@RestController
@RequestMapping(InitializeController.INIT_CONTROLLER_URL)
public class InitializeController {
    private static final Logger log = LoggerFactory.getLogger(InitializeController.class);
    public static final String INIT_CONTROLLER_URL = "/api/init";
    public static final String INIT_ENABLED_URL = INIT_CONTROLLER_URL + "/enabled";

    private final AuthService authService;
    private final AppShutdowner appShutdowner;
    private final String initToken;

    public InitializeController(AuthService authService, AppShutdowner appShutdowner,
                                @Value("${token.initialization.value}") String initToken) {
        this.authService = authService;
        this.appShutdowner = appShutdowner;
        this.initToken = initToken;
    }

    @GetMapping("/enabled")
    public ResponseEntity<Boolean> enabled() {
        return ResponseEntity.ok(true);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> initialize(
            @Valid @RequestBody AdminInitRequest req,
            @RequestHeader(value = "X-Init-Token", required = false) String initToken) {
        validateInitToken(initToken);
        authService.registerAdmin(req, true);
        appShutdowner.shutdown();
        MessageResponse messageResponse = new MessageResponse("Admin created. Please check if application starts automatically. Restarting application...");
        log.info("Application shutting down due to finalize admin initialization, this controller will not be available anymore.");
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    private void validateInitToken(String provided) {
        if (StringUtils.isBlank(initToken)) {
            log.debug("Init token not configured; skipping validation.");
            return;
        }

        if (StringUtils.isBlank(provided)) {
            log.warn("Init token missing in request.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        byte[] a = provided.getBytes(StandardCharsets.UTF_8);
        byte[] b = initToken.getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(a, b)) {
            log.warn("Invalid init token provided.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
