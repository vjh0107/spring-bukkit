package kr.summitsystems.springbukkit.checker.annotation

import kr.summitsystems.springbukkit.checker.ThreadCheckFailureException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.bukkit.Server

@Suppress("UNUSED_PARAMETER", "ArgNamesWarningsInspection")
@Aspect
class ThreadCheckerAdvisor(private val server: Server) {

    @Pointcut("@annotation(ensuresMainThread)")
    fun ensuresMainThreadPointcut(ensuresMainThread: EnsuresMainThread) {}

    @Pointcut("@annotation(ensuresAsyncThread)")
    fun ensuresAsyncThreadPointcut(ensuresAsyncThread: EnsuresAsyncThread) {}

    @Before("ensuresMainThreadPointcut(ensuresMainThread)")
    fun beforeEnsuresMainThread(joinPoint: JoinPoint, ensuresMainThread: EnsuresMainThread) {
        if (!server.isPrimaryThread) {
            throw ThreadCheckFailureException.MainThread(joinPoint.signature.name, Thread.currentThread())
        }
    }

    @Before("ensuresAsyncThreadPointcut(ensuresAsyncThread)")
    fun beforeEnsuresAsyncThread(joinPoint: JoinPoint, ensuresAsyncThread: EnsuresAsyncThread) {
        if (server.isPrimaryThread) {
            throw ThreadCheckFailureException.AsyncThread(joinPoint.signature.name, Thread.currentThread())
        }
    }
}