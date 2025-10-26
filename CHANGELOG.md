# Changelog – Debug Stick Crafting in Survival

## [2.1.3-1.21] – October 26, 2025

### Added
- New permission system for Nether dimension waterlogging (debugstickcs.waterlog.nether)
- Separate waterlogging restrictions for Overworld and Nether dimensions

### Changed
- Split waterlogging permissions into dimension-specific controls
- Improved permission checking logic with new WaterlogPermissionResult pattern
- Streamlined waterlogging event handling with clearer responsibility separation

### Technical
- Replaced enum-based permission results with factory pattern
- Introduced immutable result objects for permission checks
- Improved error handling for waterlogging operations
- Added dedicated methods for state management

### Compatibility
- **Minecraft:** 1.21+
- **Java:** 21+
- **API:** 1.21

## [2.1.1-1.21] – October 19, 2025

### Added
- Intelligent capability restriction system (waterlogging & slab doubling control)
- Granular permission system:
  - `debugstickcs.waterlog`, `debugstickcs.doubleslab`, `debugstickcs.reload`, `debugstickcs.*`
- Smart property management (change block states without full restriction)
- Two-level restriction model for excluded blocks and restricted capabilities

### Changed
- Reworked configuration structure with new toggles for restriction features
- Updated excluded block list (removed slabs, leaves, trapdoors)
- Improved event handling (block state snapshot + delayed revert system)
- Permission defaults adjusted (basic Debug Stick use now available to all players)

### Fixed
- Debug Stick not working for non-OP players
- Missing permission defaults (`minecraft.debugstick`, `minecraft.debugstick.always`)
- Issues with waterloggable blocks reverting unexpectedly
- Error handling and stability improvements

### Compatibility
- **Minecraft:** 1.21+
- **Java:** 21+
- **API:** 1.21