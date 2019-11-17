plugins {
    java
}

sourceSets["main"].java.srcDir(file("src"))

dependencies {
    compile("commons-logging:commons-logging:1.1.3")
    compile(project(":source:com.microsoft.tfs.core"))
    compile(project(":source:com.microsoft.tfs.util"))

    implementation(files("../../.build/eclipse/eclipse/plugins/org.eclipse.equinox.common_3.5.1.R35x_v20090807-1100.jar"))
    implementation(files("../../.build/eclipse/eclipse/plugins/org.eclipse.core.jobs_3.4.100.v20090429-1800.jar"))
}