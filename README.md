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

* `/scythe toggle`: Toggles the automatic replanting and right-click harvesting feature for the player who executed the
  command. This requires the `scythe.toggle` permission.
* `/scythe reload`: Reloads the configuration for the plugin. This requires the `scythe.reload` permission.

* `/scythe-item [player]` : If custom items are enabled in config, will give the user a scythe item if they have `scythe.item` permission, or if the user has `scythe.item.others` permission, will give the provided player a scythe item
### Permissions

* `scythe.use` : Base permission for access to Scythe functionality.
* `scythe.use.harvest` : Allows a player to harvest blocks using scythe functionality
* `scythe.use.replant` : Allows a player to auto-replant crops using scythe functionality
* `scythe.toggle` : Allows the player to toggle the feature for themselves using the /scythe toggle command.
* `scythe.reload` : Allows the player to reload the configuration using the /scythe reload command.
* `scythe.item` : Allows the player to obtain a scythe item created in the config
* `scythe.item.others` : Allows the player to send a scythe item to another player
* `scythe.bypass.cooldown` : Allows the player to bypass the configured command cooldown for the scythe item command

## Config

Note, generated config does not have this many comments lol, this is just for the README/description

```yml
#Scythe
auto-replant:
  enabled: true # Should crops automatically be replanted?
  require-seeds: false # Should the user be required to have seeds in their inventory? Note: will consume seeds
  require-tool: false # Should the user be required to use a specific tool?
  delay-ticks: 1 # How long in ticks after breaking the block should it be replanted? (20 ticks per second, without lag)
right-click-harvest:
  enabled: true # Should users be able to right-click to harvest blocks?
  require-tool: false # Should users be required to use a specific tool?
#Only applies if "require-tool" is true in any of the above categories
tools:
  enabled-tools: # If users are required to use a specific tool, which tools are valid?
    - "minecraft:wooden_hoe"
    - "minecraft:stone_hoe"
    - "minecraft:iron_hoe"
    - "minecraft:golden_hoe"
    - "minecraft:diamond_hoe"
    - "minecraft:netherite_hoe"
  # Item models should be declared like "namespace:id"
  required-item-models: [ ] # Do you have any specific item models you're using on tools that make them look different? If so, declare them here
  # If you want to use a custom item, please note that the item validity is checked by a pdc tag, so old versions will still be valid
  # This also means other players cannot make fake versions, but it also means even if you copy the info here, it won't be a valid tool
  # If custom item is enabled, the previous settings for valid item types and item models will be ignored.
  custom-item:
    enabled: false
    custom-name: "<yellow>Scythe</yellow>"
    lore:
      - "<white>This is some cool lore</white>"
      - "<green>Idk you should probably change this</green>"
      - "<gradient:blue:green>Colors and stuff yay</gradient>"
    item-type: WOODEN_HOE
    item-model: ""
    enchantment-glint: true
    max-durability: 2031
    command-cooldown-seconds: 600  
  # Note: In vanilla, harvesting normally crops does NOT use durability.
  # This will cause the tool to lose durability on right-click harvest ONLY
  durability:
    harvest-uses-durability: false # Should right-click-harvest make tool durability go down?
    replant-uses-durability: false # Should auto-replant make tool durability go down?
    prevent-tool-break: true # If either of the two above are set to 'true', do you want to prevent the plugin from working if a tool is low on durability, to prevent users from accidentally breaking their tools?
    minimum-durability: 10 # If the above is set to true, how low should durability be when the plugin stops working?
# Please choose a sound from this list: https://jd.papermc.io/paper/1.21.4/org/bukkit/Sound.html
sounds:
  enabled: true # Should sounds be played when a user breaks or auto-replants a crop?
  break-sound: BLOCK_CROP_BREAK # Which sound should be played when a crop is broken?
  plant-sound: ITEM_CROP_PLANT # Which sound should be played when a crop is replanted?
  # between 0 and 2
  volume: 1.0 # How loud should it be?
  # between 0 and 2
  pitch: 1.0 # How high-pitched should it be?
# Please choose a particle from this list: https://jd.papermc.io/paper/1.20/org/bukkit/Particle.html
particles:
  harvest:
    enabled: true # Should particles show when a user right-click-harvests a block?
    particle: BLOCK # Which particle should show?
    count: 40 # How many particles should spawn? This will wildly depend on the selected particle
    # how far from the center particles should go, in blocks
    spread: 0.5
  replant:
    enabled: true  # Should particles show when a block is auto-replanted?
    particle: HAPPY_VILLAGER # Which particle should show?
    count: 2 # How many particles should spawn? This will wildly depend on the selected particle
    spread: 0.5 # how far from the center should particles go, in blocks
# Crops the plugin should work on, list of materials is here: https://jd.papermc.io/paper/1.21.4/org/bukkit/Material.html
# Note, the material must have the BlockData of 'Ageable' - note, most other ageable blocks in the list don't work as intended.
allowed-crops:
  - BEETROOTS
  - CARROTS
  - COCOA
  - NETHER_WART
  - POTATOES
  - WHEAT
  # - PUMPKIN_STEM
  # - MELON_STEM
```

## API

### HarvestEvent - Cancellable

| Method                  | Returns       | Description                                   |
|-------------------------|---------------|-----------------------------------------------|
| `getPlayer()`           | `Player`      | Returns the player who triggered the harvest. |
| `getBlock()`            | `Block`       | Returns the crop block being harvested.       |
| `isCancelled()`         | `boolean`     | Checks if the event is cancelled.             |
| `setPlayer(Player)`     | `void`        | Updates the player associated with the event. |
| `setBlock(Block)`       | `void`        | Updates the block involved in the event.      |
| `setCancelled(boolean)` | `void`        | Cancels or un-cancels the event.              |
| `getHandlers()`         | `HandlerList` | Bukkit-required handler method.               |
| `getHandlerList()`      | `HandlerList` | Bukkit-required static handler method.        |

### ReplantEvent - Cancellable

| Method                    | Returns       | Description                                                 |
|---------------------------|---------------|-------------------------------------------------------------|
| `getPlayer()`             | `Player`      | Returns the player who triggered the replanting.            |
| `getBlock()`              | `Block`       | Returns the block being replanted.                          |
| `getBlockData()`          | `BlockData`   | Returns the BlockData being applied to the replanted block. |
| `isCancelled()`           | `boolean`     | Checks if the event is cancelled.                           |
| `setPlayer(Player)`       | `void`        | Updates the player associated with the event.               |
| `setBlock(Block)`         | `void`        | Updates the block involved in the event.                    |
| `setBlockData(BlockData)` | `void`        | Updates the BlockData for the replanted block.              |
| `setCancelled(boolean)`   | `void`        | Cancels or un-cancels the event.                            |
| `getHandlers()`           | `HandlerList` | Bukkit-required handler method.                             |
| `getHandlerList()`        | `HandlerList` | Bukkit-required static handler method.                      |

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

If you need any help with the Scythe plugin, please open an issue on the GitHub repository or join
our [Discord server](https://discord.gg/qe3YQrbegA) for support.

### License

Scythe is licensed under the MIT license. See the LICENSE file for more information.
