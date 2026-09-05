# Create Crafts & Additions: Adapted 0.8

Unofficial Minecraft 26.2 / NeoForge adaptation by BugsGify.
Original Create Crafts & Additions by MRH0.

File: `createaddition-26.2-1.7.0-adapted-0.8.jar`

Includes the user-provided project logo as the mod-list icon, with legacy logoFile metadata for Prism Launcher compatibility. No gameplay changes from 0.6.

Requires Create: Adapted 0.983 or newer and its dependencies. Install the addon on both client and server. This JAR contains the addon's resources, not Create or other dependency mods. Sable and Sable Companion are not bundled or required for the independent moving-wire implementation.

## Moving wires (experimental)

Connect a stationary connector to a connector attached to a modular accumulator before assembling the Create contraption. The moving connector uses Push to charge the contraption accumulator bank or Pull to discharge it. Wires follow movement and break beyond the connector's configured length limit. Two moving endpoints and connecting wires after assembly are not supported.

User-confirmed in-game: rotation, charging, gauge/goggle display, disassembly, world reload and overlength break. Dedicated-server multiplayer, reverse transfer, chunk transitions and destruction edge cases still need in-game testing. Use a test world or backup.

## License and resources

The upstream LICENSE grants MIT permissions for the project, subject to retaining the copyright and license notice. It separately identifies audio from Pixabay under the Pixabay Content License and Freesound audio under CC0. Those audio assets are not relicensed to MIT. Keep them integrated into the mod, retain the notices, and do not redistribute Pixabay audio as a standalone asset collection.

The complete upstream LICENSE, including its audio notices, is included in the JAR at `META-INF/LICENSE`. Describe this project as an unofficial adaptation and do not imply endorsement by the original authors.

See `ADAPTED-CHANGELOG.md` for version history and `TESTING-MOVING-WIRES.md` for test instructions.
