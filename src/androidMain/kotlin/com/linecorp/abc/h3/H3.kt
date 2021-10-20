package com.linecorp.abc.h3

import com.uber.h3core.H3Core

actual class H3 {

    actual companion object {

        actual fun geoToH3(geo: LatLng, res: Int) = H3Core.newSystemInstance().geoToH3(geo.lat, geo.lng, res).toULong()

        actual fun h3ToString(h3: ULong) = H3Core.newSystemInstance().h3ToString(h3.toLong())

        actual fun vertices(h3Index: String) =
            H3Core.newSystemInstance().h3ToGeoBoundary(h3Index).map { LatLng(it.lat, it.lng) }
    }
}