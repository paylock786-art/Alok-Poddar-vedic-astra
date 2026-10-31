# Free / no-paid-service policy

VEDIC ASTRA is being developed to avoid paid APIs, paid ephemeris subscriptions, paid image APIs, and paid cloud services.

## Astronomy calculations
- Astronomy Engine by Don Cross is included as a free, open-source Kotlin/JVM dependency (MIT License).
- Upstream: https://github.com/cosinekitty/astronomy
- Accuracy claim from upstream: approximately 1 arcminute for supported astronomical calculations. Astrology-specific sidereal conversion and Panchanga rules are our own integration and must be validated separately.
- The current Lahiri ayanamsha implementation is an approximation. Panchanga values and ascendant/house calculations must be cross-checked against published almanacs and independent reference software before being represented as production-grade.
- No paid Swiss Ephemeris license or paid API is required by this integration.

## Other no-cost components
- Android Jetpack Compose / AndroidX and Coil are open-source dependencies.
- App data is stored locally on the device unless a future feature explicitly requires a server.
- No subscription, paid API key, or paid cloud backend is planned.

## Important
"Free" does not remove third-party license obligations. Keep the upstream MIT notice and this attribution in distributed builds. Free third-party devotional images must be checked for their own reuse/redistribution terms; online availability alone is not proof of commercial redistribution rights.
