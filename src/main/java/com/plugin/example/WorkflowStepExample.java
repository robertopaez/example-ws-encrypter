package com.plugin.example;

import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.step.StepPlugin;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import com.dtolabs.rundeck.plugins.PluginLogger;
import java.util.Map;

@Plugin(service=ServiceNameConstants.WorkflowStep,name="example")
@PluginDescription(title="WorkflowStepExample", description="My WorkflowStep plugin description")
public class WorkflowStepExample implements StepPlugin, Describable{

    public static final String SERVICE_PROVIDER_NAME = "example";

   /**
     * Overriding this method gives the plugin a chance to take part in building the {@link
     * com.dtolabs.rundeck.core.plugins.configuration.Description} presented by this plugin.  This subclass can use the
     * {@link DescriptionBuilder} to modify all aspects of the description, add or remove properties, etc.
     */
   @Override
   public Description getDescription() {
        return DescriptionBuilder.builder()
            .name(SERVICE_PROVIDER_NAME)
            .title("WorkflowStepExample")
            .description("Example Workflow Step")
            .property(PropertyBuilder.builder()
                    .string("password")
                    .title("Password Value")
                    .description("Password description")
                    .required(true)
                    .build()
            )
            .property(PropertyBuilder.builder()
                          .booleanType("exampleBoolean")
                          .title("Example Boolean")
                          .description("Example Boolean?")
                          .required(false)
                          .defaultValue("false")
                          .build()
            )
            .property(PropertyBuilder.builder()
                          .freeSelect("ExampleFreeSelect")
                          .title("Example Free Select")
                          .description("Example Free Select")
                          .required(false)
                          .defaultValue("Blue")
                          .values("Blue", "Beige", "Black")
                          .build()
            )
            .build();
   }

   /**
     * This enum lists the known reasons this plugin might fail
     */
   static enum Reason implements FailureReason{
        ExampleReason
   }


   /**
     * Here is the meat of the plugin implementation, which should perform the appropriate logic for your plugin.
     * <p/>
     * The {@link PluginStepContext} provides access to the appropriate Nodes, the configuration of the plugin, and
     * details about the step number and context.
     */
   @Override
   public void executeStep(final PluginStepContext context, final Map<String, Object> configuration) throws
                                                                                                      StepException{
        PluginLogger logger= context.getLogger();
        logger.log(2,"Example step configuration: " + configuration);
        logger.log(2,"Example step num: " + context.getStepNumber());
        logger.log(2,"Example step context: " + context.getStepContext());

        String passwordEncypted = (String)configuration.get("password");
        logger.log(2, "Password ecrypted:" + passwordEncypted);


       MyPasswordEncrypter encryptor = new MyPasswordEncrypter();
       String password=encryptor.decrypt(passwordEncypted);

        logger.log(2, "Password decrypted:" + password);


        if ("true".equals(configuration.get("exampleBoolean"))) {
            throw new StepException("exampleBoolean was true", Reason.ExampleReason);
        }
   }

}
