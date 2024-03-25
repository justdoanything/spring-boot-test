package yong.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class FlywayConfiguration {

    @Value("${spring.datasource.url}")
    private final String url;

    @Value("${spring.datasource.user}")
    private final String user;

    @Value("${spring.datasource.password}")
    private final String password;

    @Value("${spring.profiles.active}")
    private final String profile;

    @PostConstruct
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .outOfOrder(true)
                .locations("classpath:db/migration"
                        , "classpath:db/data")
                .baselineOnMigrate(true)
                .cleanDisabled(false)
                .baselineVersion("0.0")
                .cleanOnValidationError(
                        "local".equals(profile)
                                || "dev".equals(profile))
                .load();

        flyway.migrate();
    }
}
