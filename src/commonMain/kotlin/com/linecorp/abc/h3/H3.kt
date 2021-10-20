package com.linecorp.abc.h3

expect class H3 {

    companion object {
        fun geoToH3(geo: LatLng, res: Int): ULong
        fun h3ToString(h3: ULong): String
        fun vertices(h3Index: String): List<LatLng>
    }
}

fun H3.Companion.geoToH3String(geo: LatLng, res: Int) = h3ToString(geoToH3(geo, res))

fun H3.Companion.polygon(h3Index: String) = Polygon(h3Index, vertices(h3Index))

fun H3.Companion.polygons(h3Indexes: List<String>) = h3Indexes.map { Polygon(it, vertices(it)) }