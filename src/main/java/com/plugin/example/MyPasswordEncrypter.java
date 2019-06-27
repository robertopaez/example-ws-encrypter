package com.plugin.example;

import com.dtolabs.rundeck.core.encrypter.PasswordUtilityEncrypter;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Property;
import com.dtolabs.rundeck.core.plugins.configuration.PropertyUtil;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(service= ServiceNameConstants.PasswordUtilityEncrypter,name="MyPasswordEncrypter")
@PluginDescription(title="MyPasswordEncrypter", description="MyPasswordEncrypter description")
public class MyPasswordEncrypter implements PasswordUtilityEncrypter {

    public static String OPTION_ENC_PATTERN="ENC\\([\\\"|\\'](.*)[\\\"|\\']\\)";

    StandardPBEStringEncryptor encryptor;

    public MyPasswordEncrypter() {
        CustomEncryptorProvider provider = new CustomEncryptorProvider();
        encryptor = provider.getEncryptor();
    }


    @Override
    public String name() {
        return "MyPasswordEncrypter";
    }

    @Override
    public Map encrypt(Map params) {
        String valToEncrypt = (String)params.get("value");
        Map<String,String> result = new HashMap();
        String encryptedValue = encryptor.encrypt(valToEncrypt);
        result.put("value","ENC('"+encryptedValue+"')");
        return result;
    }

    @Override
    public List<Property> formProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(PropertyUtil.string("value", "value", "value to encrypt", true, null));
        return properties ;
    }

    public String decrypt(String value) {
        Pattern p = Pattern.compile(OPTION_ENC_PATTERN);

        Matcher m = p.matcher(value);
        if(m.matches()){
            String decryptedString = encryptor.decrypt(m.group(1));
            if(decryptedString!=null){
                return decryptedString;
            }
        }
        return null ;
    }
}
