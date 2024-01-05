package br.com.rocketdevelopment.jfilesyncserver.service;

import java.io.*;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;


/*####################################################################
 *          Essa Classe Foi inspirada no tutorial do site:           *
 *  http://www.devmedia.com.br/desenvolvendo-um-cliente-ftp/3547     *
 *####################################################################
 */


/**
 * Classe criada para trabalhar com FTP.
 * @author Felipe Santaniello
 * @date 15/12/2015
 *
 */
@Service
public class FtpService {

    private FTPClient ftpClient;

    public FtpService(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
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
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(diretorio);
            nameDirs = ftpClient.listNames();
        } catch (IOException e) {
            e.printStackTrace();
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
        FTPFile[] filesConfig = null;
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(diretorio);
            filesConfig = ftpClient.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
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
            ftpClient.enterLocalPassiveMode();
            arqEnviar = new FileInputStream(caminhoArquivo);
            if (ftpClient.storeFile(arquivo, arqEnviar)) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
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

    /**
     * Fecha a conexão com o FTP.
     * @author Felipe Santaniello
     * @data 15/12/2015
     * @description Faz a desconexão com o FTP.
     * @return void
     *
     * */
    public void disconnectFTP() {
        try {
            this.ftpClient.logout();
            this.ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(FTPClient ftpClient,String remoteFilePath, String savePath) throws IOException {
        OutputStream outputStream1 = new FileOutputStream(savePath);
        boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream1);
        outputStream1.close();

        if (success) {
            System.out.println("File #1 has been downloaded successfully.");
        }
    }
    public void uploadFile(FTPClient ftpClient,String localFilePath, String remoteFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(localFilePath);
        System.out.println("Start uploading first file");
        boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
    }
    public void deleteFile(FTPClient ftpClient,String filePath) throws IOException {
        boolean deleted = ftpClient.deleteFile(filePath);
        if (deleted) {
            System.out.println("The file was deleted successfully.");
        }
    }


public void copyAllFIles(FTPClient ftpClient,String source, String destination) throws IOException {
        FTPFile[] files = ftpClient.listFiles(source);
        for (FTPFile file : files) {
            String dest = destination + "/" + file.getName();
            if (file.isDirectory()) {
                // cria diretório no destino
                ftpClient.makeDirectory(dest);
                // faz cópia dos arquivos e subpastas
                copyAllFIles(ftpClient,source + "/" + file.getName(), dest);
            } else {
                // copia arquivo
                InputStream is = ftpClient.retrieveFileStream(source + "/" + file.getName());
                OutputStream os = new FileOutputStream(dest);
                byte[] bytes = new byte[1024];
                int read = 0;
                while ((read = is.read(bytes)) != -1) {
                    os.write(bytes, 0, read);
                }
                is.close();
                os.close();
            }
        }
}




    public void listDirectory(FTPClient ftpClient, String parentDir,
                                      String currentDir, int level) throws IOException {
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
                    listDirectory(ftpClient, dirToList, currentFileName, level + 1);
                }
            }
        }
    }
}
