package br.com.rocketdevelopment.jfilesyncserver;

import br.com.rocketdevelopment.jfilesyncserver.service.FileManagerService;
import br.com.rocketdevelopment.jfilesyncserver.service.FtpService;
import br.com.rocketdevelopment.jfilesyncserver.service.SmbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootTest
class JFileSyncServerApplicationTests {
	@Autowired
	private SmbService smbService;

	@Autowired
	private FileManagerService fileManagerService;

	@Autowired
	private FtpService ftpService;
//	@Test
//	void contextLoads() throws IOException {
//		OutputStream outputStream = smbService.downloadFile("Cleiton/setting_v2.zip");
//		InputStream inputStream = fileManagerService.convertOutputStreamToInputStream(outputStream);
//		fileManagerService.writeFileInputStream("C:\\Projetos\\setting_v2.zip",inputStream);
//	}

	@Test
	void contextLoads2() throws IOException {
		OutputStream outputStream = smbService.downloadFile("saida/setting_v2.zip");
		InputStream inputStream = fileManagerService.convertOutputStreamToInputStream(outputStream);
		ftpService.writeFileInputStream("/files/entrada/setting_v2.zip",inputStream);
	}

}
