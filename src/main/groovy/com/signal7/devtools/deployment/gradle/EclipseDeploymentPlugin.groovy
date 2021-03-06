package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.WarPlugin;
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
						fileSet(dir : deploymentRule.fileSetDir){
							deploymentRule.excludes.each { name->
								exclude(name:name)
							}
						}
					}
				}
			}
			outputFile.text=writer.toString()
		}
		project.tasks.eclipse.dependsOn(outputDeploymentFileTask)
		
		outputDeploymentFileTask.outputs.file outputFile
		
		
		
		// fixme
		outputDeploymentFileTask.outputs.upToDateWhen {false}

		if (project.plugins.hasPlugin(WarPlugin)) {
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
		}

	}
}
