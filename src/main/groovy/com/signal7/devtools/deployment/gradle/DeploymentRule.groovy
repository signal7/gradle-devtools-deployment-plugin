package com.signal7.devtools.deployment.gradle;
class DeploymentRule {
	
	def toDir
	def fileSetDir
	def excludes = []

	DeploymentRule(String toDir, String fileSetDir){
		this.toDir = toDir
		this.fileSetDir = fileSetDir
	}
	def exclude(String path){
		excludes << path
	}
}