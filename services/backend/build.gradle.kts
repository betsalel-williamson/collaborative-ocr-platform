plugins {
    application
    java
}

group = "com.prototype.ocr"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.12")
    // FoundationDB Java client for connectivity testing
    // Record Layer will be added in task 02_record-layer-schema
    implementation("org.foundationdb:fdb-java:7.3.27")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

application {
    mainClass.set("com.prototype.ocr.backend.BackendApplication")
}

tasks.test {
    useJUnitPlatform()
}

