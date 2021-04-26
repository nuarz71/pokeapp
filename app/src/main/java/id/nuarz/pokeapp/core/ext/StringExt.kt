package id.nuarz.pokeapp.core.ext

fun String.clearWhiteSpace(): String {
    return replace("\\s".toRegex(), " ")
}