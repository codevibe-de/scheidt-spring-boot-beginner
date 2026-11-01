// Slide "Fallstrick 4: Rollback" (120)
package c120.rollback;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.RollbackOn;

// global, since Spring Framework 6.2
@Configuration
@EnableTransactionManagement(
        rollbackOn = RollbackOn.ALL_EXCEPTIONS)
public class TransactionConfig {
}
