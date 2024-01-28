package br.com.rocketdevelopment.jfilesyncserver;

import br.com.rocketdevelopment.jfilesyncserver.service.FileManagerService;
import br.com.rocketdevelopment.jfilesyncserver.service.SmbService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootApplication
public class JFileSyncServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JFileSyncServerApplication.class, args);
	}


}
