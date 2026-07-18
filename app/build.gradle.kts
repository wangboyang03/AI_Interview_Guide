plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "cn.itcast.ai_interview_guide"
  compileSdk {
    version = release(37) {
      minorApiLevel = 1
    }
  }

  defaultConfig {
    applicationId = "cn.itcast.ai_interview_guide"
    minSdk = 23
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      optimization {
        enable = false
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  /** material3图标库 **/
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.material.icons.extended)

  /** Navigation **/
  implementation(libs.navigation.compose)

  /** MVVM **/
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  /** 网络请求相关 **/
  implementation(libs.retrofit)
  implementation(libs.retrofit.kotlinx.serialization)
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging)
  implementation(libs.kotlinx.serialization.json)

  /** 持久化存储 **/
  implementation(libs.datastore.preferences)

  /** Room **/
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  // ksp(libs.room.compiler)  // KSP 在编译时生成 DAO 实现类
}