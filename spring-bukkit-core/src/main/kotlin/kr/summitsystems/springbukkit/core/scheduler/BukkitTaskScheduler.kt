package kr.summitsystems.springbukkit.core.scheduler

interface BukkitTaskScheduler {
    fun schedule(task: () -> Unit): BukkitScheduledTask

    fun scheduleAtFixedRate(periodTicks: Long, initialDelayTicks: Long = 0, task: () -> Unit): BukkitScheduledTask

    fun scheduleWithFixedDelay(delayedTicks: Long, task: () -> Unit): BukkitScheduledTask
}