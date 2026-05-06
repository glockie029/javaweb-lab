package com.example.lab01.mapper;

import com.example.lab01.model.MyBatisUserRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyBatisUserMapper {

    List<MyBatisUserRecord> selectByUsernameSafe(@Param("username") String username);

    List<MyBatisUserRecord> selectByUsernameVulnerable(@Param("username") String username);
}
