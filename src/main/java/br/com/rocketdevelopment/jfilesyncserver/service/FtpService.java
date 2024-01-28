package br.com.rocketdevelopment.jfilesyncserver.service;

import java.io.*;
import java.net.SocketException;

import br.com.rocketdevelopment.jfilesyncserver.config.FtpClientConfig;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe criada para trabalhar com FTP.
 * @author Felipe Santaniello
 * @date 15/12/2015
 *
 */
@Service
public class FtpService {

    private FtpClientConfig ftpClientConfig;
    private FTPClient ftpClient;
    @Autowired
    public FtpService(FtpClientConfig ftpClientConfig) {
        this.ftpClientConfig = ftpClientConfig;
    }
    public FtpService() {
    }


    /**
     * Método que retorna apenas uma lista com o nome dos diretórios e arquivos do FTP.
     * @author Felipe Santaniello
     * @data 15/12/2015
     * @param  diretorio String - Nome do diretório a ser listado.
     * @throws SocketException
     * @throws IOException
     * @return String[] - Retorna uma lista de Strings com o nome dos arquivos e diretórios contidos no diretório informado.
     *
     * */
    public String[] getNameDirs(String diretorio) throws SocketException, IOException {
        String[] nameDirs = null;
        try {
            ftpClientConnect();
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(diretorio);
            nameDirs = ftpClient.listNames();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClientDisconnect();
        }
        return nameDirs;
    }

    /**
     *
     * Método que devolve diversas propriedades dos arquivos e diretórios do FTP tais como: permissões, tamanho dos arquivos e diretórios e etc... É mais completo que o getNameDirs.
     * @author Felipe Santaniello
     * @data 15/12/2015
     * @param  diretorio String - Nome do diretório a ser listado.
     * @throws SocketException
     * @throws IOException
     * @return FTPFile[] - Retorna uma lista do tipo FTPFile com o nome dos arquivos e diretórios contidos no diretório informado.
     *
     * */
    public FTPFile[] getConfigFTPFiles(String diretorio) {
        ftpClientConnect();
        FTPFile[] filesConfig = null;
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(diretorio);
            filesConfig = ftpClient.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClientDisconnect();
        }
        return filesConfig;
    }


    /**
     * Envia um arquivo para o servidor FTP.
     * @author Felipe Santaniello
     * @data 15/12/2015
     * @description Envia um arquivo para o servidor FTP.
     * @param caminhoArquivo String - Caminho aonde está localizado o arquivo a ser enviado.
     * @param  arquivo String - Nome do arquivo.
     * @throws IOException
     * @return boolean - Retorna true se o arquivo foi enviado e false caso contrário.
     *
     * */
    public boolean sendFTPFile(String caminhoArquivo, String arquivo) throws IOException {
        FileInputStream arqEnviar = null;
        try {
            ftpClientConnect();
            ftpClient.enterLocalPassiveMode();
            arqEnviar = new FileInputStream(caminhoArquivo);
            if (ftpClient.storeFile(arquivo, arqEnviar)) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            ftpClientDisconnect();
        }
        return false;
    }


    /**
     * Obtém um arquivo do servidor FTP.
     * @author Felipe Santaniello
     * @data 15/12/2015
     * @description Obtém um arquivo do servidor FTP.
     * @param  arquivo String - Nome do arquivo a ser baixado.
     * @return void
     *
     * */
    public void getFile(String arquivo) {
        try {

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE );
            OutputStream os = new FileOutputStream(arquivo);
            ftpClient.retrieveFile(arquivo, os );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String remoteFilePath, String savePath) throws IOException {
        OutputStream outputStream1 = new FileOutputStream(savePath);
        boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream1);
        outputStream1.close();

        if (success) {
            System.out.println("File #1 has been downloaded successfully.");
        }
    }
    public void uploadFile(String localFilePath, String remoteFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(localFilePath);
        System.out.println("Start uploading first file");
        boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
    }
    public boolean deleteFile(String filePath) throws IOException {
        boolean deleted = ftpClient.deleteFile(filePath);
        if (deleted) {
            System.out.println("The file was deleted successfully.");
        }
        return deleted;
    }

    public void createDirectory(String dirPath) throws IOException {
        boolean created = ftpClient.makeDirectory(dirPath);
        if (created) {
            System.out.println("CREATED a new directory: " + dirPath);
        } else {
            System.out.println("COULD NOT create a new directory: " + dirPath);
        }
    }
    public InputStream readFTPInputStream(String source){
        InputStream is = null;
        try {
            ftpClientConnect();
            is = ftpClient.retrieveFileStream(source);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClientDisconnect();
        }
        return is;
    }

    public void writeFileInputStream(String destination,InputStream in){
        try {
            ftpClientConnect();
            ftpClient.storeFile(destination, in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClientDisconnect();
        }
    }

    public void copyAllFiles(String source, String destination) {
        try {
            ftpClientConnect();
            FTPFile[] files = ftpClient.listFiles(source);
            for (FTPFile file : files) {
                ftpClientConnect();
                String dest = destination + "\\" + file.getName();
                if (file.isDirectory()) {
                    new File(dest).mkdirs(); // Cria o diretório no destino
                    copyAllFiles(source + "/" + file.getName(), dest);
                } else {
                    try {
                        InputStream is = ftpClient.retrieveFileStream(source + "/" + file.getName());
                        if (is != null) {
                            OutputStream os = new FileOutputStream(dest);
                            byte[] bytes = new byte[1024];
                            int read = 0;
                            while ((read = is.read(bytes)) != -1) {
                                os.write(bytes, 0, read);
                            }
                            is.close();
                            os.close();
                        } else {
                            System.out.println("File not found: " + source + "/" + file.getName());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to retrieve file: " + source + "/" + file.getName());
                        e.printStackTrace();
                    }
                }
                ftpClientDisconnect();
            }
        } catch (IOException e) {
            System.out.println("Failed to list files in: " + source);
            e.printStackTrace();
        }
        ftpClientDisconnect();
    }



    public void listDirectory(String parentDir, String currentDir, int level) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
        // Imprime o caminho completo do diretório atual
        System.out.println("Diretório: " + dirToList);
        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // Ignora referências ao diretório atual e ao diretório pai
                    continue;
                }
                for (int i = 0; i < level; i++) {
                    System.out.print("\t");
                }
                System.out.println(aFile.getName());

                if (aFile.isDirectory()) {
                    listDirectory(dirToList, currentFileName, level + 1);
                }
            }
        }
    }


    public FTPClient ftpClientConnect() {
        if(ftpClient == null){
            ftpClient = new FTPClient();
        }else if(ftpClient.isConnected()){
            return ftpClient;
        }

        try {
            ftpClient.connect(ftpClientConfig.getFtpHost(),ftpClientConfig.getFtpPort());
            ftpClient.login(ftpClientConfig.getFtpUsername(), ftpClientConfig.getFtpPassword());
            ftpClient.setControlEncoding("UTF-8");
            // Configurações opcionais
            ftpClient.enterLocalPassiveMode(); // Modo passivo
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // Tipo de arquivo (binário é o mais comum para transferência de dados não-texto)
            ftpClient.setConnectTimeout(10000); // Timeout de conexão em milissegundos
        } catch (IOException ex) {
            ex.printStackTrace();
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
        }
    }

}
