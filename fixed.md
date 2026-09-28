# Create Crafts & Additions: Adapted - 26.3-1.7.0-adapted-0.16

## Connector display correction

Restored the familiar connector-scale energy bar: normally 5 kFE for large connectors and 1 kFE for small connectors, following their configured limits.

The reading uses the current sum of the two network buffers, capped at the connector display limit. This prevents artificial alternation between the buffers while retaining the familiar scale. The bar is a connector buffer indicator, not a measurement of all energy in the network. Transfer operations and limits are unchanged.

The Tesla Coil persistence fix from 0.15 and relay conservation fix from 0.13 are retained.

## Requirements and validation

Minecraft 26.3; NeoForge 26.3.0.25-beta or newer within 26.3; Create: Adapted 1.19 or newer.
JAR assembly passed. In-game Jade validation is pending. The Minecraft 26.2 checkout is unchanged.
