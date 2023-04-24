package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd01admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd01admin",
        password = "admin",
        serverName = "ssbd_db",
        portNumber = 5432,
        databaseName = "ssbd01",
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10)

@Stateless
public class DataSourceConfig {

    @PersistenceContext(unitName = "ssbd01adminPU")
    private EntityManager em;
}
