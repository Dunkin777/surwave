package epamers.surwave.configuration;

import epamers.surwave.annotations.NotForTests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@NotForTests
@Configuration
public class PostgresConfiguration {
    @Value("${POSTGRES_DB_HOST:localhost}")
    private String host;

    @Value("${POSTGRES_DB:surwave}")
    private String databaseName;

    @Value("${POSTGRES_USER:postgres}")
    private String user;

    @Value("${POSTGRES_PASSWORD:postgres}")
    private String password;

    @Bean
    public DataSource getDataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(String.format("jdbc:postgresql://%s:5432/%s", host, databaseName));
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.username(user);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
}
