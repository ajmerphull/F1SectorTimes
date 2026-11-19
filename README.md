# `README.md`

```markdown
# F1 Sector Time Calculator

A small desktop tool for Formula 1 fans and sim racers that calculates your **theoretical fastest lap** by summing your three best sector times.

**Version:** 1.0

---

## What it does

Enter your best time for each of the three sectors of a lap. The app parses each entry, sums them, and displays the **Possible Fastest Lap** — the lap time you could achieve if you stitched your best sectors together. This is the classic "ideal lap" metric used in F1 telemetry analysis.

The window stays **Always on Top** by default, so you can keep it visible alongside a timing screen, stream, or sim.

---

## Features

- **Three sector inputs** — Sector 1, Sector 2, Sector 3.
- **Digit-only entry** — non-numeric characters are filtered as you type; input is capped at 6 digits.
- **Automatic time formatting** — a full 6-digit entry is reformatted on focus loss as `M:SS.mmm` (e.g. `123456` → `1:23.456`).
- **Flexible parsing** — accepts raw digits, `SS.mmm`, or full `M:SS.mmm` formats, so you can type however you like.
- **Live total** — the Possible Fastest Lap field updates automatically as you type and when fields lose focus.
- **Always on Top** — toggleable checkbox; enabled by default.
- **Reset button (`X`)** — clears all fields in one click.
- **macOS integration** — native look and feel, dock icon, application name, and About handler.

---

## Requirements

- **Java** (compiled for Java 7+; uses Swing).
- **macOS** recommended — the app includes macOS-specific niceties (dock icon, screen menu bar, app name). It will still run on Windows/Linux, minus the macOS integrations.
- A system with a graphical display (Swing desktop app).

---

## Building

The project is a NetBeans Java project and can be built with NetBeans or Ant.

**With NetBeans:**
1. Open the `F1SectorTimeCalculator` folder as a project.
2. Run **Clean and Build** (`Shift+F11`).

**With Ant:**
```bash
ant clean jar
```

Build outputs:

- `build/classes/` — compiled `.class` files
- `dist/F1SectorTimeCalculator.jar` — the runnable application JAR

A packaged macOS app bundle is included at `F1 Sector Time Calculator.app/`.

---

## Running

**From the JAR:**
```bash
java -jar dist/F1SectorTimeCalculator.jar
```

**From the macOS app bundle:**
```bash
open "F1 Sector Time Calculator.app"
```

---

## Usage

1. Type your best sector 1 time into the first field (digits only).
2. Repeat for sectors 2 and 3.
3. The **Possible Fastest Lap** updates automatically.

**Input formats accepted:**

| You type | Interpreted as |
|----------|----------------|
| `123456` | `1:23.456` (M:SS.mmm) |
| `23456`  | `23.456` (SS.mmm) |
| `456`    | `0.456` (mmm) |
| `1:23.456` | `1:23.456` |

When you leave a field containing exactly 6 digits, it is auto-formatted to `M:SS.mmm` for readability. Shorter entries are left as-is but still parsed correctly.

Use the **X** button to reset all fields. Toggle **Always on Top** to keep the window above other apps.

---

## How it works

- **Input filtering:** a custom `PlainDocument` (`DigitOnlyDocument`) strips non-digit characters on insert and enforces a 6-digit maximum, triggering a total recalculation.
- **Formatting:** on focus loss, `formatFieldValue` converts a 6-digit string into `M:SS.mmm`.
- **Parsing:** `parseTime` handles three cases — full `M:SS.mmm`, raw digits of length ≤3 (milliseconds), ≤5 (seconds + ms), or 6 (minutes + seconds + ms) — and returns a `double` seconds value.
- **Totalling:** `calculateTotal` sums the three parsed sectors and renders the result as `M:SS.mmm`.
- **macOS hooks:** reflection against `com.apple.eawt.Application` sets the dock icon and About handler; all calls are wrapped in try/catch so the app runs fine on non-macOS systems.

---

## Project layout

```
F1SectorTimeCalculator/
├── src/f1sectortimecalculator/
│   ├── F1SectorTimeCalculator.java   # Application source
│   └── racing-car.png                # In-app icon
├── resources/                        # App icons (.icns, .iconset, PNG)
├── build.xml                         # Ant build script
├── manifest.mf                       # JAR manifest
├── nbproject/                        # NetBeans project metadata
├── dist/                             # Build output (JAR)
├── build/                            # Compiled classes
└── "F1 Sector Time Calculator.app"/  # macOS app bundle
```

---

## Known limitations

- Uses internal `com.apple.eawt.Application` APIs via reflection — these are not part of the public Java SE API and may need `--add-exports` / `--add-opens` flags on newer JDKs.
- Input is capped at 6 digits, so it supports lap times up to `9:59.999`.
- A duplicate copy of `F1SectorTimeCalculator.java` and `racing-car.png` exists under `build/classes/` — that's build output and is ignored by Git.

---

## License

Version 1.0. All rights reserved.
```
