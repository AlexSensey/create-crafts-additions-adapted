# Create Crafts & Additions: Adapted - 26.2-1.7.0-adapted-0.16

## Backported fixes

- Stable connector energy display: use the combined current network buffer, capped at the familiar connector scale (normally 5 kFE for large connectors and 1 kFE for small connectors). Transfer operations and limits are unchanged.
- Prevent relay energy loss when the destination cannot accept the full request by checking available space before withdrawing from the input.
- Save Tesla Coil energy during normal world saves and mark server-side energy changes for saving. Previously lost charge cannot be recovered.
- Wire-network buffers remain transient and restart empty after world reload.

## Compatibility

Minecraft 26.2, NeoForge 26.2.0.59 or newer within the 26.2 series, and Create: Adapted 1.15 or newer. Build dependencies and Flywheel imports now match the isolated Flywheel included with Create: Adapted 1.15.

## Validation

Compilation and JAR assembly passed against the 26.2 APIs. No game launch or broad tests were run. In-game Jade, relay and charged-coil save/reload checks in Prism 26.2 are pending. The separate Minecraft 26.3 worktree is unchanged.
