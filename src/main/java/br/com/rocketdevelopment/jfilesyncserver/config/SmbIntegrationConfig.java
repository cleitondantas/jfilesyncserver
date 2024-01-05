package br.com.rocketdevelopment.jfilesyncserver.config;

import jcifs.DialectVersion;
import jcifs.smb.SmbFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.smb.filters.SmbRegexPatternFileListFilter;
import org.springframework.integration.smb.inbound.SmbInboundFileSynchronizer;
import org.springframework.integration.smb.inbound.SmbInboundFileSynchronizingMessageSource;
import org.springframework.integration.smb.session.SmbSessionFactory;
import org.springframework.integration.smb.outbound.SmbMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class SmbIntegrationConfig {

    @Value("${smb.host}")
    private String smbHost;

    @Value("${smb.port}")
    private Integer smbPort;

    @Value("${smb.username}")
    private String smbUsername;

    @Value("${smb.password}")
    private String smbPassword;

    @Value("${smb.share-and-dir}")
    private String smbShareAndDir;

    @Value("${smb.remote-target-directory}")
    private String smbRemoteTargetDirectory;

    @Bean
    public MessageChannel smbOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public SmbSessionFactory smbSessionFactory() {
        SmbSessionFactory smbSessionFactory = new SmbSessionFactory();
        smbSessionFactory.setHost(smbHost);
        smbSessionFactory.setPort(smbPort);
        smbSessionFactory.setUsername(smbUsername);
        smbSessionFactory.setPassword(smbPassword);
        smbSessionFactory.setShareAndDir(smbShareAndDir);
        smbSessionFactory.setSmbMinVersion(DialectVersion.SMB210);
        smbSessionFactory.setSmbMaxVersion(DialectVersion.SMB311);
        return smbSessionFactory;
    }
//
//    @Bean
//    public SmbInboundFileSynchronizer smbInboundFileSynchronizer() {
//        SmbInboundFileSynchronizer fileSynchronizer = new SmbInboundFileSynchronizer(smbSessionFactory());
//        fileSynchronizer.setFilter(compositeFileListFilter());
//        fileSynchronizer.setRemoteDirectory("mySharedDirectoryPath");
//        fileSynchronizer.setDeleteRemoteFiles(true);
//        return fileSynchronizer;
//    }
//
//    @Bean
//    public CompositeFileListFilter<SmbFile> compositeFileListFilter() {
//        CompositeFileListFilter<SmbFile> filters = new CompositeFileListFilter<>();
//        filters.addFilter(new SmbRegexPatternFileListFilter("^(?i).+((\\.txt))$"));
//        return filters;
//    }
//
//
//
//    @Bean
//    @InboundChannelAdapter(value = "smbFileInputChannel", poller = @Poller(fixedDelay = "2000"))
//    public MessageSource<File> smbMessageSource() {
//        SmbInboundFileSynchronizingMessageSource messageSource = new SmbInboundFileSynchronizingMessageSource(smbInboundFileSynchronizer());
//        messageSource.setLocalDirectory(new File("myLocalDirectoryPath"));
//        messageSource.setAutoCreateLocalDirectory(true);
//        return messageSource;
//    }
}