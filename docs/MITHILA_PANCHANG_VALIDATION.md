# Mithila Panchang validation status
## Current implementation
- Solar/lunar apparent geocentric ecliptic longitudes: Astronomy Engine (MIT).
- Sidereal conversion: approximate Lahiri-style polynomial.
- Tithi, nakshatra, yoga and karana: angular divisions.
- Sunrise/sunset: supplied coordinates and local civil date.
- Rahu Kaal/Yamaganda: weekday-specific daylight eighths.
These are prototype calculations, not yet certified against a named published Mithila Panchang edition.

## Required reference dataset
For every comparison store exact Panchang title, publisher/editor, edition/year/page, coordinates, timezone, sunrise/sunset, tithi/nakshatra transition times, yoga, karana, ayanamsha convention and rounding rules.

## Test protocol
- At least 30 dates across weekdays, seasons, new/full moons and transition boundaries.
- Compare transition instants, not just noon snapshots.
- Record sunrise/sunset tolerance separately (reference horizon/refraction conventions matter).
- Cross-check with an independent ephemeris; preserve mismatches instead of tuning to force agreement.
- Commit a sourced CSV corpus before claiming alignment.

## Blocking dependency
No specific Mithila Panchang edition or permitted reference dataset has been supplied or independently identified. A numerical match cannot honestly be claimed until that reference is available. Never invent page citations or accuracy claims.
