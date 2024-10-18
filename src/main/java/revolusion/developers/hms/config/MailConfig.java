package revolusion.developers.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Bean
    public MailTrapConfig mailTrapConfig() {
        MailTrapConfig config = new MailTrapConfig();
        config.setUsername("9843650206ec60");
        config.setPassword("52ef475e7e50d9");
        return config;
    }

}
