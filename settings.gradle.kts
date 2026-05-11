pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Namma-Mela"
include(":app")
include(":core")
include(":feature:splash")
include(":feature:home")
include(":feature:showdetail")
include(":feature:seatmap")
include(":feature:booking")
include(":feature:studio")
include(":feature:fanwall")
include(":feature:profile")
