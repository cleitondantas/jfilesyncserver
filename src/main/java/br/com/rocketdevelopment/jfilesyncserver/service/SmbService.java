package br.com.rocketdevelopment.jfilesyncserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.smb.session.SmbSession;
import org.springframework.integration.smb.session.SmbSessionFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;

@Service
public class SmbService {


    @Autowired
    private SmbSessionFactory smbSessionFactory;



    public OutputStream downloadFile(String remoteFilePath) throws IOException {
        SmbSession smbSession = smbSessionFactory.getSession();
        OutputStream localOutputStream = new ByteArrayOutputStream();
        smbSession.read(remoteFilePath,localOutputStream);
        return localOutputStream;
    }

}
