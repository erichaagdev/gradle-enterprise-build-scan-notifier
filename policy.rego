package alerts

import future.keywords.contains
import future.keywords.if

messages := [
    ":wave: Hey! It looks you broke the build. :scream: That's okay, because we have Gradle Enterprise. :muscle: Here's the Build Scan!\n\n%s/failure",
    "Uh, oh! The build is broken. Go check out the Build Scan!\n\n%s/failure",
    "Oops. It looks like the build failed. Take a look at the Build Scan!\n\n%s/failure",
]

alerts contains alert if {
	input.attributes.environment.username == "ehaag"
	input.attributes.hasFailed

	alert := {
        "destination": "testing-server",
        "message": sprintf(messages[rand.intn("", count(messages))], [input.link])
    }
}

alerts contains alert if {
	input.build.buildToolType == "gradle"
	input.attributes.buildOptions.daemonEnabled == false

	alert := {
        "destination": "testing-server",
        "message": sprintf("Why do you have the Gradle daemon disabled? You should turn that back on.\n\n%s#switches\n\nhttps://docs.gradle.org/current/userguide/gradle_daemon.html", [input.link])
    }
}

alerts contains alert if {
	input.build.buildToolType == "gradle"
	input.buildToolVersion != "8.0.1"

	alert := {
        "destination": "testing-server",
        "message": concat(" ", ["Did you know Gradle 8.0.1 is available? You should upgrade! You're still on", input.buildToolVersion])
    }
}
