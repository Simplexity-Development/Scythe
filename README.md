# Scythe - Auto-Replant, Right-Click-Harvest plugin

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/scythe?color=00AAAA&label=%20Downloads&style=flat-square&logo=modrinth)](https://modrinth.com/plugin/scythe)
[![Discord](https://img.shields.io/badge/Discord-join-7289DA?logo=discord&logoColor=7289DA&style=flat-square)](https://discord.gg/qe3YQrbegA)

<a href='https://ko-fi.com/E1E8DZGDF' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://storage.ko-fi.com/cdn/kofi1.png?v=3' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

### Features

* Automatically replant crops after they are harvested
* Right-click to harvest fully grown crops
* Crouch in order to break crops without having to toggle the functionality off
* Configuration options to customize how you'd like the plugin to work on your server

### Commands

* `/scythe toggle`: Toggles the automatic replanting and right-click harvesting feature for the player who executed the command. This requires the `scythe.toggle` permission. 
* `/scythe reload`: Reloads the configuration for the plugin. This requires the `scythe.reload` permission.

### Permissions

* `scythe.use` : Base permission for access to Scythe functionality.
* `scythe.use.harvest` : Allows a player to harvest blocks using scythe functionality
* `scythe.use.replant` : Allows a player to auto-replant crops using scythe functionality
* `scythe.toggle` : Allows the player to toggle the feature for themselves using the /scythe toggle command.
* `scythe.reload` : Allows the player to reload the configuration using the /scythe reload command.

### Configuration Options

* `right-click-to-harvest` : (Boolean) Allows right-click harvesting. 
* `require-tool-for-replant` : (Boolean) Requires a tool in order for crops to automatically replant (configured below)
* `require-tool-for-right-click-harvest` : (Boolean) Requires a tool for right click harvest, requires `right-click-harvest` to be true
* `replant-tools` : (String list) List of tools allowed to be used. Only used if `require-tool-for-replant` or `require-tool-for-right-click-harvest` is set to `true`. These must be declared as they would be written in a vanilla `/give` command
* `play-sounds` (Boolean): If set to true, sounds will be played to emulate blocks breaking when right-clicking to harvest crops. If set to false, no sounds will be played.
* `sound` : (String) Should be taken from [The Sound Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html)
* `sound-volume` : (float) The volume the sound should be played at
* `sound-pitch` : (float) The pitch the sound should be played at
* `break-particles`: (Boolean) If set to true, particles will be displayed to emulate blocks breaking when right-clicking to harvest crops. If set to false, no particles will be displayed.
* `particle` : (String) The particle to be shown, should be taken from [The Particle Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Particle.html)
* `particle-count` : (int) The number of particles to play
* `crops` (String list): A whitelist of crops that this plugin should work on. Any crops not on this list will not be affected by the plugin.

### Installation

1. Download the latest release of the Scythe plugin from the releases page.
2. Place the plugin jar file in the plugins folder of your Minecraft server.
3. Start the server and wait for the plugin to be enabled.
4. Edit the configuration file located in the plugins/Scythe folder to your liking.
5. Restart the server for the changes to take effect.

## API

### [Javadocs](https://simplexity-development.github.io/Scythe/index.html)

### Maven
```xml
<repository>
  <id>modrinth-repo</id>
  <url>https://api.modrinth.com/maven/</url>
</repository>

<dependency>
  <groupId>maven.modrinth</groupId>
  <artifactId>scythe</artifactId>
  <version>4.0</version>
  <scope>provided</scope>
</dependency>
```

### Gradle
```gradle
exclusiveContent {
    forRepository { maven { url = "https://api.modrinth.com/maven" } }
    filter { includeGroup "maven.modrinth" }
}

dependencies {
    compileOnly 'maven.modrinth:scythe:4.0'
}
```

### Support

If you need any help with the Scythe plugin, please open an issue on the GitHub repository or join our [Discord server](https://discord.gg/qe3YQrbegA) for support.

### License

Scythe is licensed under the MIT license. See the LICENSE file for more information.
