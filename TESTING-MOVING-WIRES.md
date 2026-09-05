# Moving wire test build 0.5

This is an experimental independent Create integration, not a Sable port.
Use a disposable test world or a copy of your world. Do not publish this build as fully tested.

## Setup

1. Use Create: Adapted 0.983 and Create Crafts & Additions: Adapted 0.5. No Sable is needed.
2. Make a stationary power source and a connector in Pull mode.
3. On a small glued piston/pulley/bearing contraption, place a modular accumulator and attach a connector directly to it, in Push mode.
4. Connect the two connectors with a spool BEFORE assembling/moving the contraption. Keep within the configured wire length.

## In-game checks still required

- Start movement: one wire should follow the moving connector, with no ghost at the original position.
- Charge: stop/disassemble and check that accumulator charge increased. Wait at least one second for the accumulator actor to register.
- Reverse flow: use moving Pull and stationary Push, and verify charge leaves the moving accumulator for a stationary consumer.
- Rotate on a bearing: the endpoint should follow the connector's attachment point, not just the block center.
- Stop/disassemble: the normal stationary wire should be restored, including after a quarter-turn rotation.
- Extend beyond maximum wire length: one wire drop, no remaining active link, no second drop on disassembly.
- Cut/break the stationary connector: no lingering moving connection or duplicate wire drop.
- Assemble the formerly stationary end too: the tether deliberately breaks once; two moving ends are not supported.
- Save/quit while moving and reload: the token and wire should survive, with the entity id rebound.
- Cross chunk boundaries: no forced chunk loading; transfer pauses when the stationary endpoint is unloaded and resumes when available.
- Destroy the contraption: the stationary wire is cleared. If its chunk was unloaded, cleanup is saved until that connector loads.
- Have a second player observe movement and rejoin: both should see the same endpoint and break.
- Repeat with copper, gold, electrum and festive wires; check vertical wires too.

## Scope

Only pre-connected stationary-to-moving tethers are implemented. For power, the moving connector must be attached to a modular accumulator; it uses the existing contraption-wide accumulator bank (as the portable energy interface does). This does not tick arbitrary machines aboard a contraption, add ship physics, or allow connecting spools to moving blocks.

## Verification performed

`gradle --offline build` passes, including `testCreateTethers` with 18 offline regression checks:
NBT round trips, negative coordinates, removal of only the captured node, persisted energy accounting, one-time deferred cleanup and the exact Create mixin target signatures. These checks do not execute Minecraft rendering, a real wire network, or multiplayer.

## Rollback

The Prism test profile keeps the previous addon 0.3 and Create 0.982 as `.jar.disabled` files. Disable/remove only the new 0.5/0.983 files and re-enable the two previous files to roll back. Restore the test world's backup if needed; do not downgrade a valuable world containing active experimental tethers.
