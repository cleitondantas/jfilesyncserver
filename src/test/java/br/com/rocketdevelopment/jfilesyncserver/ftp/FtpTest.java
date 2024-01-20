package br.com.rocketdevelopment.jfilesyncserver.ftp;

import br.com.rocketdevelopment.jfilesyncserver.config.FtpClientConfig;
import br.com.rocketdevelopment.jfilesyncserver.service.FileManagerService;
import br.com.rocketdevelopment.jfilesyncserver.service.FtpService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest(classes = FtpClientConfig.class)
public class FtpTest {

    @Autowired
    private FtpService ftpService;

    @Autowired
    private FileManagerService fileManagerService;

    @Test
    public void testCopyAllFile() throws IOException {
        InputStream inputStream1 = fileManagerService.readFileInputStream("C:\\Projetos\\Test\\Thiago2.pdf");
        ftpService.writeFileInputStream("/files/Bruna/Documents/Thiago3.pdf",inputStream1);
    }

    @Test
    public void testCopyFileFTP() throws IOException {
        InputStream inputStream = ftpService.readFTPInputStream("/files/Bruna/Documents/Thiago1.pdf");
        fileManagerService.writeFileInputStream("C:\\Projetos\\Test\\Thiago1.pdf", inputStream);
    }
    @Test
    public void testCopyFileToFTP() throws IOException {
        InputStream inputStream1 = fileManagerService.readFileInputStream("C:\\Projetos\\Test\\Thiago1.pdf");
        ftpService.writeFileInputStream("/files/Bruna/Documents/Thiago2.pdf",inputStream1);
    }
}
