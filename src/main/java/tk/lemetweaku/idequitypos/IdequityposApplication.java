package tk.lemetweaku.idequitypos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@MapperScan("tk.lemetweaku.idequitypos.dao")
@Configuration
@SpringBootApplication
public class IdequityposApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdequityposApplication.class, args);
    }

}
