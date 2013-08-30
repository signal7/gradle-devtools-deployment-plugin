package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Project;

class DeploymentTomcatExtension {
	
	def deploymentRules = []

	def providedProjects = [];
	
	def warLibsDir;
	private Project project;
	
	
	DeploymentTomcatExtension(Project project){
		this.project = project;
		warLibsDir ="$project/dependencies"
	}
	
	void providedProject(Project project) {
		providedProjects << project
	}
	
	void rule(arg){
		deploymentRules << new DeploymentRule(arg.toDir, arg.fileSetDir)
	}

	void useDefaultRules(String base){
		deploymentRules << new DeploymentRule("$base/WEB-INF/lib",warLibsDir)
		deploymentRules << new DeploymentRule("$base/WEB-INF/classes", "bin")
		deploymentRules << new DeploymentRule("$base", "src/main/webapp")
	}
}