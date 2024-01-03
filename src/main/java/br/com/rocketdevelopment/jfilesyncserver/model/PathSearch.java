package br.com.rocketdevelopment.jfilesyncserver.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PathSearch {
    private String path;
    private String filename;
    private String extension;
    private boolean recursive;

    public PathSearch() {
    }

    public PathSearch(String path,String filename, String extension, boolean recursive) {
        this.path = path;
        this.extension = extension;
        this.recursive = recursive;
        this.filename = filename;
    }

}
