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

@Plugin(service = ServiceNameConstants.PasswordUtilityEncrypter, name = "MyPasswordEncrypter")
@PluginDescription(title = "MyPasswordEncrypter", description = "MyPasswordEncrypter description")
/**
 * new PasswordUtilityEncrypter plugin, will provide the encrypt function on the rundeck GUI (Password Utility page)
 */
public class MyPasswordEncrypter implements PasswordUtilityEncrypter {

    public static String OPTION_ENC_PATTERN = "ENC\\([\\\"|\\'](.*)[\\\"|\\']\\)";

    StandardPBEStringEncryptor encryptor;

    public MyPasswordEncrypter() {
        //instance my custom provider with will encrypt/decypt the password
        CustomEncryptorProvider provider = new CustomEncryptorProvider();
        encryptor = provider.getEncryptor();
    }


    /**
     * define the name of the encrypter displayed on the GUI
     */
    @Override
    public String name() {
        return "MyPasswordEncrypter";
    }

    /**
     * will perform the encrypt process based on the parameters set on formProperties()
     * The {@link params} map of parametes passing form gue GUI. the available keys are the one set on formProperties() (on this case the only one enable is called "value")
     */
    @Override
    public Map encrypt(Map params) {
        //get the value set on the GUI form
        String valToEncrypt = (String) params.get("value");

        String encryptedValue = encryptor.encrypt(valToEncrypt);

        //this will be returned to the page, this map will be printed on the GUI
        Map<String, String> result = new HashMap();
        result.put("value", "ENC('" + encryptedValue + "')");
        return result;
    }

    /**
     * the list of input values displayed on  the GUI
     * it could be a input text, select, free select, text area, etc.
     */
    @Override
    public List<Property> formProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(PropertyUtil.string("value", "value", "value to encrypt", true, null));
        return properties;
    }

    /**
     * this is not exposed with the plugin , we are using it to decrpyt the value from the worflow step that we added on this example
     */
    public String decrypt(String value) {
        Pattern p = Pattern.compile(OPTION_ENC_PATTERN);

        Matcher m = p.matcher(value);
        if (m.matches()) {
            String decryptedString = encryptor.decrypt(m.group(1));
            if (decryptedString != null) {
                return decryptedString;
            }
        }
        return null;
    }
}
