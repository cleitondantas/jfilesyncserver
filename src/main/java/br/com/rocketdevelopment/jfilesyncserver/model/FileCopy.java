package br.com.rocketdevelopment.jfilesyncserver.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileCopy {
    private String pathFrom;
    private String pathTo;
    private String fileName;

}
