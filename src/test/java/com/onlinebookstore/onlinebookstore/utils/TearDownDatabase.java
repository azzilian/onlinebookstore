package com.onlinebookstore.onlinebookstore.utils;

import java.sql.Connection;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public class TearDownDatabase {

    @SneakyThrows
    public static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-all-from-all-tables.sql")
            );
        }
    }
}
