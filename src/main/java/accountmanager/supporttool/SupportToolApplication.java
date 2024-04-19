package accountmanager.supporttool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SupportToolApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SupportToolApplication.class, args);
		SpringApplication application = new SpringApplication(SupportToolApplication.class);
		application.setAdditionalProfiles("prod");
		application.run(args);
	}

}
