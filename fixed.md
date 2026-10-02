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
