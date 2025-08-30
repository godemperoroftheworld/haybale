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
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }}

/* Helpers */

fun bool(str: String) : Boolean {
    return str.lowercase().startsWith("t")
}

fun boolProperty(key: String) : Boolean {
    if(!hasProperty(key)){
        return false
    }
    return bool(property(key).toString())
}

fun listProperty(key: String) : ArrayList<String> {
    if(!hasProperty(key)){
        return arrayListOf()
    }
    val str = property(key).toString()
    if(str == "UNSET"){
        return arrayListOf()
    }
    return ArrayList(str.split(" "))
}

fun optionalStrProperty(key: String) : Optional<String> {
    if(!hasProperty(key)){
        return Optional.empty()
    }
    val str = property(key).toString()
    if(str =="UNSET"){
        return Optional.empty()
    }
    return Optional.of(str)
}

class VersionRange(val min: String, val max: String){
    fun asForgelike() : String{
        return "${if(min.isEmpty()) "(" else "["}${min},${max}${if(max.isEmpty()) ")" else "]"}"
    }
    fun asFabric() : String{
        var out = ""
        if(min.isNotEmpty()){
            out += ">=$min"
        }
        if(max.isNotEmpty()){
            if(out.isNotEmpty()){
                out += " "
            }
            out += "<=$max"
        }
        return out
    }
}
fun versionProperty(key: String) : VersionRange {
    if(!hasProperty(key)){
        return VersionRange("","")
    }
    val list = listProperty(key)
    for (i in 0 until list.size) {
        if(list[i] == "UNSET"){
            list[i] = ""
        }
    }
    return if(list.isEmpty()){
        VersionRange("","")
    }
    else if(list.size == 1) {
        VersionRange(list[0],"")
    }
    else{
        VersionRange(list[0], list[1])
    }
}
fun optionalVersionProperty(key: String) : Optional<VersionRange>{
    val str = optionalStrProperty(key)
    if(!hasProperty(key)){
        return Optional.empty()
    }
    if(!str.isPresent){
        return Optional.empty()
    }
    return Optional.of(versionProperty(key))
}

/**
 * Stores core dependency and environment information.
 */
class Env {
    val archivesBaseName = property("archives_base_name").toString()

    val mcVersion = versionProperty("deps.core.mc.version_range")

    val loader = property("loom.platform").toString()
    val isFabric = loader == "fabric"
    val isForge = loader == "forge"
    val isNeo = loader == "neoforge"

    val javaVer = if(atMost("1.16.5")) 8 else if(atMost("1.20.4")) 17 else 21

    fun atLeast(version: String) = stonecutter.compare(mcVersion.min, version) >= 0
    fun atMost(version: String) = stonecutter.compare(mcVersion.min, version) <= 0
    fun isNot(version: String) = stonecutter.compare(mcVersion.min, version) != 0
    fun isExact(version: String) = stonecutter.compare(mcVersion.min, version) == 0
}
val env = Env()

// Stores information about the mod itself.
class ModProperties {
    val id = property("mod.id").toString()
    val displayName = property("mod.display_name").toString()
    val version = property("version").toString()
    val description = property("mod.description").toString()
    val authors = property("mod.authors").toString()
    val icon = property("mod.icon").toString()
    val issueTracker = property("mod.issue_tracker").toString()
    val license = property("mod.license").toString()
    val sourceUrl = property("mod.source_url").toString()
}
class ModFabric {
    val loaderVersion = versionProperty("deps.core.fabric.loader.version_range").min;
    val version = versionProperty("deps.core.fabric.version_range").min;
}
class ModForge {
    val loaderVersion = versionProperty("deps.core.forge.loader.version_range").min;
    val version = versionProperty("deps.core.forge.version_range").min;
}
val mod = ModProperties()
val modFabric = ModFabric()
val modForge = ModForge()

version = "${mod.version}+${env.mcVersion.min}+${env.loader}"
group = property("group").toString()

stonecutter.const("fabric",env.isFabric)
stonecutter.const("forge",env.isForge)
stonecutter.const("neoforge",env.isNeo)

stonecutter {
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

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}
base { archivesName.set(env.archivesBaseName) }

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

    // Extra Dependencies
    if (env.isFabric) {
        modApi("com.terraformersmc:modmenu:${property("deps.api.mod_menu")}") {
            exclude(group = "net.fabricmc")
        }
    }
    modApi("me.shedaniel.cloth:cloth-config-${env.loader}:${property("deps.api.cloth_config")}") {
        exclude(group = "net.fabricmc")
    }
    implementation("org.ini4j:ini4j:0.5.4")
    include("org.ini4j:ini4j:0.5.4")
    if (env.isForge || env.isNeo) {
        "forgeRuntimeLibrary"("org.ini4j:ini4j:0.5.4")
    }

    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
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

    val map = mapOf<String,String>(
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
    val mcTargets = arrayListOf<String>()
    val modrinthProjectToken = property("publish.token.modrinth").toString()
    val curseforgeProjectToken = property("publish.token.curseforge").toString()
    val dryRunMode = findProperty("publish.dry_run")

    init {
        val tempmcTargets = listProperty("publish_acceptable_mc_versions")
        if(tempmcTargets.isEmpty()){
            mcTargets.add(env.mcVersion.min)
        }
        else{
            mcTargets.addAll(tempmcTargets)
        }
    }
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