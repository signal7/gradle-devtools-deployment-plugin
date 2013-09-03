package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Delete;

class EclipseDeploymentPlugin implements Plugin<Project>{

	void apply(Project project){

		project.extensions.create("deploymentDescriptor", DeploymentTomcatExtension, project)
		def outputFile = project.file(".deployment")

		def	outputDeploymentFileTask = project.task("outputDeploymentFile") << {
			def writer = new StringWriter()
			def xml = new groovy.xml.MarkupBuilder(writer)
			xml.setDoubleQuotes(true)
			xml.deployment(){
				project.deploymentDescriptor.deploymentRules.each{deploymentRule ->
					deploy(toDir : deploymentRule.toDir){
						fileSet(dir : deploymentRule.fileSetDir)
					}
				}
			}
			outputFile.text=writer.toString()
		}
		project.tasks.eclipse.dependsOn(outputDeploymentFileTask)
		outputDeploymentFileTask.outputs.file outputFile



		def provideWarLibsTask = project.task("provideWarLibs")<<{

			def warLibs = project.configurations.runtime - project.configurations.providedRuntime

			project.deploymentDescriptor.providedProjects.each { p ->
				warLibs -= p.tasks["jar"].outputs.files
			}
			project.copy {
				from warLibs
				into project.deploymentDescriptor.warLibsDir
			}
		}
		provideWarLibsTask.outputs.dir project.deploymentDescriptor.warLibsDir
		project.tasks.provideWarLibs.dependsOn(project.tasks.war)

		project.task("cleanWarLibs", type: Delete){ delete provideWarLibsTask }

		// fixme
		outputDeploymentFileTask.outputs.upToDateWhen {false}
	}
}
