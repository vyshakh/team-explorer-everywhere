plugins {
    java
}

sourceSets["main"].java.srcDir(file("src"))

dependencies {
    compile(project(":source:com.microsoft.tfs.util"))
    compile(project(":source:com.microsoft.tfs.logging"))
    compile(project(":source:com.microsoft.tfs.jni"))
    compile(project(":source:com.microsoft.tfs.console"))
    compile(project(":source:com.microsoft.tfs.core.httpclient"))
    compile(project(":source:com.microsoft.tfs.core.ws.runtime"))
    compile(project(":source:com.microsoft.tfs.core.ws"))
    compile(project(":source:com.microsoft.tfs.client.common"))
}
