package br.com.rocketdevelopment.jfilesyncserver;

import br.com.rocketdevelopment.jfilesyncserver.service.FileManagerService;
import br.com.rocketdevelopment.jfilesyncserver.service.FtpService;
import br.com.rocketdevelopment.jfilesyncserver.service.SmbService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JFileSyncServerApplicationTests {


	@Autowired
	private SmbService smbService;

	@Autowired
	private FileManagerService fileManagerService;

	@Autowired
	private FtpService ftpService;

	@Test
	@Order(1)
	void fromSMBtoLocalFile() throws IOException {
		OutputStream outputStream = smbService.downloadFile("saida/setting_v2.zip");
		InputStream inputStream = fileManagerService.convertOutputStreamToInputStream(outputStream);
		fileManagerService.writeFileInputStream("C:\\Projetos\\setting_v2.zip",inputStream);
	}
	@Test
	@Order(2)
	void deleteLocalFile() throws IOException {
		boolean delete = fileManagerService.delete("C:\\Projetos\\setting_v2.zip");
		Assertions.assertTrue(delete);
	}

	@Test
	@Order(3)
	void fromSMBtoFtp() throws IOException {
		OutputStream outputStream = smbService.downloadFile("saida/setting_v2.zip");

		InputStream inputStream = fileManagerService.convertOutputStreamToInputStream(outputStream);
		ftpService.ftpClientConnect();
		ftpService.writeFileInputStream("/entrada/setting_v2.zip",inputStream);
		ftpService.ftpClientDisconnect();
	}
	@Test
	@Order(4)
	void deleteFtp() throws IOException {
		ftpService.ftpClientConnect();
		boolean delete = ftpService.deleteFile("/files/entrada/setting_v2.zip");
		ftpService.ftpClientDisconnect();
		Assertions.assertTrue(delete);
	}

	@Test
	@Order(5)
	public void fromFTPtoSMB() throws IOException {
		ftpService.ftpClientConnect();
		InputStream inputStream = ftpService.readFTPInputStream("/files/saida/setting_v2.zip");
		ftpService.ftpClientDisconnect();
		smbService.uploadFile("entrada/setting_v2.zip",inputStream);

	}

	@Test
	@Order(6)
	public void testCopyFileFTP() throws IOException {
		ftpService.ftpClientConnect();
		InputStream inputStream = ftpService.readFTPInputStream("/files/saida/setting_v2.zip");
		fileManagerService.writeFileInputStream("C:\\Projetos\\Test\\setting_v2.zip", inputStream);
		ftpService.ftpClientDisconnect();
	}
	@Test
	@Order(7)
	public void testCopyFileToFTP() throws IOException {
		ftpService.ftpClientConnect();
		InputStream inputStream1 = fileManagerService.readFileInputStream("C:\\Projetos\\Test\\setting_v2.zip");
		ftpService.writeFileInputStream("/entrada/setting_v2.zip",inputStream1);
		ftpService.ftpClientDisconnect();
	}
}
