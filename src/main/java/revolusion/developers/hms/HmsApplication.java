package revolusion.developers.hms;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import revolusion.developers.hms.service.impl.EmailServiceImpl;

@SpringBootApplication
public class HmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmsApplication.class, args);

        // for swagger documentation
        // http://localhost:8080/swagger-ui/index.html

        // my portfolio
        // https://nizomiddin-portfolio.netlify.app/






    }

}
