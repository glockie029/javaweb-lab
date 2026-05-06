package com.example.lab01.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class VulnerableUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            renderMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "没有上传文件。");
            return;
        }

        String originalFileName = filePart.getSubmittedFileName();
        Path baseDir = Paths.get(getServletContext().getRealPath("/demo-storage/uploads-vuln"));
        Files.createDirectories(baseDir);

        Path targetPath = baseDir.resolve(originalFileName);
        filePart.write(targetPath.toString());

        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println("上传成功（脆弱示例）: " + targetPath);
    }

    private void renderMessage(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println(message);
    }
}
