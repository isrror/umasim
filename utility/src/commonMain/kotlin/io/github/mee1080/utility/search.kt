package io.github.mee1080.utility

expect fun toSimplifiedChinese(text: String): String
expect fun toTraditionalChinese(text: String): String

fun String.searchKey(): String = buildString(length) {
    for (char in this@searchKey) {
        when {
            char.isWhitespace() || char == '\u3000' -> Unit
            char.isLetterOrDigit() -> append(char.lowercaseChar())
        }
    }
}

private fun String.searchVariants(): Set<String> = buildSet {
    listOf(
        this@searchVariants,
        toSimplifiedChinese(this@searchVariants),
        toTraditionalChinese(this@searchVariants),
    ).forEach { variant ->
        val key = variant.searchKey()
        if (key.isNotBlank()) add(key)
    }
}

fun String.matchesSearch(query: String): Boolean = matchesSearchKey(query.searchKey())

fun String.matchesSearchKey(queryKey: String): Boolean {
    if (queryKey.isBlank()) return true
    val candidateVariants = searchVariants()
    val queryVariants = queryKey.searchVariants()
    if (candidateVariants.any { candidate ->
            queryVariants.any { query -> candidate.contains(query) || query.contains(candidate) }
        }
    ) return true

    return searchDistanceTo(queryKey) <= searchThreshold(queryKey)
}

fun String.searchDistanceTo(query: String): Double {
    val candidateVariants = searchVariants()
    val queryVariants = query.searchVariants()
    if (candidateVariants.isEmpty() || queryVariants.isEmpty()) return Double.POSITIVE_INFINITY
    var best = Double.POSITIVE_INFINITY
    candidateVariants.forEach { candidate ->
        queryVariants.forEach { queryVariant ->
            best = minOf(best, normalizedLevenshteinDistance(candidate, queryVariant))
        }
    }
    return best
}

private fun searchThreshold(queryKey: String): Double {
    return when (queryKey.length) {
        0, 1, 2 -> 0.5
        3, 4 -> 0.4
        else -> 0.33
    }
}

fun <T> Iterable<T>.filterBySearch(query: String, selector: (T) -> String): List<T> {
    if (query.isBlank()) return toList()
    return filter { selector(it).matchesSearch(query) }
}
