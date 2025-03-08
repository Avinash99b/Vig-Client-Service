plugins {
    id("java")
    kotlin("jvm")
    application
}

group = "com.avinash"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib")) // Ensure Kotlin Standard Library is included


    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}
application {
    mainClass.set("com.avinash.SystemMonitorService")  // Change to your service main class
}

tasks.jar {
    archiveBaseName.set("service")
    archiveClassifier.set("")  // Remove "-SNAPSHOT"
    manifest {
        attributes["Main-Class"] = "com.avinash.SystemMonitorService"  // Update with your main class
    }

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

