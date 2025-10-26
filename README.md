# Minecraft-Debug-Stick-Crafting-in-Survival

Plugin allows to craft "Debug Stick" in survival mode

## 🎮 Features

- Custom crafting recipe
- Block restrictions with a configurable list
- Messages and feature toggles via config
- Hot reload with `/debugstickreload`

## 📋 Requirements

- Minecraft server: 1.21+ (Spigot or Paper)
- Java: JDK 21+

## 🔧 Installation

1. Download `DebugStickCraftingInSurvival-{version_of_plugin}-{version_of_minecraft}.jar` from Releases.
2. Place it in `plugins/`.
3. Restart the server or use `/reload`.
4. Edit `plugins/DebugStickCraftingInSurvival/config.yml`.
5. Run `/debugstickreload` to apply changes.

## 📖 Usage

### Crafting recipe
![image](https://user-images.githubusercontent.com/77124888/175134422-a4c1c9c8-3ab6-4693-9fec-0f3cfa30e17e.png)

### Commands

| Command              | Description                | Permission             | Aliases                     |
|---------------------|----------------------------|------------------------|-----------------------------|
| `/debugstickreload` | Reloads plugin configuration | `debugstickcs.reload` | `/dsreload`, `/debugreload` |

### Permissions

| Permission                     | Description                              | Default |
|--------------------------------|------------------------------------------|---------|
| `debugstickcs.reload`          | Allows reloading configuration           | OP      |
| `minecraft.debugstick`         | Allows using Debug Stick                 | off     |
| `minecraft.debugstick.always`  | Allows using Debug Stick in Survival     | off     |


## ⚙️ Configuration

File: `plugins/DebugStickCraftingInSurvival/config.yml`

~~~yaml
# Feature toggles
features:
  enable_crafting: true
  enable_restrictions: true

# Messages shown to players
messages:
  restriction: "§cYou cannot use the Debug Stick on this type of block!"

# List of blocks where Debug Stick usage is restricted
excluded_blocks:
  - COMPOSTER
  - CHEST
  - CACTUS
  - ...
~~~

## 🔨 Build from source

Requirements:
- JDK 21+
- Maven 3.6+

Build:
~~~bash
mvn clean package
~~~

## 🔌 Compatibility

| Plugin Version | Minecraft | Java | API |
|----------------|-----------|------|-----|
| 2.1.0+         | 1.21+     | 21+  | 1.21|
| 1.x            | 1.19.x    | 8+   | 1.19|

## 🗂️️ Project Structure

~~~
pl.nubet.debugstickinsurvival/
├── DebugStickCraftingInSurvival.java
├── command/
│   └── ReloadCommand.java
├── config/
│   └── ConfigurationManager.java
├── service/
│   └── BlockRestrictionService.java
├── listener/
│   └── DebugStickInteractionListener.java
└── recipe/
    └── DebugStickRecipeManager.java
~~~

## 📝 Changelog
See `CHANGELOG.md` for full history.

## 🔗 Links
- [SpigotMC](https://www.spigotmc.org/resources/debug-stick-in-survival.102837/)
- [Modrinth](https://modrinth.com/plugin/debug-stick-in-survival)
---
Made with ❤️ by Nubet ️
