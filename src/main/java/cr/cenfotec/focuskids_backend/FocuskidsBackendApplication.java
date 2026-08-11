package cr.cenfotec.focuskids_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class FocuskidsBackendApplication {

	public static void main(String[] args) {
		// Fijar zona horaria de Costa Rica (UTC-6, sin horario de verano)
		TimeZone.setDefault(TimeZone.getTimeZone("America/Costa_Rica"));
		SpringApplication.run(FocuskidsBackendApplication.class, args);
	}

}
