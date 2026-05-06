package com.example.lab01.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafeDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fileName = req.getParameter("file");
        if (fileName == null || fileName.trim().isEmpty()) {
            renderMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "缺少 file 参数。");
            return;
        }

        Path baseDir = Paths.get(getServletContext().getRealPath("/demo-files"))
                .toAbsolutePath()
                .normalize();
        Path targetPath = baseDir.resolve(fileName).normalize();

        if (!targetPath.startsWith(baseDir)) {
            renderMessage(resp, HttpServletResponse.SC_FORBIDDEN, "非法路径。");
            return;
        }

        if (!Files.exists(targetPath) || !Files.isRegularFile(targetPath)) {
            renderMessage(resp, HttpServletResponse.SC_NOT_FOUND, "文件不存在。");
            return;
        }

        streamFile(resp, targetPath);
    }

    private void streamFile(HttpServletResponse resp, Path targetPath) throws IOException {
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + targetPath.getFileName().toString() + "\"");

        try (InputStream inputStream = Files.newInputStream(targetPath);
                OutputStream outputStream = resp.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
    }

    private void renderMessage(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println(message);
    }
}
