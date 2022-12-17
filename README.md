# Scythe - Automated Crop Replanting and Right-Click Harvesting Plugin for Minecraft

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/scythe?color=00AAAA&label=Modrinth%20Downloads&style=flat-square&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/scythe)
[![GitHub Downloads](https://img.shields.io/github/downloads/ADHDMC/Scythe/total?color=00AAAA&label=GitHub%20Downloads&logo=github&style=flat-square)](https://github.com/ADHDMC/Scythe/releases)

[![Discord](https://img.shields.io/badge/Discord-join-7289DA?logo=discord&logoColor=7289DA&style=flat-square)](https://discord.gg/qe3YQrbegA)
[![Ko-Fi Support Link](https://img.shields.io/badge/Ko--fi-donate-FF5E5B?logo=ko-fi&style=flat-square)](https://ko-fi.com/illogicalrhythmic)

Scythe is a plugin for Minecraft that allows players to automatically replant crops and right-click to harvest them. This can save time and effort, allowing players to focus on other tasks in their game.
### Features

* Automatically replant crops after they are harvested
* Right-click to harvest fully grown crops
* Customizable configuration options, including the ability to require a hoe for replanting, play sounds and particles when right-clicking to harvest, and specify a whitelist of crops to affect
* Language configuration options for translating messages in the plugin

### Commands

* `/scythe toggle`: Toggles the automatic replanting and right-click harvesting feature for the player who executed the command. This requires the `scythe.toggle` permission. 
* `/scythe reload`: Reloads the configuration for the plugin. This requires the `scythe.reload` permission.

### Permissions

* `scythe.use` : Allows the player to use the automatic replanting and right-click harvesting feature.
* `scythe.toggle` : Allows the player to toggle the feature for themselves using the /scythe toggle command.
* `scythe.reload` : Allows the player to reload the configuration using the /scythe reload command.

### Configuration Options

* `require-hoe` (Boolean): If set to true, players will need to use a hoe to automatically replant crops. If set to false, automatic replanting will occur without the need for a hoe.
* `right-click-to-harvest` (Boolean): If set to true, players will be able to right-click to harvest fully grown crops. If set to false, right-clicking will not harvest crops.
* `play-sounds` (Boolean): If set to true, sounds will be played to emulate blocks breaking when right-clicking to harvest crops. If set to false, no sounds will be played.
* `break-particles` (Boolean): If set to true, particles will be displayed to emulate blocks breaking when right-clicking to harvest crops. If set to false, no particles will be displayed.
* `crops` (String list): A whitelist of crops that this plugin should work on. Any crops not on this list will not be affected by the plugin.

### Installation

1. Download the latest release of the Scythe plugin from the releases page.
2. Place the plugin jar file in the plugins folder of your Minecraft server.
3. Start the server and wait for the plugin to be enabled.
4. Edit the configuration file located in the plugins/Scythe folder to your liking.
5. Restart the server for the changes to take effect.

### Support

If you need any help with the Scythe plugin, please open an issue on the GitHub repository or join our [Discord server](https://discord.gg/qe3YQrbegA) for support.

### Credits

Scythe was developed by ADHDMC Development

### License

Scythe is licensed under the MIT license. See the LICENSE file for more information.
