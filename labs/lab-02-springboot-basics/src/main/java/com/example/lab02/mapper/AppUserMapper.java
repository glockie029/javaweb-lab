package com.example.lab02.mapper;

import com.example.lab02.model.AppUserRecord;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppUserMapper {

    AppUserRecord findById(@Param("id") Long id);

    AppUserRecord findByUsername(@Param("username") String username);

    List<AppUserRecord> listUsersOrderByVulnerable(@Param("sortField") String sortField);

    List<AppUserRecord> listUsersOrderBySafe(@Param("sortField") String sortField);

    List<AppUserRecord> searchUsersVulnerable(@Param("username") String username,
                                              @Param("role") String role);

    List<AppUserRecord> searchUsersSafe(@Param("username") String username,
                                        @Param("role") String role);

    int updateProfileByUsername(@Param("username") String username,
                                @Param("nickname") String nickname,
                                @Param("email") String email);

    int updateUserSelectiveVulnerable(@Param("username") String username,
                                      @Param("nickname") String nickname,
                                      @Param("email") String email,
                                      @Param("role") String role);

    int updateUserSelectiveSafe(@Param("username") String username,
                                @Param("nickname") String nickname,
                                @Param("email") String email);
}
