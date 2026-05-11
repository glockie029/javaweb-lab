package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.FileWriteRequest;
import com.example.lab02.service.FileDemoService;
import java.io.IOException;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileDemoController {

    private final FileDemoService fileDemoService;

    public FileDemoController(FileDemoService fileDemoService) {
        this.fileDemoService = fileDemoService;
    }

    @GetMapping("/storage-info")
    public ApiResponse<Map<String, Object>> storageInfo() throws IOException {
        return ApiResponse.success("file demo storage info", fileDemoService.getStorageInfo());
    }

    @GetMapping("/vuln/read")
    public ApiResponse<Map<String, Object>> readVulnerable(@RequestParam("path") String path) throws IOException {
        return ApiResponse.success("vulnerable file read demo", fileDemoService.readVulnerable(path));
    }

    @GetMapping("/safe/read")
    public ApiResponse<Map<String, Object>> readSafe(@RequestParam("path") String path) throws IOException {
        return ApiResponse.success("safe file read demo", fileDemoService.readSafe(path));
    }

    @PostMapping("/vuln/write")
    public ApiResponse<Map<String, Object>> writeVulnerable(@Valid @RequestBody FileWriteRequest request) throws IOException {
        return ApiResponse.success("vulnerable file write demo", fileDemoService.writeVulnerable(request));
    }

    @PostMapping("/safe/write")
    public ApiResponse<Map<String, Object>> writeSafe(@Valid @RequestBody FileWriteRequest request) throws IOException {
        return ApiResponse.success("safe file write demo", fileDemoService.writeSafe(request));
    }
}
