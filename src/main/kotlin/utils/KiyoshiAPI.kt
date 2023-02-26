@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.awt.Color
import java.time.Instant
import java.time.temporal.TemporalAccessor
import java.util.*
import kotlin.io.path.*
import kotlin.system.exitProcess


class KiyoshiAPI {

    fun insertValueInFile(fileName: String, value: String) {
        val file = Path("resources/$fileName")
        if (file.exists()) {
            try {
                if(file.readText().contains(value)) {
                    println("[Justin] Value already exists in file!")
                } else {
                    file.appendText("$value\n")
                    println("[Justin] Value $value inserted in file!")
                }
            } catch (e: Exception) {
                println("[Justin] Error while inserting value in file: $e")
            }
        }
    }

    fun insertIntoJsonFile(fileName: String, key: String, value: String) {
        val file = Path("resources/$fileName")
        if (file.exists()) {
            try {
                if(file.readText().contains(key)) {
                    println("[Justin] Value already exists in file!")
                } else {
                    file.appendText("\"$key\": \"$value\",\n")
                    println("[Justin] Value :$value inserted in file as $key!")
                }
            } catch (e: Exception) {
                println("[Justin] Error while inserting value in file: $e")
            }
        }
    }

    fun insertCommentIntoJsonFile(fileName: String, comment: String) {
        val file = Path("resources/$fileName")
        if (file.exists()) {
            try {
                file.appendText("// $comment\n")
                println("[Justin] Comment $comment inserted in file!")
            } catch (e: Exception) {
                println("[Justin] Error while inserting comment in file: $e")
            }
        }
    }

    fun createCustomFile(fileName: String) {
        val file = Path("resources/$fileName")
        if (!file.exists()) {
            file.createFile()
            insertValueInFile(".env", "TOKEN=INSERT-THE-TOKEN-HERE")
            insertCommentIntoJsonFile("config.json", "This is the config file for Justin, please do not edit this file unless you know what you are doing.")
            insertCommentIntoJsonFile("config.json", "If you want to add a new config value, please use the insertIntoJsonFile function in the KiyoshiAPI class.")
            insertCommentIntoJsonFile("config.json", " - Generic Section - ")
            insertIntoJsonFile("config.json", "GUILDID", "10246295255518562876")
            insertIntoJsonFile("config.json", "LOGCHANNELID", "1052218550932416582")
            insertIntoJsonFile("config.json", "WEBSITE", "https://kiyoshi.space")
            insertIntoJsonFile("config.json", "JUSTINCOLOR", "0x9586db")
            insertIntoJsonFile("config.json", "PASSWORD", "examplepassword")
            insertIntoJsonFile("config.json", "OWNERID", "1023379955987373066")
            insertIntoJsonFile("config.json", "STAFFID", "1025130559689324584")
            insertIntoJsonFile("config.json", "ANNOUNCEMENTCHANNELID", "1038455329686876200")
            insertIntoJsonFile("config.json", "DISCORDSERVER", "https://discord.gg/ka7uGKfy7p")
            insertIntoJsonFile("config.json", "EVERYONEID", "10246295264448562876")
            insertIntoJsonFile("config.json", "SERVERIMAGE", "https://i.imgur.com/sqdmuzV.png")
            insertCommentIntoJsonFile("config.json", " - MySQL Section - ")
            insertIntoJsonFile("config.json", "HOST", "localhost")
            insertIntoJsonFile("config.json", "PORT", "3306")
            insertIntoJsonFile("config.json", "DATABASE", "exampledatabase")
            insertIntoJsonFile("config.json", "USERNAME", "exampleusername")
            insertIntoJsonFile("config.json", "PASSWORDMYSQL", "examplepassword")
        }
    }

    fun getFileValueReadText(fileName: String): String {
        val file = Path("resources/$fileName")
        if (file.exists()) {
            return file.readText()
        }
        return ""
    }

    fun createResourcesFolder() {
        val resourcesFolder = Path("resources")
        if (!resourcesFolder.exists()) {
            resourcesFolder.createDirectory()
            createCustomFile(".env")
            createCustomFile("config.json")
        }
    }






    fun getEnv(value: String): String {
        return getFileValueReadText(".env").split("\n").find { it.startsWith(value) }!!.split("=")[1]
    }

    fun getConfig(value: String): String {
        return getFileValueReadText("config.json").split("\"$value\": \"")[1].split("\"")[0]
    }

