group = "app.chirag127.morphe"

patches {
    about {
        name = "chirag127 Morphe patches"
        description = "Device-detection removal patches for Android apps — Poco F7 / non-Pixel targets. Includes Pixel Studio unlock. GPLv3."
        source = "git@github.com:chirag127/morphe-patches.git"
        author = "chirag127"
        contact = "https://github.com/chirag127"
        website = "https://github.com/chirag127/morphe-patches"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath: Configuration by configurations.creating

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
