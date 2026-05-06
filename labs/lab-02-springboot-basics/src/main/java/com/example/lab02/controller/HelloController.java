package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.service.GreetingService;
import com.example.lab02.service.UserQueryService;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final GreetingService greetingService;
    private final UserQueryService userQueryService;

    public HelloController(GreetingService greetingService, UserQueryService userQueryService) {
        this.greetingService = greetingService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/hello")
    public ApiResponse<Map<String, String>> hello(@RequestParam(name = "name", required = false) String name) {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("message", greetingService.buildGreeting(name));
        return ApiResponse.success("requestParam demo", data);
    }

    @GetMapping("/users/{id}")
    public ApiResponse<Map<String, Object>> userById(@PathVariable("id") Long id) {
        Map<String, Object> data = userQueryService.getUserSummaryById(id);
        if (data == null) {
            return ApiResponse.failure("user not found");
        }
        return ApiResponse.success("pathVariable + mybatis demo", data);
    }

    @GetMapping("/users/vuln-list")
    public ApiResponse<List<Map<String, Object>>> usersVulnerable(@RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField) {
        return ApiResponse.success("mybatis dynamic sql vulnerable demo", userQueryService.listUserSummariesVulnerable(sortField));
    }

    @GetMapping("/users/safe-list")
    public ApiResponse<List<Map<String, Object>>> usersSafe(@RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField) {
        return ApiResponse.success("mybatis dynamic sql safe demo", userQueryService.listUserSummariesSafe(sortField));
    }

    @GetMapping("/users/vuln-search")
    public ApiResponse<List<Map<String, Object>>> usersSearchVulnerable(@RequestParam(name = "username", required = false) String username,
                                                                         @RequestParam(name = "role", required = false) String role) {
        return ApiResponse.success("mybatis if/where vulnerable demo", userQueryService.searchUserSummariesVulnerable(username, role));
    }

    @GetMapping("/users/safe-search")
    public ApiResponse<List<Map<String, Object>>> usersSearchSafe(@RequestParam(name = "username", required = false) String username,
                                                                  @RequestParam(name = "role", required = false) String role) {
        return ApiResponse.success("mybatis if/where safe demo", userQueryService.searchUserSummariesSafe(username, role));
    }

    @GetMapping("/users/vuln-update")
    public ApiResponse<Map<String, Object>> usersUpdateVulnerable(@RequestParam("username") String username,
                                                                  @RequestParam(name = "nickname", required = false) String nickname,
                                                                  @RequestParam(name = "email", required = false) String email,
                                                                  @RequestParam(name = "role", required = false) String role) {
        return ApiResponse.success("mybatis set vulnerable demo", userQueryService.updateUserSelectiveVulnerable(username, nickname, email, role));
    }

    @GetMapping("/users/safe-upda te")
    public ApiResponse<Map<String, Object>> usersUpdateSafe(@RequestParam("username") String username,
                                                            @RequestParam(name = "nickname", required = false) String nickname,
                                                            @RequestParam(name = "email", required = false) String email) {
        return ApiResponse.success("mybatis set safe demo", userQueryService.updateUserSelectiveSafe(username, nickname, email));
    }
}
