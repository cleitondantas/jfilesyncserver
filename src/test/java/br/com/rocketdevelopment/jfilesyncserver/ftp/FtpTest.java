package br.com.rocketdevelopment.jfilesyncserver.ftp;

import br.com.rocketdevelopment.jfilesyncserver.config.FtpClientConfig;
import br.com.rocketdevelopment.jfilesyncserver.service.FtpService;
import org.apache.commons.net.ftp.FTPClient;

import org.junit.jupiter.api.Test;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FtpTest {

    @Test
    public void test() throws IOException {
        FtpClientConfig ftpClientConfig = new FtpClientConfig("192.168.1.54", 21, "cyber_tronyk", "Pedepano10!");
        FTPClient ftpClient = ftpClientConfig.ftpClientConnect();
        FtpService ftpService = new FtpService(ftpClient);
        assertTrue(ftpClient.isConnected());
        ftpService.downloadFile(ftpClient, "/files/Cleiton/setting_v2.zip", "C:\\Projetos\\setting_v2.zip");

//        ftpService.listDirectory(ftpClient, "", "", 0);
        ftpClientConfig.ftpClientDisconnect();
        assertFalse(ftpClient.isConnected());
    }



}
