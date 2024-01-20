package br.com.rocketdevelopment.jfilesyncserver.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileItem {

    private String fileName;
    private String filePath;
    private long fileSize;
    private LocalDateTime creationTime;
    private LocalDateTime lastModifiedTime;
    private String fileExtension;
    private boolean isDirectory;
    private boolean hash;
}