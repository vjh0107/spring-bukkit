package kr.summitsystems.springbukkit.core.util.extension

fun <T> T.print(prefix: String = ""): T = this.also { println(prefix + this.toString()) }