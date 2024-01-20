package br.com.rocketdevelopment.jfilesyncserver.service;


import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileManagerService {


    /**
     * Método para listar todos os arquivos de um diretório
     * @param dirPath
     * @return
     * @throws IOException
     */
    public List<String> fileslist(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        return Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Método para listar todos os arquivos de um diretório com uma determinada extensão
     * @param dirPath
     * @param extension
     * @return
     * @throws IOException
     */
    public List<String> listFiles(String dirPath, String extension) throws IOException {
        Path path = Paths.get(dirPath);
        return Files.list(path)
                .filter(p -> p.getFileName().toString().endsWith(extension))
                .map(p -> p.getFileName().toString())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Método para listar todos os arquivos de um diretório
     * @param dirPath
     * @return
     * @throws IOException
     */
    public List<String> folderList(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        return Files.list(path)
                .filter(p -> p.toFile().isDirectory())
                .map(p -> p.getFileName().toString())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Método para copiar um arquivo para um determinado diretório
     * @param sourceFilePath
     * @param destinationDirPath
     * @return
     * @throws IOException
     */
    public Path copy(String sourceFilePath, String destinationDirPath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationDirPath).resolve(sourcePath.getFileName());

        Path copy = Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return copy;
    }

    /**
     * Método para copiar todos os arquivos de um diretório para outro
     * @param sourceDirectory
     * @param targetDirectory
     * @throws IOException
     */
    public void copyAllFiles(Path sourceDirectory, Path targetDirectory) throws IOException {
        if (!Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDirectory)) {
            for (Path file: stream) {
                Path destFile = targetDirectory.resolve(sourceDirectory.relativize(file));
                if (Files.isDirectory(file)) {
                    copyAllFiles(file, destFile);
                } else {
                    Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }


    /** Método para deletar um arquivo
     * @param filePath
     * @return
     * @throws IOException
     */
    public boolean delete(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        boolean isFileDeleted = Files.deleteIfExists(path);
        return isFileDeleted;
    }

    /**
     * Método para deletar um diretório
     * @param dirPath
     * @throws IOException
     */
    public void pathDelete(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        Files.walk(path)
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Tratar exceções apropriadamente
                    }
                });
    }

    /**
     * Método para deletar um diretório
     * @param dirPath
     * @throws IOException
     */
    public void mkdir(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

   /**
     * Método para mover um arquivo para um determinado diretório
     * @param sourceFilePath
     * @param destinationDirPath
     * @throws IOException
     */
    public Path move(String sourceFilePath, String destinationDirPath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationDirPath).resolve(sourcePath.getFileName());

        Path move = Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return move;
    }

    /**
     * Método para mover todos os arquivos de um diretório para outro
     * @param sourceDirectory
     * @param targetDirectory
     * @throws IOException
     */
    public void moveAllFiles(Path sourceDirectory, Path targetDirectory) throws IOException {
        if (!Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDirectory)) {
            for (Path file: stream) {
                Path destFile = targetDirectory.resolve(sourceDirectory.relativize(file));
                if (Files.isDirectory(file)) {
                    moveAllFiles(file, destFile);
                } else {
                    Files.move(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * Método para renomear um arquivo
     * @param filePath
     * @param newFileName
     * @throws IOException
     */
    public void rename(String filePath, String newFileName) throws IOException {
        Path path = Paths.get(filePath);
        Path newPath = path.getParent().resolve(newFileName);
        Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Método para renomear um diretório
     * @param dirPath
     * @param newDirName
     * @throws IOException
     */
    public void renamePath(String dirPath, String newDirName) throws IOException {
        Path path = Paths.get(dirPath);
        Path newPath = path.getParent().resolve(newDirName);
        Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
    }



    /**
     * Método para criar o hash de um arquivo
     * @param filePath
     * @return
     */
    public String hash(String filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(filePath);
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            // Lê o arquivo e atualiza o digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            };

            fis.close();

            // Converte o resumo em hexadecimal
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para listar todos os arquivos e diretórios de um diretório
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> listFilesAndFolders(String path) throws IOException {
        Path filePath = Paths.get(path);
        try (Stream<Path> stream = Files.walk(filePath)) {
            return stream.map(Path::toString).collect(Collectors.toList());
        }
    }

    public void writeFileInputStream(String path, InputStream inputStream) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
        }
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
    public InputStream readFileInputStream(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found");
        }
        return Files.newInputStream(filePath);
    }
    public InputStream convertOutputStreamToInputStream(OutputStream outputStream) throws IOException {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;

        return new ByteArrayInputStream(baos.toByteArray());
    }
    public OutputStream convertInputStreamToOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos;
    }

}
