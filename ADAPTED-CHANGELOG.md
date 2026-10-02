# Create Crafts & Additions: Adapted ? Minecraft 26.1.2

Version: 26.1.2-1.7.0-adapted-0.17
Branch: mc26.1.2-adapted

- Adapted the existing 26.2 code to Minecraft 26.1.2 and NeoForge 26.1.2.109 using Java 25.
- Requires Create: Adapted 1.27 or newer, without pinning an exact Create version.
- Uses the Flywheel namespace and baked model API of the 26.1.2 Create build.
- Adapted wire rendering to the native 26.1.2 render pipeline while preserving QUADS topology and the original shader settings.
- Adapted the Rolling Mill's item stack template conversion.
- Fixed client startup when Ponder registered the Electric Blocks icon before item components were ready. The category uses the existing mod logo as its texture icon; all tutorials remain registered.
- Preserved createaddition-common.toml and supported LOCAL/COMMON configuration names.
- Retained the existing recipes, translations, models, energy bridge and contraption wire fixes.

Validation:
- Native dedicated server: all 21 mod blocks placed, including 14 block entities; 11 rolling, 52 charging and 3 liquid-burning recipes loaded; the motor accepted and stored energy through the NeoForge capability.
- Native client with the installed 26.1.2 mods in a disposable world copy: all 7 partial models baked, QUADS wire pipeline verified, 67 JEI recipe layouts and animated preview extraction passed, and all 3 category screens rendered.
- All 18 existing tether data/API regression checks passed.

These tests do not cover every gameplay scenario, optional mod integration or a remote multiplayer connection.

---

## 26.2-1.7.0-adapted-0.16

- Stable connector energy display: use the combined current network buffer, capped at the familiar connector scale (normally 5 kFE for large connectors and 1 kFE for small connectors). Transfer operations and limits are unchanged.
- Prevent relay energy loss when the destination cannot accept the full request by checking available space before withdrawing from the input.
- Save Tesla Coil energy during normal world saves and mark server-side energy changes for saving. Previously lost charge cannot be recovered.
- Wire-network buffers remain transient and restart empty after world reload.

## Compatibility

Minecraft 26.2, NeoForge 26.2.0.59 or newer within the 26.2 series, and Create: Adapted 1.15 or newer. Build dependencies and Flywheel imports now match the isolated Flywheel included with Create: Adapted 1.15.

## Validation

Compilation and JAR assembly passed against the 26.2 APIs. No game launch or broad tests were run. In-game Jade, relay and charged-coil save/reload checks in Prism 26.2 are pending. The separate Minecraft 26.3 worktree is unchanged.

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
