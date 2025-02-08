plugins {
    id("java")
    `maven-publish`
}

group = "fr.flylonyx"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "fr.flylonyx"
            artifactId = "JDORM"
            version = "1.0.0"

            pom {
                name.set("JDORM")
                description.set("Une bibliothèque Java")
                url.set("https://github.com/FlyLonyx/JDORM-library")
            }
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/FlyLonyx/JDORM-library")
            credentials {
                // Vérifiez si les variables sont nulles avant d'appeler toString
                val user = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                val token = project.findProperty("gpr.token") ?: System.getenv("MY_GITHUB_TOKEN")

                if (user == null || token == null) {
                    throw GradleException("GitHub username or token not set!")
                }

                username = user.toString()
                password = token.toString()
            }
        }
    }
}
