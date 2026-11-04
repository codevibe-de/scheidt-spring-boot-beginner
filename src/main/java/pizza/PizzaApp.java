package pizza;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import pizza.util.CliArgs;
import pizza.util.CommandLineRunner;

import java.util.List;

@Configuration
@ComponentScan
public class PizzaApp {

    @Autowired
    private List<CommandLineRunner> commandLineRunners;

    // CliArgs is only registered by main(). A test that boots the context (e.g. @SpringJUnitConfig)
    // never runs main(), so the bean may be absent
    @Autowired(required = false)
    private CliArgs cliArgs;

    @EventListener(ContextRefreshedEvent.class)
    public void runRunners() {
        String[] args = (cliArgs == null) ? new String[0] : cliArgs.args();
        this.commandLineRunners.forEach(r -> r.run(args));
    }


    public static void main(String[] args) {
        // Instantiate annotation configured context ---
        try (var beanContainer = new AnnotationConfigApplicationContext()) {
            beanContainer.register(PizzaApp.class);
            beanContainer.registerBean(CliArgs.class, () -> new CliArgs(args));
            beanContainer.refresh();
        }
    }

}
