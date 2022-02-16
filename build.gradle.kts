plugins {
    java
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
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
    testImplementation("net.jqwik:jqwik:${jqwikVersion}")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
}

tasks.test {
    useJUnitPlatform { includeEngines("junit-jupiter", "jqwik") }
    testLogging {
        events("passed", "skipped", "failed")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_17
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