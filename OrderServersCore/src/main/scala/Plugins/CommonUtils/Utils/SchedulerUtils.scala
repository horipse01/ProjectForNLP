package Plugins.CommonUtils.Utils

import monix.execution.schedulers.{ExecutorScheduler, SchedulerService}
import monix.execution.{ExecutionModel, Features, Scheduler, UncaughtExceptionReporter}

import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent._

object SchedulerUtils {
  def createScheduler(name: String = "default-scheduler",
                      corePoolSize: Int = Runtime.getRuntime.availableProcessors(),
                      maxThreads: Int = Int.MaxValue,
                      workQueue: BlockingQueue[Runnable] = new LinkedBlockingQueue[Runnable](),
                      rejectHandler: RejectedExecutionHandler = new AbortPolicy(),
                      preStart: Boolean = false,
                      reporter: UncaughtExceptionReporter = UncaughtExceptionReporter.default,
                      executionModel: ExecutionModel = ExecutionModel.Default): SchedulerService = {

    val threadFactory = new ThreadFactory {
      override def newThread(r: Runnable): Thread = {
        val thread = new Thread(r)
        thread.setName(name + "-" + thread.getId)
        thread.setDaemon(false)
        thread.setUncaughtExceptionHandler(reporter.asJava)
        thread
      }
    }

    val executor = new ThreadPoolExecutor(
      corePoolSize,
      maxThreads,
      60,
      TimeUnit.SECONDS,
      workQueue,
      threadFactory,
      rejectHandler
    )

    if (preStart) executor.prestartAllCoreThreads()

    ExecutorScheduler(executor, reporter, executionModel, Features.empty)
  }

  implicit lazy val defaultComputation: Scheduler = SchedulerUtils.createScheduler(
    "default-computation",
    Runtime.getRuntime.availableProcessors(),
    Runtime.getRuntime.availableProcessors(),
    new LinkedBlockingQueue[Runnable](),
    preStart = true
  )
}
