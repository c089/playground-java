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

val jqwikVersion = "1.6.5"
val junitVersion = "5.8.2"
val mockitoVersion = "3.+"
val assertJversion = "3.23.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:jul-to-slf4j:1.7.36")
    implementation("org.springframework.data:spring-data-jpa:2.7.0")
    implementation("com.sparkjava:spark-core:2.9.3")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    runtimeOnly("org.slf4j:slf4j-log4j12:1.7.36")


    implementation("org.hibernate:hibernate-core:5.6.7.Final")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("org.postgresql:postgresql:42.3.3")

    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:postgresql:1.16.3")

    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testImplementation("org.assertj:assertj-core:${assertJversion}")
    testImplementation("net.jqwik:jqwik:${jqwikVersion}")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("io.rest-assured:rest-assured:5.1.0")
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