package com.example.lab02.service;

import com.example.lab02.dto.FileWriteRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FileDemoService {

    private final Path baseDirectory;

    public FileDemoService() throws IOException {
        this.baseDirectory = Paths.get("target", "file-demo-storage").toAbsolutePath().normalize();
        initializeDemoFiles();
    }

    public Map<String, Object> getStorageInfo() throws IOException {
        initializeDemoFiles();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("baseDirectory", baseDirectory.toString());
        data.put("sampleFile", baseDirectory.resolve("notes/welcome.txt").toString());
        data.put("samplePreview", Files.readString(baseDirectory.resolve("notes/welcome.txt")));
        return data;
    }

    public Map<String, Object> readVulnerable(String relativePath) throws IOException {
        Path targetPath = baseDirectory.resolve(relativePath);
        return buildReadResult("vulnerable-file-read", relativePath, targetPath, Files.readString(targetPath));
    }

    public Map<String, Object> readSafe(String relativePath) throws IOException {
        Path targetPath = resolveInsideBase(relativePath);
        return buildReadResult("safe-file-read", relativePath, targetPath, Files.readString(targetPath));
    }

    public Map<String, Object> writeVulnerable(FileWriteRequest request) throws IOException {
        Path targetPath = baseDirectory.resolve(request.getPath());
        Path parent = targetPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(targetPath, request.getContent(), StandardCharsets.UTF_8);
        return buildWriteResult("vulnerable-file-write", request.getPath(), targetPath);
    }

    public Map<String, Object> writeSafe(FileWriteRequest request) throws IOException {
        Path targetPath = resolveInsideBase(request.getPath());
        Path parent = targetPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(targetPath, request.getContent(), StandardCharsets.UTF_8);
        return buildWriteResult("safe-file-write", request.getPath(), targetPath);
    }

    private void initializeDemoFiles() throws IOException {
        Files.createDirectories(baseDirectory.resolve("notes"));
        Path sampleFile = baseDirectory.resolve("notes/welcome.txt");
        if (!Files.exists(sampleFile)) {
            Files.writeString(sampleFile, "stage8 file demo sample", StandardCharsets.UTF_8);
        }
    }

    private Path resolveInsideBase(String relativePath) {
        Path targetPath = baseDirectory.resolve(relativePath).normalize();
        if (!targetPath.startsWith(baseDirectory)) {
            throw new IllegalArgumentException("path escapes base directory");
        }
        return targetPath;
    }

    private Map<String, Object> buildReadResult(String mode, String inputPath, Path resolvedPath, String content) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("mode", mode);
        data.put("inputPath", inputPath);
        data.put("resolvedPath", resolvedPath.toString());
        data.put("content", content);
        return data;
    }

    private Map<String, Object> buildWriteResult(String mode, String inputPath, Path resolvedPath) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("mode", mode);
        data.put("inputPath", inputPath);
        data.put("resolvedPath", resolvedPath.toString());
        return data;
    }
}
