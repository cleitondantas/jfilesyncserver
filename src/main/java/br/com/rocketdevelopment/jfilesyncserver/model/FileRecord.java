package br.com.rocketdevelopment.jfilesyncserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRecord {
    private Long id;
    private String fileName;
    private String filePath;
    private String hash;
    private boolean isDeleted;
    public FileRecord() {
    }


}
