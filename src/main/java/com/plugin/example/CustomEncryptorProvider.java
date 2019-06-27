package com.plugin.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.security.Security;

public class CustomEncryptorProvider {

    public StandardPBEStringEncryptor getEncryptor() {
        Security.addProvider(new BouncyCastleProvider());

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setProviderName("BC");
        config.setPassword("masterpassword");

        encryptor.setConfig(config);

        return encryptor;

    }

}
