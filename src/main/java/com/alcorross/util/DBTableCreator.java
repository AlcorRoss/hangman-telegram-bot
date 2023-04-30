package com.alcorross.util;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public final class DBTableCreator {
    private DBTableCreator() {
    }

    public static void createDBTable() {
        try (var connection = DataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db.changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts("Create table"), new LabelExpression());
        } catch (SQLException | LiquibaseException e) {
            log.info("Couldn't create tables", e);
        }
    }
}
