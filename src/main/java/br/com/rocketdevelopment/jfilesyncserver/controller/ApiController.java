package br.com.rocketdevelopment.jfilesyncserver.controller;
import br.com.rocketdevelopment.jfilesyncserver.model.FileCopy;
import br.com.rocketdevelopment.jfilesyncserver.model.FileRecord;
import br.com.rocketdevelopment.jfilesyncserver.model.PathSearch;
import br.com.rocketdevelopment.jfilesyncserver.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ApiController {

    private FileManagerService fileManagerService;

    @Autowired
    public ApiController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping("/list")
    public List<String> list(@RequestBody PathSearch pathSearch)  {
        List<String> files = null;
        try {
            String path = pathSearch.getPath();
            files = fileManagerService.listFilesAndFolders(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }
    @PostMapping("/folderlist")
    public List<String> folderList(@RequestBody PathSearch pathSearch)  {
        List<String> files = null;
        try {
            String path = pathSearch.getPath();
            files =fileManagerService.folderList(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    @PostMapping("/fileslist")
    public ResponseEntity<List<String>> fileslist(@RequestBody PathSearch pathSearch)  {
        List<String> files = null;
        try {
            String path = pathSearch.getPath();
            files =fileManagerService.fileslist(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(files);
    }

    @PostMapping("/copy")
    public ResponseEntity<String> copy(@RequestBody FileCopy fileCopy)  {
        String hash = null;
        try {
            String sourceFilePath = fileCopy.getPathFrom()+fileCopy.getFileName();
            String destinationDirPath = fileCopy.getPathTo();
            Path copy =  fileManagerService.copy(sourceFilePath, destinationDirPath);
             hash = fileManagerService.hash(copy.toFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo copiado com sucesso %s ",hash));
    }

    @PostMapping("/copyall")
    public ResponseEntity<String> copyAllFiles(@RequestBody FileCopy fileCopy)  {
        try {
            String sourceFilePath = fileCopy.getPathFrom()+fileCopy.getFileName();
            String destinationDirPath = fileCopy.getPathTo();
            Path sourcePath = Paths.get(sourceFilePath);
            Path destination = Paths.get(destinationDirPath);
            fileManagerService.copyAllFiles(sourcePath, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo copiado com sucesso "));
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestBody FileCopy fileCopy)  {
        String hash = null;
        try {
            String sourceFilePath = fileCopy.getPathFrom()+fileCopy.getFileName();
            String destinationDirPath = fileCopy.getPathTo();
            Path moved =  fileManagerService.move(sourceFilePath, destinationDirPath);
            hash = fileManagerService.hash(moved.toFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo movido com sucesso %s ",hash));
    }

    @PostMapping("/moveall")
    public ResponseEntity<String> moveAllFiles(@RequestBody FileCopy fileCopy)  {
        try {
            String sourceFilePath = fileCopy.getPathFrom();
            String destinationDirPath = fileCopy.getPathTo();
            Path sourcePath = Paths.get(sourceFilePath);
            Path destination = Paths.get(destinationDirPath);
            fileManagerService.moveAllFiles(sourcePath, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo movido com sucesso "));
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody FileRecord fileRecord)  {
        boolean delete = false;
        String path = fileRecord.getFilePath()+fileRecord.getFileName();
        try {
             delete = fileManagerService.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo deletado is  %s ",delete));
    }

    @DeleteMapping("/pathdelete")
    public ResponseEntity<String> pathDelete(@RequestBody PathSearch pathSearch) {
        String path = pathSearch.getPath();
        try {
            fileManagerService.pathDelete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(String.format("Arquivo deletado is  %s ",path));
    }


}
