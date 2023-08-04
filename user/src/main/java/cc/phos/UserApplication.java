package cc.phos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("cc.phos.mapper")
@EnableConfigurationProperties
@ConfigurationPropertiesScan("cc.phos.properties")
public class UserApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(UserApplication.class, args);
        System.out.println(applicationContext);
    }
}
