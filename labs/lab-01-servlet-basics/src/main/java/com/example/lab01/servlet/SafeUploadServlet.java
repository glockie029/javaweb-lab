package com.example.lab01.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class SafeUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            renderMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "没有上传文件。");
            return;
        }

        String originalFileName = filePart.getSubmittedFileName();
        String suffix = extractAllowedSuffix(originalFileName);
        if (suffix == null) {
            renderMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "仅允许上传 .txt、.pdf、.png 文件。");
            return;
        }

        Path baseDir = Paths.get(getServletContext().getRealPath("/demo-storage/uploads-safe"))
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(baseDir);

        String generatedFileName = UUID.randomUUID().toString() + suffix;
        Path targetPath = baseDir.resolve(generatedFileName).normalize();

        if (!targetPath.startsWith(baseDir)) {
            renderMessage(resp, HttpServletResponse.SC_FORBIDDEN, "非法上传路径。");
            return;
        }

        filePart.write(targetPath.toString());

        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println("上传成功（安全示例）: " + generatedFileName);
    }

    private String extractAllowedSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }

        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".txt")) {
            return ".txt";
        }
        if (lowerName.endsWith(".pdf")) {
            return ".pdf";
        }
        if (lowerName.endsWith(".png")) {
            return ".png";
        }
        return null;
    }

    private void renderMessage(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println(message);
    }
}
