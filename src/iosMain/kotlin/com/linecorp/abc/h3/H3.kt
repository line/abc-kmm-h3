package com.linecorp.abc.h3

import h3.GeoCoord
import h3.H3Cell
import h3.H3Utils

actual class H3 {

    actual companion object {

        actual fun geoToH3(geo: LatLng, res: Int) = H3Utils.geoToH3(GeoCoord(geo.lat, geo.lng), res)

        actual fun h3ToString(h3: ULong) = H3Utils.h3ToString(h3)

        actual fun vertices(h3Index: String) =
            H3Cell(h3Index).geoCoords
                .map { it as GeoCoord }
                .map { LatLng(it.lat, it.lng) }
    }
}