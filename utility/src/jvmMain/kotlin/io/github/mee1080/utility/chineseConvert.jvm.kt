package io.github.mee1080.utility

import com.github.houbb.opencc4j.util.ZhConverterUtil

actual fun toSimplifiedChinese(text: String): String = ZhConverterUtil.toSimple(text)

actual fun toTraditionalChinese(text: String): String = ZhConverterUtil.toTraditional(text)
