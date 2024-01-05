package br.com.rocketdevelopment.jfilesyncserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SmbService {

    @Autowired
    private MessageChannel smbOutputChannel;

    public void uploadFile(File file) {
        Message<File> message = MessageBuilder.withPayload(file)
                .setHeader("remote-target-directory", "destino/diretorio")
                .build();

        smbOutputChannel.send(message);
    }

}
