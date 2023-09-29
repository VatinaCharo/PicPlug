package nju.eur3ka.ext

fun ByteArray.toHexString(): String = joinToString("") {  "%02x".format(it) }