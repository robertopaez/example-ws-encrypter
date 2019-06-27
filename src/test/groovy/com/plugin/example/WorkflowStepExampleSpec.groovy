package com.plugin.example

import com.dtolabs.rundeck.plugins.step.PluginStepContext
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException
import com.dtolabs.rundeck.plugins.PluginLogger
import spock.lang.Specification

class WorkflowStepExampleSpec extends Specification {

    def getContext(PluginLogger logger){
        Mock(PluginStepContext){
            getLogger()>>logger
        }
    }

    def "check Boolean parameter"(){

        given:

        def example = new WorkflowStepExample()
        def context = getContext(Mock(PluginLogger))
        def configuration = [password:"example123",exampleBoolean:"true"]

        when:
        example.executeStep(context,configuration)

        then:
        thrown StepException
    }

    def "run OK"(){

        given:

        def example = new WorkflowStepExample()
        def logger = Mock(PluginLogger)
        def context = getContext(logger)
        def configuration = [password:"example123",exampleBoolean:"false",exampleFreeSelect:"Beige"]

        when:
        example.executeStep(context,configuration)

        then:
        1 * logger.log(2, 'Example step configuration: {password=example123, exampleBoolean=false, exampleFreeSelect=Beige}')
    }

}
