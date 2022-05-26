plugins {
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

val jqwikVersion = "1.6.4"
val hamcrestVersion = "2.2"
val junitVersion = "5.8.2"
val mockitoVersion = "3.+"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.slf4j:slf4j-log4j12:1.7.36")

    implementation("org.hibernate:hibernate-core:5.6.7.Final")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("org.postgresql:postgresql:42.3.3")

    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:postgresql:1.16.3")

    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
    testImplementation("net.jqwik:jqwik:${jqwikVersion}")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

tasks.test {
    useJUnitPlatform { includeEngines("junit-jupiter", "jqwik") }
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<Test> {
    jvmArgs("--enable-preview")
}
tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}