plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

val jqwikVersion = "1.3.5"
val hamcrestVersion = "2.2"
val junitVersion = "5.7.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
    testImplementation("net.jqwik:jqwik:${jqwikVersion}")
}

tasks.test {
    useJUnitPlatform { includeEngines("jqwik") }
    testLogging {
        events("passed", "skipped", "failed")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_15
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