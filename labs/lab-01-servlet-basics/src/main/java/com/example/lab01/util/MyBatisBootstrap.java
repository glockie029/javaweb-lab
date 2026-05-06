package com.example.lab01.util;

import com.example.lab01.mapper.MyBatisUserMapper;
import com.example.lab01.model.MyBatisUserRecord;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public final class MyBatisBootstrap {

    private static final String JDBC_URL = "jdbc:h2:mem:lab01db;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";
    private static final SqlSessionFactory SQL_SESSION_FACTORY = buildSqlSessionFactory();

    private MyBatisBootstrap() {
    }

    public static List<MyBatisUserRecord> selectByUsernameSafe(String username) {
        try (SqlSession sqlSession = SQL_SESSION_FACTORY.openSession()) {
            MyBatisUserMapper mapper = sqlSession.getMapper(MyBatisUserMapper.class);
            return mapper.selectByUsernameSafe(username);
        }
    }

    public static List<MyBatisUserRecord> selectByUsernameVulnerable(String username) {
        try (SqlSession sqlSession = SQL_SESSION_FACTORY.openSession()) {
            MyBatisUserMapper mapper = sqlSession.getMapper(MyBatisUserMapper.class);
            return mapper.selectByUsernameVulnerable(username);
        }
    }

    private static SqlSessionFactory buildSqlSessionFactory() {
        try {
            Properties properties = new Properties();
            properties.setProperty("jdbc.driver", JDBC_DRIVER);
            properties.setProperty("jdbc.url", JDBC_URL);
            properties.setProperty("jdbc.username", JDBC_USERNAME);
            properties.setProperty("jdbc.password", JDBC_PASSWORD);

            Reader reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, properties);
            initializeDatabase(factory);
            return factory;
        } catch (Exception ex) {
            throw new IllegalStateException("初始化 MyBatis 失败", ex);
        }
    }

    private static void initializeDatabase(SqlSessionFactory factory) throws Exception {
        try (SqlSession sqlSession = factory.openSession();
                Connection connection = sqlSession.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS demo_users");
            statement.execute("CREATE TABLE demo_users ("
                    + "id INT PRIMARY KEY, "
                    + "username VARCHAR(64), "
                    + "role VARCHAR(32))");
            statement.execute("INSERT INTO demo_users (id, username, role) VALUES "
                    + "(1, 'alice', 'user'), "
                    + "(2, 'bob', 'user'), "
                    + "(3, 'admin', 'admin')");
            connection.commit();
        }
    }
}
