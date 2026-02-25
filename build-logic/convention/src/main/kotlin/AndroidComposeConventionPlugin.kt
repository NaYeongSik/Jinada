import com.android.build.api.dsl.CommonExtension
import internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType(CommonExtension::class.java)
            extension.apply {
                buildFeatures.apply {
                    compose = true
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))

                add("implementation", libs.findBundle("compose.runtime").get())
                add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
                add("androidTestImplementation", libs.findBundle("compose.androidTest").get())
            }
        }
    }
}
