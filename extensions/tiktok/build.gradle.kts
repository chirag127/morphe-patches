dependencies {
    compileOnly(project(":extensions:shared:library"))
    compileOnly(project(":extensions:tiktok:stub"))
    compileOnly(libs.annotation)
}

extension {
    name = "extensions/tiktok.mpe"
}

android {
    namespace = "app.morphe.extension.tiktok"

    defaultConfig {
        minSdk = 22
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

