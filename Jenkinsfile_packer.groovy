node {
	properties(
		[parameters(
			[choice(choices: 
			[
				'golden_image', 
				'vault', 
				'elk', 
				'gitlab',
				'source_ami_filter',
				'jenkins'],
		description: 'What would you like to build? ', 
		name: 'TOOL'), 
			choice(choices: 
			[
			'us-east-1', 
			'us-east-2', 
			'us-west-1', 
			'us-west-2', 
			'eu-west-1', 
			'eu-west-2', 
			'eu-central-1'], 
		description: 'Where would you like to build? ', 
		name: 'REGION')])])
		
	stage("Checkout SCM"){
		timestamps {
			ws {
				echo "Slack"
			checkout([$class: 'GitSCM', branches: [[name: 'main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/aotdevops/packer.git']]])
		}
	}
}
	stage("Run Packer"){
		timestamps {
			ws {
				sh "packer version"
				sh "packer build --var region=${REGION} tools/${TOOL}.json"
		}
	}
}
	stage("Send slack notifications"){
		timestamps {
			ws {
				echo "Slack"
				//slackSend color: '#BADA55', message: 'Hello, World!'
			}
		}
	}
}
