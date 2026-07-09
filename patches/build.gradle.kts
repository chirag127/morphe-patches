group = "app.morphe"

patches {
    about {
        name = "ajstrick81 Patches"
        description = "Paramount+ and Disney+ Android TV ad blocking patches"
        source = "https://github.com/ajstrick81/morphe-androidtv-patches"
        author = "ajstrick81"
        contact = "https://github.com/ajstrick81"
        website = "https://github.com/ajstrick81/morphe-androidtv-patches"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("app.morphe.util.PatchListGeneratorKt")
    }
    publish {
        dependsOn("generatePatchesList")
    }
}
