plugins {
    java
}

sourceSets["main"].java.srcDir(file("src"))

dependencies {
    compile("commons-logging:commons-logging:1.1.3")
}
