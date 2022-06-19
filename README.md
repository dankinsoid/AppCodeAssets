# AppCodeAssets

![Build](https://github.com/dankinsoid/AppCodeAssets/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
This plugin provides integration with [`Tuist` commmands](https://docs.tuist.io/commands).

## Usage
Commands are available in <kbd>Tools</kbd> > <kbd>Tuist</kbd> menu. It's recommended to add keymaps for these commands.
While editing a manifest projects `tuist generate` calls automatically on saving, as well as `tuist fetch` while editing the `Dependencies.swift`.

## Available commands
- Generate - [`tuist generate -n`](https://docs.tuist.io/commands/generate).
- Edit Manifests - [`tuist edit`](https://docs.tuist.io/commands/edit) in `AppCode`.
- Fetch Dependencies - [`tuist fetch`](https://docs.tuist.io/commands/dependencies) and [`tuist generate -n`](https://docs.tuist.io/commands/generate).
- Update Dependencies - [`tuist fetch --update`](https://docs.tuist.io/commands/dependencies) and [`tuist generate -n`](https://docs.tuist.io/commands/generate).
- Clean Dependencies - [`tuist clean dependencies`](https://docs.tuist.io/commands/dependencies).
- Build - [`tuist build`](https://docs.tuist.io/commands/build).
- Clean - [`tuist clean`](https://docs.tuist.io/commands/clean).
- Test - [`tuist test`](https://docs.tuist.io/commands/test).
<!-- Plugin description end -->

## License
This plugin is licensed under the terms of the GNU Public License version 3 or any later version.

## Credits
Plugin icon is merged icons of IdeaVim plugin and a random sneaker by FreePic from flaticon.com

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "AppCodeAssets"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/dankinsoid/AppCodeAssets/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
