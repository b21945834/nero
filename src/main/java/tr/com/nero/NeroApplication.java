package tr.com.nero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class NeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeroApplication.class, args);
	}

}
