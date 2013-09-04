gradle-devtools-deployment-plugin
=================================

[![Build Status](https://travis-ci.org/signal7/gradle-devtools-deployment-plugin.png)](https://travis-ci.org/signal7/gradle-devtools-deployment-plugin)

Gradle plugin for eclipse devtools deployment plugin


##Usage

	apply plugin: "eclipse"
	apply plugin: "war"
	apply plugin: "devtools-deployment"
	
	ext{
		deploymentProjectBase = "shared"
		provideSharedLibsOutputFolder = "$buildDir/dependencies"
	}
	
	deploymentDescriptor {
		rule  toDir: "$deploymentBase/$deploymentProjectBase/lib", fileSetDir: project.relativePath(provideSharedLibsOutputFolder)
		rule  toDir: "$deploymentBase/$deploymentProjectBase/classes", fileSetDir: "bin"
	}
	
	