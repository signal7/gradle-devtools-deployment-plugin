package com.signal7.devtools.deployment.gradle;

import org.gradle.api.Project;

class DeploymentTomcatExtension {
	
	def deploymentRules = []

	def providedProjects = [];
	
	def warLibsDir;
	
	
	DeploymentTomcatExtension(Project project){
		warLibsDir ="$project/dependencies"
	}
	
	void providedProject(Project project) {
		providedProjects << project
	}
	
	void rule(arg){
		deploymentRules << new DeploymentRule(arg.toDir, arg.fileSetDir)
	}

	void useDefaultRules(String base){
		deploymentRules << new DeploymentRule("$base/WEB-INF/lib","build/dependencies")
		deploymentRules << new DeploymentRule("$base/WEB-INF/classes", "bin")
		deploymentRules << new DeploymentRule("$base", "src/main/webapp")
	}
}