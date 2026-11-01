package pizza;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

// exercises a) and c): on startup Boot applied all migrations from db/migration;
// the sample data from db/sample-data (bonus d) only exists in the profile "h2server".
@SpringBootTest
@ActiveProfiles("test")
class FlywayMigrationTest {

    @Autowired
    Flyway flyway;

    @Test
    void allMigrationsApplied() {
        assertThat(flyway.info().applied())
                .extracting(MigrationInfo::getScript)
                .containsExactly("V1__create_tables.sql", "V2__add_customer_email.sql");
        assertThat(flyway.info().pending()).isEmpty();
    }
}
