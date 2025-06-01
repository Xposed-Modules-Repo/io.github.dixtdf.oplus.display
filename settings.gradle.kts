pluginManagement {
    repositories {
        maven(url = "https://mirrors.huaweicloud.com/repository/maven/")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://mirrors.huaweicloud.com/repository/maven/")
        google()
        mavenCentral()
    }
}

rootProject.name = "allow-display-connect"
include(":app")
 