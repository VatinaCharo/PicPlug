package nju.eur3ka

enum class PicType(val ext: String, val header: String) {
    JPEG("jpg", "FFD8FFE0"),
    PNG("png", "89504E47"),
    GIF("gif", "47494638"),
    BMP("bmp", "424D"),
    UNKNOWN("unknown", "00FF00FF")
}