package br.com.rocketdevelopment.jfilesyncserver.config;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FtpClientConfig {

    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.username}")
    private String ftpUsername;

    @Value("${ftp.password}")
    private String ftpPassword;
    private FTPClient ftpClient;
    public FtpClientConfig(){
    }
    public FtpClientConfig(String ftpHost, Integer ftpPort, String ftpUsername, String ftpPassword) {
        this.ftpHost = ftpHost;
        this.ftpPort = ftpPort;
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
    }


    public FTPClient ftpClientConnect() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHost,ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.setControlEncoding("UTF-8");
            // Configurações opcionais
            ftpClient.enterLocalPassiveMode(); // Modo passivo
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // Tipo de arquivo (binário é o mais comum para transferência de dados não-texto)
            ftpClient.setConnectTimeout(10000); // Timeout de conexão em milissegundos
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            System.out.println(ftpClient.isConnected());
        }
        return ftpClient;
    }

    public void ftpClientDisconnect(){
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            System.out.println(ftpClient.isConnected());
        }
    }

}
