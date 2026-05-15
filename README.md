# LiamEngine

A 2D game engine built in Java, developed as the engine tier for college game projects.

## Purpose

Provides a reusable 2D game framework with a game loop, level management, entity/component system, sprite rendering, collision detection, audio playback, and input handling. Student-built games extend the abstract classes (`ILevel`, `IDrawable`) to create their gameplay.

## Requirements

- Java 8 (1.8)
- Apache Ant (for building via NetBeans or CLI)

## Structure

| Package | Description |
|---|---|
| `Entry` | Window management and level switching (`Game`) |
| `AbstractClasses` | Base classes for levels (`ILevel`) and game objects (`IDrawable`) |
| `Components` | Transform, collision, vector math, sprite sheets |
| `Utils` | Image/audio/resource loading, level loader, file utilities |
