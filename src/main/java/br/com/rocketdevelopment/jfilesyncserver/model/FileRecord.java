package br.com.rocketdevelopment.jfilesyncserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRecord extends FileItem{
    private Long id;
    private boolean isDeleted;
    public FileRecord() {
    }


}
