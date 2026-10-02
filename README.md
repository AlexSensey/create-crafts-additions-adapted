# Create Crafts & Additions: Adapted

[![GitHub release](https://img.shields.io/github/v/release/AlexSensey/create-crafts-additions-adapted?logo=github&label=release)](https://github.com/AlexSensey/create-crafts-additions-adapted/releases/latest)
[![GitHub downloads](https://img.shields.io/github/downloads/AlexSensey/create-crafts-additions-adapted/total?logo=github&label=downloads)](https://github.com/AlexSensey/create-crafts-additions-adapted/releases)
![Minecraft 26.1.2](https://img.shields.io/badge/Minecraft-26.1.2-62b47a)
![NeoForge 26.1.2](https://img.shields.io/badge/NeoForge-26.1.2-orange)

An unofficial Minecraft 26.1.2 NeoForge adaptation of Create Crafts & Additions.
It bridges electricity and kinetic energy in
[Create: Adapted](https://www.curseforge.com/minecraft/mc-mods/create-adapted).

## Minecraft 26.1.2 branch

Branch: `mc26.1.2-adapted`. Mod version: `26.1.2-1.7.0-adapted-0.17`.

## Download

This branch builds `createaddition-26.1.2-1.7.0-adapted-0.17.jar` for Minecraft 26.1.2.

- Requires Minecraft 26.1.2 and NeoForge 26.1.2.109 or newer.
- Requires [Create: Adapted 1.27 or newer](https://www.curseforge.com/minecraft/mc-mods/create-adapted).
- Install the same Create Crafts & Additions: Adapted JAR on both the client and server.

## About this adaptation

This adaptation is maintained by Buggsify and is not affiliated with the official
Create or Create Crafts & Additions development teams.

The 26.1.2 adaptation includes the NeoForge transfer API migration, Flywheel-compatible
moving block rendering, updated recipes and resources, JEI 29 categories, contraption
energy support, and fixes for wires, gauges, motors, the Rolling Mill, Tesla Coil,
Liquid Blaze Burner, and Barbed Wire.

See [ADAPTED-CHANGELOG.md](ADAPTED-CHANGELOG.md) for adaptation-specific changes.

## Original project and credits

Create Crafts & Additions was created by MRH0. The original project remains
available on [GitHub](https://github.com/mrh0/createaddition) and
[CurseForge](https://www.curseforge.com/minecraft/mc-mods/createaddition).

This repository preserves the original project's MIT license and attribution.

## Content

![Items](https://raw.githubusercontent.com/mrh0/createaddition/1.18.2/cca_20230412a.png)

- Electric Motor.
- Alternator.
- Rolling Mill.
- Creative Generator.
- Telsa Coil.
- Accumulator.
- Digital Adapter.
- Energy transport.
- Diamond Grit Sandpaper.

And more!

## License

Available under MIT the license more info at: https://tldrlegal.com/license/mit-license

MIT License

Copyright 2022 MRH0

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Build this branch

Use Java 25. Stage the real Create: Adapted 1.27 (or compatible newer) and JEI 29.37.0.98 binaries:

```sh
python tools/prepare_build_dependencies.py --create-jar /path/to/create-adapted-26.1.2-1.27-public-external-assets.jar --jei-jar /path/to/jei-26.1.2-neoforge-29.37.0.98.jar
./gradlew jar testCreateTethers
```

The first build resolves NeoForge and the retained optional compile-time APIs from the configured repositories. Original Create assets are required by the public Create: Adapted build, as described by that project.
