package kr.summitsystems.springbukkit.util

import net.md_5.bungee.api.ChatColor
import java.awt.Color
import java.util.regex.Matcher
import java.util.regex.Pattern

object BukkitColorUtils {
    private val GRADIENT_PATTERN: Pattern = Pattern.compile("<gradient:(#[a-fA-F0-9]{6}):(#[a-fA-F0-9]{6})>(.*?)</gradient>")
    private val COLOR_PATTERN = Pattern.compile("<#([a-fA-F0-9]{6})>")

    fun parse(text: String): String {
        var result = text
        result = parseColor(result)
        result = parseGradient(result)
        return result
    }

    private fun parseColor(text: String): String {
        val matcher: Matcher = COLOR_PATTERN.matcher(text)
        val result = StringBuffer()
        while (matcher.find()) {
            val colorCode = matcher.group(1)
            val color = ChatColor.of("#$colorCode")
            matcher.appendReplacement(result, color.toString())
        }
        matcher.appendTail(result)
        return result.toString()
    }

    private fun parseGradient(text: String): String {
        val matcher: Matcher = GRADIENT_PATTERN.matcher(text)
        val result = StringBuffer()
        while (matcher.find()) {
            val startColor: String = matcher.group(1)
            val endColor: String = matcher.group(2)
            val content: String = matcher.group(3)
            val startChatColor = ChatColor.of(startColor)
            val endChatColor = ChatColor.of(endColor)
            val gradientText = applyGradient(content, startChatColor, endChatColor)
            matcher.appendReplacement(result, gradientText)
        }
        matcher.appendTail(result)
        return result.toString()
    }

    private fun applyGradient(text: String, startColor: ChatColor, endColor: ChatColor): String {
        val result = StringBuilder()
        for (i in text.indices) {
            val currentChar = text[i]
            val gradientColor = interpolateColor(startColor, endColor, i.toFloat() / text.length)
            result.append(gradientColor).append(currentChar)
        }
        return result.toString()
    }

    private fun interpolateColor(startColor: ChatColor, endColor: ChatColor, ratio: Float): ChatColor {
        val startRGB: Int = startColor.color.rgb
        val endRGB: Int = endColor.color.rgb
        val interpolatedRGB = interpolate(startRGB, endRGB, ratio)
        return ChatColor.of(Color(interpolatedRGB))
    }

    private fun interpolate(color1: Int, color2: Int, ratio: Float): Int {
        fun getRed(rgb: Int): Int {
            return rgb shr 16 and 0xFF
        }
        fun getGreen(rgb: Int): Int {
            return rgb shr 8 and 0xFF
        }
        fun getBlue(rgb: Int): Int {
            return rgb and 0xFF
        }
        val red = (getRed(color1) * (1 - ratio) + getRed(color2) * ratio).toInt()
        val green = (getGreen(color1) * (1 - ratio) + getGreen(color2) * ratio).toInt()
        val blue = (getBlue(color1) * (1 - ratio) + getBlue(color2) * ratio).toInt()
        return (red shl 16) + (green shl 8) + blue
    }
}