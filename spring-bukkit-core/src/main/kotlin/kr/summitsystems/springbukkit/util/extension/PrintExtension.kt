package kr.summitsystems.springbukkit.util.extension

fun <T> T.print(prefix: String = ""): T = this.also { println(prefix + this.toString()) }