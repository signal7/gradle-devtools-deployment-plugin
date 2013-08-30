package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

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



		project.task("provideWarLibs", type:Copy){

			def warLibs = configurations.runtime - configurations.providedRuntime

			project.deploymentDescriptor.providedProjects.each { p ->
				warLibs -= p.tasks["jar"].outputs.files
			}
			from warLibs
			into project.deploymentDescriptor.warLibsDir
		}
		provideWarLibs.dependsOn(war)

		// fixme
		outputDeploymentFileTask.outputs.upToDateWhen {false}
	}
}
