// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Файл верхнего уровня сборки, где задаются общие настройки для всех подпроектов/модулей.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.test) apply false
}

val currentJavaMajor = JavaVersion.current().majorVersion.toIntOrNull()
val supportedJavaMajors = 17..21

if (currentJavaMajor == null || currentJavaMajor !in supportedJavaMajors) {
    throw GradleException(
        """
        Unsupported Java runtime for this build: ${JavaVersion.current()}
        java.home=${System.getProperty("java.home")}

        Required Gradle JVM: JDK 17..21 (recommended: JDK 21).
        Using newer JDKs (for example JDK 24) can fail while creating AndroidUnitTest tasks
        with error: "Type T not present".

        Fix:
        - Android Studio: set Gradle JDK to JBR/JDK 21
        - CLI: set JAVA_HOME to JDK 21 before running Gradle
        """.trimIndent()
    )
}
