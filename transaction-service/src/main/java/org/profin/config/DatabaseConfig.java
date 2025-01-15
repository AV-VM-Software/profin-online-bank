package org.profin.config;


import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * Configuration class responsible for initializing and populating the database schema.
 * It uses an initializer to execute database scripts on application startup.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Creates and configures a ConnectionFactoryInitializer to populate the schema
     * from an SQL script. The populator executes predefined SQL statements, such
     * as table creation or seed data insertion, every time the application starts.
     *
     * @param connectionFactory the ConnectionFactory used to establish reactive connections
     * @return a configured ConnectionFactoryInitializer that applies the SQL schema on startup
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}