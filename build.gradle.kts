import java.util.Optional

// Plugins
plugins {
    kotlin("jvm") version "2.2.10"
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin")
}

// Repositories
repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.shedaniel.me/")
    maven( "https://maven.terraformersmc.com/releases/")
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }
}

val env = Env(project)
val mod = ModProperties(project)
val modFabric = ModFabric(project)
val modForge = ModForge(project)

version = "${mod.version}+${env.mcVersion.min}+${env.loader}"
group = property("group").toString()

stonecutter {
    constants {
        set("fabric", env.isFabric)
        set("forge", env.isForge)
        set("neoforge", env.isNeo)
    }
    replacements.string {
        direction = eval(current.version, "<=1.18.2")
        replace("RegisterParticleProvidersEvent", "ParticleFactoryRegisterEvent")
    }
}

loom {
    silentMojangMappingsLicense()

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    // Forge Mixin
    if (env.isForge) {
        forge {
            mixinConfigs("mixins.haybale.json", "mixins.haybale.forge.json")
        }
    }

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}
base { archivesName.set(env.archivesBaseName) }

/** Dependencies **/
class API(
    val group: String,
    val module: String,
    val version: VersionRange,
    val exclude: String = "",
    val loader: String = ""
)
val apis: Array<API> = arrayOf(
    API("com.terraformersmc", "modmenu", versionProperty("deps.api.mod_menu"), "net.fabricmc", "fabric"),
    API("me.shedaniel.cloth", "cloth-config-${env.loader}", versionProperty("deps.api.cloth_config"), "net.fabricmc")
)

dependencies {
    minecraft("com.mojang:minecraft:${env.mcVersion.min}")
    mappings(loom.officialMojangMappings())
    // Base Dependencies
    if(env.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${modFabric.loaderVersion}")
        modApi("net.fabricmc.fabric-api:fabric-api:${modFabric.version}")
    }
    if(env.isForge) {
        "forge"("net.minecraftforge:forge:${modForge.version}")
    }
    if(env.isNeo) {
        "neoForge"("net.neoforged:neoforge:${modForge.version}")
    }
    // Shadow ini4j
    implementation("org.ini4j:ini4j:0.5.4")
    include("org.ini4j:ini4j:0.5.4")
    if (env.isForge || env.isNeo) {
        "forgeRuntimeLibrary"("org.ini4j:ini4j:0.5.4")
    }
    // Decompiler
    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
    // APIs
    apis.forEach {
        if (it.loader.isBlank() || it.loader == env.loader) {
            if (it.exclude.isBlank()) {
                modApi("${it.group}:${it.module}:${it.version.min}")
            } else {
                modApi("${it.group}:${it.module}:${it.version.min}") {
                    exclude(group = it.exclude)
                }
            }
        }
    }
}

java {
    withSourcesJar()
    val java = if(env.javaVer == 8) JavaVersion.VERSION_1_8 else if(env.javaVer == 17) JavaVersion.VERSION_17 else JavaVersion.VERSION_21
    targetCompatibility = java
    sourceCompatibility = java
}

/**
 * Replaces the normal copy task and post-processes the files.
 * Effectively renames datapack directories due to depluralization past 1.20.4.
 */
abstract class ProcessResourcesExtension : ProcessResources() {
    @get:Input
    val autoPluralize = arrayListOf(
        "/data/minecraft/tags/block",
        "/data/minecraft/tags/item",
        "/data/haybale/loot_table",
        "/data/haybale/recipe",
        "/data/haybale/tags/item",
    )
    override fun copy() {
        super.copy()
        val root = destinationDir.absolutePath
        autoPluralize.forEach { path ->
            val file = File(root.plus(path))
            if(file.exists()){
                file.copyRecursively(File(file.absolutePath.plus("s")),true)
                file.deleteRecursively()
            }
        }
    }
}

if(env.atMost("1.20.6")){
    tasks.replace("processResources",ProcessResourcesExtension::class)
}

tasks.processResources {
    dependsOn("stonecutterGenerate")

    val map = mapOf(
        "modid" to mod.id,
        "id" to mod.id,
        "name" to mod.displayName,
        "display_name" to mod.displayName,
        "version" to mod.version,
        "description" to mod.description,
        "authors" to mod.authors,
        "github_url" to mod.sourceUrl,
        "source_url" to mod.sourceUrl,
        "icon" to mod.icon,
        "mc_min" to env.mcVersion.min,
        "mc_max" to env.mcVersion.max,
        "issue_tracker" to mod.issueTracker,
        "java_ver" to env.javaVer.toString(),
        "loader_id" to env.loader,
        "license" to mod.license,
        "forgelike_loader_ver" to modForge.loaderVersion,
    )
    map.forEach{ (key, value) ->
        inputs.property(key,value)
    }
    filesMatching("pack.mcmeta") { expand(map) }
    filesMatching("fabric.mod.json") { expand(map) }
    filesMatching("META-INF/mods.toml") { expand(map) }
    filesMatching("META-INF/neoforge.mods.toml") { expand(map) }
    filesMatching("META-INF/services/**") { expand(mapOf(
        "loader" to env.loader
    )) }
}

//TODO: Enable auto-publishing.
/**
 * Controls publishing. For publishing to work dryRunMode must be false.
 * Modrinth and Curseforge project tokens are publicly accessible, so it is safe to include them in files.
 * Do not include your API keys in your project!
 *
 * The Modrinth API token should be stored in the MODRINTH_TOKEN environment variable.
 * The curseforge API token should be stored in the CURSEFORGE_TOKEN environment variable.
 */
class ModPublish {
    val mcTargets = listProperty("publish_acceptable_mc_versions")
    val modrinthProjectToken = property("publish.token.modrinth").toString()
    val curseforgeProjectToken = property("publish.token.curseforge").toString()
    val dryRunMode = findProperty("publish.dry_run")
}

val modPublish = ModPublish()
/*publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${mod.displayName} ${mod.version} for ${env.mcVersion.min}"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add(env.loader)

    dryRun = modPublish.dryRunMode

    modrinth {
        projectId = modPublish.modrinthProjectToken
        // Get one here: https://modrinth.com/settings/pats, enable read, write, and create Versions ONLY!
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)
        apis.forEach{ src ->
            if(src.enabled) src.versionRange.ifPresent{ ver ->
                if(src.type.isOptional()){
                    src.modInfo.rinthSlug?.let {
                        optional {
                            slug = it
                            version = ver.min

                        }
                    }
                }
                else{
                    src.modInfo.rinthSlug?.let {
                        requires {
                            slug = it
                            version = ver.min
                        }
                    }
                }
            }
        }
    }

    curseforge {
        projectId = modPublish.curseforgeProjectToken
        // Get one here: https://legacy.curseforge.com/account/api-tokens
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)
        apis.forEach{ src ->
            if(src.enabled) src.versionRange.ifPresent{ ver ->
                if(src.type.isOptional()){
                    src.modInfo.curseSlug?.let {
                        optional {
                            slug = it
                            version = ver.min

                        }
                    }
                }
                else{
                    src.modInfo.curseSlug?.let {
                        requires {
                            slug = it
                            version = ver.min
                        }
                    }
                }
            }
        }
    }
}