package kr.summitsystems.springbukkit.core.scheduler

interface BukkitTaskScheduler {
    fun schedule(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask

    fun scheduleAtFixedRate(periodTicks: Long, initialDelayTicks: Long = 0, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask

    fun scheduleWithFixedDelay(delayedTicks: Long, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask

    fun scheduleAsync(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask

    fun scheduleAsyncAtFixedRate(periodTicks: Long, initialDelayTicks: Long = 0, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask

    fun scheduleAsyncWithFixedDelay(delayedTicks: Long, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask
}