package com.example.lab02.service;

import com.example.lab02.dto.CommandRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CommandDemoService {

    private final Path baseDirectory;

    public CommandDemoService() throws IOException {
        this.baseDirectory = Paths.get("target", "cmd-demo").toAbsolutePath().normalize();
        Files.createDirectories(baseDirectory);
    }

    public Map<String, Object> getStorageInfo() throws IOException {
        Files.createDirectories(baseDirectory);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("baseDirectory", baseDirectory.toString());
        data.put("vulnerableMarker", baseDirectory.resolve("vuln-triggered.txt").toString());
        data.put("safeMarker", baseDirectory.resolve("safe-triggered.txt").toString());
        return data;
    }

    public Map<String, Object> runVulnerable(CommandRequest request) throws IOException {
        String command = request.getCommand();
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException exception) {
            throw new CommandExecutionException("command execution failed: " + command, exception);
        }
        String cmdoutput = output.toString().trim();
        String markerContent = "mode=vulnerable\n"
                + "simulatedSink=Runtime.getRuntime().exec(command)\n"
                + "rawCommand=" + command + "\n"
                + "triggeredAt=" + Instant.now() + "\n"
                + "output=" + cmdoutput + "\n";
        Path markerPath = baseDirectory.resolve("vuln-triggered.txt");
        Files.writeString(markerPath, markerContent, StandardCharsets.UTF_8);
        return buildResult("vulnerable-command-demo", command, markerPath, markerContent);
    }

    public Map<String, Object> runSafe(CommandRequest request) throws IOException {
        String normalized = request.getCommand().trim();
        String fixedAction = mapAllowedAction(normalized);
        String markerContent = "mode=safe\n"
                + "allowedAction=" + normalized + "\n"
                + "fixedAction=" + fixedAction + "\n"
                + "triggeredAt=" + Instant.now() + "\n";
        Path markerPath = baseDirectory.resolve("safe-triggered.txt");
        Files.writeString(markerPath, markerContent, StandardCharsets.UTF_8);
        return buildResult("safe-command-demo", normalized, markerPath, markerContent);
    }

    private String mapAllowedAction(String command) {
        if ("whoami".equals(command)) {
            return "show-current-user";
        }
        if ("pwd".equals(command)) {
            return "show-working-directory";
        }
        if ("date".equals(command)) {
            return "show-current-date";
        }
        throw new IllegalArgumentException("command is not in allow list");
    }

    private Map<String, Object> buildResult(String mode, String inputCommand, Path markerPath, String markerContent) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("mode", mode);
        data.put("inputCommand", inputCommand);
        data.put("markerPath", markerPath.toString());
        data.put("markerContent", markerContent);
        return data;
    }
}
