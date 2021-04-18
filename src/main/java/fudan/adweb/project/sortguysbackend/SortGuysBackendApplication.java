package fudan.adweb.project.sortguysbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("fudan.adweb.project.sortguysbackend.mapper")
public class SortGuysBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SortGuysBackendApplication.class, args);
	}

}
