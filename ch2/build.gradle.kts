plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ch2"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.ch2"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    viewBinding.enable = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 라이브러리 등록 sync now 순간 다운로드된다.
    // group id (androidx.datastore) : artifact id (datastore-preferences) : version (1.2.0)

    // implementation(libs.androidx.appcompat)
    // 위에처럼 등록된 라이브러리는 하나의 프로젝트 내에 여러 모듈이 있을 수도 있다.
    // 동일 라이브러리에 동일 버전을 여러 모듈이 사용할 수 있다.
    // 아래처럼 선언하면.. 버전 변경시에 여러 모듈을 수정해야 한다는 불편함이 있다.
    // 프로젝트의 라이브러리를 통합 관리해서 버전을 한곳에서만 관리한다.
    // 모듈에서는 통합 관리된 라이브러리를 링크로만 걸어서 사용하자는 의미이다.
    // libs.versions.toml 파일을 제공한다. 라이브러리 버전 관리 파일이다.
    implementation("androidx.datastore:datastore-preferences:1.2.0")

    // 구글의 라이브러리를 추가할 때, 동일 이름으로 -ktx 가 추가된 라이브러리이다.
    // aaa라는 라이블러리가 있다고 가정하자
    // aaa : java로 만든 라이브러리
    // aaa-ktx : kotlin으로 만든 라이브러리
    // 코틀린에서 aaa인 java라이브러리를 사용해도 된다. 조금 불편하다.
    // java에서는 aaa-ktx는 사용하지 못한다.
    // 기왕 ktx로 코틀린 편하라고 지원하니까 ktx 라이브러리를 사용하자.
    implementation("androidx.preference:preference-ktx:1.2.1")
}