    fun autoActivity(justin: JDA){
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                when ((0..2).random()) {
                    0 -> {
                        justin.presence.activity = Activity.watching("Cute Wolves 🦴🍭")
                    }
                    1 -> {
                        justin.presence.activity = (Activity.watching("Online in ${justin.guilds.size} Servers"))
                    }
                    2 -> {
                        justin.presence.activity = (Activity.watching("Do /help"))
                    }
                }
            }
        }, 0, 5000)
    }

    fun sendHelpButtons(): MutableList<Button> {
        val buttons = mutableListOf<Button>()
        buttons.add(Button.link("https://kiyoshi.space", "Website").withEmoji(Emoji.fromUnicode("🌐")))
        buttons.add(Button.link("https://discord.gg/ka7uGKfy7p", "Discord").withEmoji(Emoji.fromUnicode("📢")))
        buttons.add(Button.link("https://github.com/MyNameIsKiyoshi/", "GitHub").withEmoji(Emoji.fromUnicode("📦")))
        buttons.add(Button.link("https://www.spigotmc.org/resources/authors/mynameiskiyoshi.1382702/", "Spigot").withEmoji(Emoji.fromUnicode("📦")))
        return buttons
    }

    fun sendOrderPluginbutton(): MutableList<Button> {
        val buttons = mutableListOf<Button>()
        buttons.add(Button.primary("orderplugin", "Order Plugin").withEmoji(Emoji.fromUnicode("📦")))
        return buttons
    }

    fun getUserById(jda: JDA, id: String): User? {
        return jda.getUserById(id)
    }

    fun sendPrivateMessage(justin: JDA, user: User, message: String, title: String) {
        user.openPrivateChannel().queue { channel ->
            channel.sendMessageEmbeds(
                EmbedBuilder()
                    .setTitle(title)
                    .setDescription(message)
                    .setColor(Color.decode(getConfig("JUSTINCOLOR")))
                    .setAuthor(justin.selfUser.name)
                    .setImage(getConfig("SERVERIMAGE"))
                    .setTimestamp(Instant.now())
                    .build()
            ).queue()
        }
    }

    fun preventLag(){
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                System.gc()
            }
        }, 0, 1000)
    }

    fun stopTheBot(justin: JDA){
        justin.shutdown()
        exitProcess(0)
    }

    fun isKiyoshi(id: String): Boolean {
        return id == getConfig("OWNERID")
    }

    @Suppress("unused")
    fun getChannel(jda: JDA, guildId: String, channelId: String): String {
        return jda.getGuildById(guildId)!!.getTextChannelById(channelId)!!.asMention
    }

    fun sendEmbedMessageInChannel(jda: JDA, channelId: String, title: String, description: String?, authorName: String, authorURL: String, authorIconURL: String?, timestamp: TemporalAccessor){
        jda.getTextChannelById(channelId)!!.sendMessageEmbeds(
            EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.decode(getConfig("JUSTINCOLOR")))
                .setImage(getConfig("SERVERIMAGE"))
                .setAuthor(
                    authorName,
                    authorURL,
                    authorIconURL
                )
                .setTimestamp(timestamp)
                .build()
        ).queue()
    }

    fun sendEmbedMessageInAnnounceChannel(jda: JDA, channelId: String, title: String, description: String?, authorName: String, authorURL: String, authorIconURL: String?, timestamp: TemporalAccessor){
        jda.getNewsChannelById( channelId)!!.sendMessageEmbeds(
            EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.decode(getConfig("JUSTINCOLOR")))
                .setAuthor(
                    authorName,
                    authorURL,
                    authorIconURL
                )
                .setTimestamp(timestamp)
                .build()
        ).queue()
    }
    fun getTimestamp(): TemporalAccessor {
        return Date().toInstant()
    }

    fun tagStaff(): String {
        return "<@&${getConfig("STAFFID")}>"
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun userToMember(jda: JDA, userId: String): Member {
        return jda.getGuildById(getConfig("GUILDID"))!!.getMemberById(userId)!!
    }

    fun addRole(jda: JDA, guildId: String, target: UserSnowflake, roleId: String) {
        jda.getGuildById(guildId)!!.addRoleToMember(target, jda.getRoleById(roleId)!!).queue()
    }

    fun removeRole(jda: JDA, guildId: String, target: UserSnowflake, roleId: String) {
        jda.getGuildById(guildId)!!.removeRoleFromMember(target, jda.getRoleById(roleId)!!).queue()
    }

    fun discordInvite(): String {
        return getConfig("DISCORDSERVER")
    }

    fun getRoleByName(jda: JDA, guildId: String, roleName: String): Role {
        return jda.getGuildById(guildId)!!.getRolesByName(roleName, true)[0]
    }

    fun hasRole(jda: JDA, guildId: String, target: UserSnowflake, roleId: String): Boolean {
        return jda.getGuildById(guildId)!!.getMemberById(target.id)!!.roles.contains(jda.getRoleById(roleId)!!)
    }

    fun changeNickColor(jda: JDA, guildId: String, target: UserSnowflake, color: String) {
        jda.getGuildById(guildId)!!.modifyNickname(jda.getGuildById(guildId)!!.getMemberById(target.id)!!, color).queue()
    }

    fun codeMessage(message: String, language: String): String {
        return "```${language}\n$message\n```"
    }

    fun getAllCommands(jda: JDA): List<Command> {
        return jda.retrieveCommands().complete()
    }
}