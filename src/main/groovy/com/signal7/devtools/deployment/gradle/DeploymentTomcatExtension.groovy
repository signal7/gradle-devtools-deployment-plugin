package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Project;

class DeploymentTomcatExtension {

	def deploymentRules = []

	def providedProjects = [];

	def warLibsDir;
	private Project project;


	DeploymentTomcatExtension(Project project){
		this.project = project;
		warLibsDir = project.relativePath("$project.buildDir/dependencies")
	}

	void providedProject(Project project) {
		providedProjects << project
	}
	
	def rule(arg){

		def deployRule = new DeploymentRule(arg.toDir, arg.fileSetDir)
		if(arg.excludes){
			deployRule.excludes += arg.excludes
		}
		
		deploymentRules << deployRule
		
	}

	void useDefaultRules(String base){
		deploymentRules << new DeploymentRule("$base/WEB-INF/lib",warLibsDir)
		deploymentRules << new DeploymentRule("$base/WEB-INF/classes", "bin")
		deploymentRules << new DeploymentRule("$base", "src/main/webapp")
	}
}