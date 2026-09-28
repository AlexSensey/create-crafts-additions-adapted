## 26.3-1.7.0-adapted-0.16

- Restore familiar connector display limits while retaining a stable, capped sum of the alternating buffers. Transfer behavior unchanged.

## 26.3-1.7.0-adapted-0.15

- Persist Tesla Coil energy through normal saves and mark server-side charge changes for saving.
- Network buffers remain transient, matching upstream. Prism save/reload validation pending.

## 26.3-1.7.0-adapted-0.14

- Report the combined current wire-network buffer and its total capacity through connector energy capabilities, avoiding alternating Jade readings.
- Existing transfer adapter and limits retained. Focused buffer-display check passed; in-game Jade validation pending.

## 26.3-1.7.0-adapted-0.13

- Prevent relay energy loss when the destination buffer cannot accept the full requested transfer.
- Five isolated conservation cases passed; in-game circuit validation pending.

## 26.3-1.7.0-adapted-0.12

- Fixed 26 recipe-unlock advancements using the old condition format, which prevented world loading in 26.3.
- JAR assembly and packaged-condition checks only; Prism validation is pending.

## 26.3-1.7.0-adapted-0.11

- Ported to Minecraft 26.3 in a separate branch, preserving the existing 26.2 version.
- Updated Create fluid and inventory compatibility APIs and its isolated Flywheel namespace.
- Replaced the removed NeoForge integer energy classes with internal storage classes behind the existing transactional energy adapter.
- Updated wire rendering to the new rendering API while retaining quad geometry.
- Migrated biomass pellets and pallets to the cooking fuel component, retaining burn times of 6,400 and 57,600 ticks.
- Updated the Liquid Blaze Burner's fuel lookup, accumulator rendering, redstone connections, item drops and inventory returns.
- Updated JEI and CC:Tweaked compile dependencies for 26.3.

## Validation

Compilation and JAR assembly passed. No game launch or gameplay tests were run. Manual validation in Prism 26.3 is pending, including motors, alternators, accumulators, wire connections and fuel handling. Optional mod integrations have not been validated.

## 26.2-1.7.0-adapted-0.10

Metadata-only compatibility release for Create: Adapted 1.x. Adds the [1.0,2.0) version range while retaining [6.0.11-adapted-0.987,). Runtime classes and assets are unchanged from 26.2-1.7.0-adapted-0.9. Startup/gameplay validation is still required.

# Create Crafts & Additions: Adapted changelog

## 26.2-1.7.0-adapted-0.8

- Add legacy logoFile metadata for Prism Launcher, retaining iconFile and an explicit bannerFile for NeoForge 26.2. All fields reference the same user-provided icon.png.
- The legacy field is deprecated by NeoForge and can produce a development-mode warning; retained for launcher compatibility.
- No gameplay changes.

## 26.2-1.7.0-adapted-0.7

- Replace the mod-list icon with the user-provided logo.jpg, converted to PNG without resizing for the existing iconFile metadata.
- No gameplay changes from 0.6.

## 26.2-1.7.0-adapted-0.6

- Package the independently implemented moving-wire support for distribution with the mod's resources included.
- User-tested in Create: Adapted: wire follows rotation, accumulator charges during movement, gauge and goggles update, connections survive disassembly and world reload, and overlong wires break.
- Wire colors and alternating segment shading were compared with upstream and left unchanged.
- No new gameplay changes since 0.5. Moving wires remain experimental: reverse energy flow, dedicated-server multiplayer, chunk unload/reload and destruction edge cases still need in-game verification.
- Retain the upstream MIT license, copyright notice and separate Pixabay/CC0 audio notices. No Sable or Sable Companion library is bundled.
- Requires Create: Adapted 0.983 or newer. Connect wires before assembly; only stationary-to-moving links are supported.

## 26.2-1.7.0-adapted-0.5 (experimental moving wires)

- Add an independent Create contraption tether implementation; Sable is not required or bundled.
- Connect a stationary connector to a connector on a contraption before assembly. The wire follows translation and rotation.
- A moving connector attached directly to a modular accumulator can charge (Push) or discharge (Pull) the contraption's existing accumulator bank.
- Persist link identity and local endpoints, restore stationary links after disassembly, and break overlong wires at their owning endpoint.
- Save deferred cleanup when a contraption is destroyed while the stationary endpoint's chunk is unloaded.
- Synchronize tether identity through connector updates and use Create entity tracking for client movement.
- Keep the separate Create client block tags consistent with captured server connections.
- Clear stale connector caches when replacing a node; guard vertical wire geometry against division by zero.
- Scope: one stationary and one moving endpoint. Assembling the second endpoint breaks the wire once. This does not add physics, Sable ships, live ticking of arbitrary machines, or wiring to an already moving contraption.
- Offline data/API regression tests are included. In-game rendering, rotation, chunk transitions and multiplayer still require testing.

## 26.2-1.7.0-adapted-0.4

- Bundle the full upstream MIT license, copyright and third-party audio notices in META-INF/LICENSE.
- Build against Create: Adapted 0.983 and declare the tested minimum version.
- Correct BugsGify attribution and direct adaptation support links to the adaptation repository.
- Use the Minecraft 26.2 iconFile metadata field.
- Port the legacy RollingMillAssemblySubCategory helper to GuiGraphicsExtractor and include it in compilation.
  Upstream 1.7.0 does not register rolling as an assembly operation; this does not add a new recipe mechanic.
- Sable support is NOT completed: available Sable dependencies target Minecraft 1.21.1.
  A compatible 26.2 Sable/Sable Companion build and runtime testing are still required.

## 26.2-1.7.0-adapted-0.3

- Fixed the AE2 Certus Quartz charging recipe to use the Minecraft 26.2
  ingredient format.
- Completed the Ukrainian translation for every current English key.
- Added Ukrainian names for Electrum Block, Biomass Pallet, Servo Motor,
  and Small Connector With Light.
- Added missing Ukrainian tooltips, subtitles, death messages, and fluid tag.
- Added item-name aliases for translated blocks in JEI and inventories.
- Preserved every language from the original 1.21.1 mod.

## 26.2-1.7.0-adapted-0.2

- Requires Create: Adapted 6.0.11-adapted-0.94 or newer.
- Restored JEI 30 recipe categories for Charging, Rolling, and Liquid Burning.
- Restored animated Tesla Coil, Rolling Mill, and Blaze Burner recipe previews.
- Matched the Rolling Mill and Tesla Coil preview placement to the original version.
- Fixed Barbed Wire playing its hurt sound every tick while an entity was invulnerable.
- Fixed Liquid Blaze Burner fluid-container interaction and client prediction.
- Fixed malformed Minecraft 26.2 recipe ingredient syntax.
- Added conditional loading for optional Simulated and Sable mixins.
- Includes the previous 26.2 rendering, energy transfer, contraption, Jade, goggles,
  wire, motor, accumulator, connector, and localization adaptations.

## 26.2-1.7.0-adapted-0.1

- Initial unofficial NeoForge adaptation for Minecraft 26.2.
