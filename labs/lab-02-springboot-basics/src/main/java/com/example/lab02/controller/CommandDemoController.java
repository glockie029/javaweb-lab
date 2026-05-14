package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.CommandRequest;
import com.example.lab02.service.CommandDemoService;
import java.io.IOException;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cmd")
public class CommandDemoController {

    private final CommandDemoService commandDemoService;

    public CommandDemoController(CommandDemoService commandDemoService) {
        this.commandDemoService = commandDemoService;
    }

    @GetMapping("/storage-info")
    public ApiResponse<Map<String, Object>> storageInfo() throws IOException {
        return ApiResponse.success("command demo storage info", commandDemoService.getStorageInfo());
    }

    @PostMapping("/vuln/run")
    public ApiResponse<Map<String, Object>> runVulnerable(@Valid @RequestBody CommandRequest request) throws IOException {
        return ApiResponse.success("vulnerable command execution demo", commandDemoService.runVulnerable(request));
    }

    @PostMapping("/safe/run")
    public ApiResponse<Map<String, Object>> runSafe(@Valid @RequestBody CommandRequest request) throws IOException {
        return ApiResponse.success("safe command execution demo", commandDemoService.runSafe(request));
    }
}
