package cr.cenfotec.focuskids_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class FocuskidsBackendApplication {

	public static void main(String[] args) {
		// Fijar zona horaria de Costa Rica (UTC-6, sin horario de verano)
		TimeZone.setDefault(TimeZone.getTimeZone("America/Costa_Rica"));
		SpringApplication.run(FocuskidsBackendApplication.class, args);
	}

}
