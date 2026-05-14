package com.example.lab02.service;

import com.example.lab02.dto.UrlFetchRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SsrfDemoService {

    private final HttpClient httpClient;
    private final Set<String> allowedHosts;

    public SsrfDemoService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.allowedHosts = Set.of("example.com", "httpbin.org");
    }

    public Map<String, Object> getContextInfo() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("sink", "HttpClient.send(...)");
        data.put("dangerousEndpoint", "/api/ssrf/vuln/fetch");
        data.put("safeEndpoint", "/api/ssrf/safe/fetch");
        data.put("allowedHosts", allowedHosts);
        data.put("sampleUrls", Arrays.asList(
                "https://example.com/",
                "https://httpbin.org/get",
                "http://127.0.0.1:8080/actuator/health"));
        return data;
    }

    public Map<String, Object> fetchVulnerable(UrlFetchRequest request) {
        URI uri = normalizeUri(request.getUrl());
        return executeFetch("vulnerable-ssrf-demo", uri);
    }

    public Map<String, Object> fetchSafe(UrlFetchRequest request) {
        URI uri = normalizeUri(request.getUrl());
        validateSafeTarget(uri);
        return executeFetch("safe-ssrf-demo", uri);
    }

    private URI normalizeUri(String rawUrl) {
        try {
            return URI.create(rawUrl.trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("url format is invalid");
        }
    }

    private void validateSafeTarget(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
            throw new IllegalArgumentException("safe ssrf demo only allows http/https");
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("safe ssrf demo requires a host name");
        }
        if (!allowedHosts.contains(host.toLowerCase())) {
            throw new IllegalArgumentException("host is not in allow list");
        }

        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                if (address.isAnyLocalAddress()
                        || address.isLoopbackAddress()
                        || address.isLinkLocalAddress()
                        || address.isSiteLocalAddress()
                        || address.isMulticastAddress()) {
                    throw new IllegalArgumentException("resolved address points to local or private network");
                }
            }
        } catch (UnknownHostException exception) {
            throw new IllegalArgumentException("host cannot be resolved");
        }
    }

    private Map<String, Object> executeFetch(String mode, URI uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("mode", mode);
            data.put("targetUrl", uri.toString());
            data.put("statusCode", response.statusCode());
            data.put("contentType", response.headers().firstValue("Content-Type").orElse("unknown"));
            data.put("bodyPreview", truncate(response.body(), 200));
            return data;
        } catch (Exception exception) {
            throw new SsrfDemoFetchException("server-side fetch failed for url: " + uri, exception);
        }
    }

    private String truncate(String content, int maxLength) {
        if (content == null) {
            return null;
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
