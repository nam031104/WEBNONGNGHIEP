package btl.nongnghiep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "btl.nongnghiep")
public class BeLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeLoginApplication.class, args);
    }
}

