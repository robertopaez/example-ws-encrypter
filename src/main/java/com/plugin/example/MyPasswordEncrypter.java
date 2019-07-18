package com.plugin.example;

import com.dtolabs.rundeck.core.encrypter.PasswordUtilityEncrypterPlugin;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.core.plugins.configuration.Property;
import com.dtolabs.rundeck.core.plugins.configuration.PropertyUtil;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(service = ServiceNameConstants.PasswordUtilityEncrypter, name = MyPasswordEncrypter.PROVIDER_NAME)
@PluginDescription(title = MyPasswordEncrypter.PROVIDER_TITLE, description = MyPasswordEncrypter.PROVIDER_DESCRIPTION)
/**
 * new PasswordUtilityEncrypter plugin, will provide the encrypt function on the rundeck GUI (Password Utility page)
 */
public class MyPasswordEncrypter implements PasswordUtilityEncrypterPlugin, Describable {

    public static String OPTION_ENC_PATTERN = "ENC\\([\\\"|\\'](.*)[\\\"|\\']\\)";
    public static final String PROVIDER_NAME="MyPasswordEncrypterTest";
    public static final String PROVIDER_TITLE="MyPasswordEncrypter";
    public static final String PROVIDER_DESCRIPTION="MyPasswordEncrypter TEst";


    static Description DESCRIPTION = DescriptionBuilder.builder()
            .name(PROVIDER_NAME)
            .title(PROVIDER_TITLE)
            .description(PROVIDER_DESCRIPTION)
            .property(PropertyUtil.string("value", "value", "value to encrypt", true, null))
            .property(PropertyBuilder.builder()
                          .booleanType("exampleBoolean")
                          .title("Example Boolean")
                          .description("Example Boolean?")
                          .required(false)
                          .defaultValue("false")
                          .build()
            )
            .property(PropertyBuilder.builder()
                          .freeSelect("exampleFreeSelect")
                          .title("Example Free Select")
                          .description("Example Free Select")
                          .required(false)
                          .defaultValue("Blue")
                          .values("Blue", "Beige", "Black")
                          .build()
            )

            .build();


    StandardPBEStringEncryptor encryptor;

    public MyPasswordEncrypter() {
        //instance my custom provider with will encrypt/decypt the password
        CustomEncryptorProvider provider = new CustomEncryptorProvider();
        encryptor = provider.getEncryptor();
    }

    /**
     * will perform the encrypt process based on the parameters set on formProperties()
     * The {@link config} map of parametes passing form gue GUI. the available keys are the one set on formProperties() (on this case the only one enable is called "value")
     */
    @Override
    public Map encrypt(Map config) {
        //get the value set on the GUI form
        String valToEncrypt = (String) config.get("value");

        String encryptedValue = encryptor.encrypt(valToEncrypt);

        //this will be returned to the page, this map will be printed on the GUI
        Map<String, String> result = new HashMap();
        result.put("original", valToEncrypt);
        result.put("encrypted", "ENC('" + encryptedValue + "')");

        return result;
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

    @Override
    public Description getDescription() {
        return DESCRIPTION;
    }
}
