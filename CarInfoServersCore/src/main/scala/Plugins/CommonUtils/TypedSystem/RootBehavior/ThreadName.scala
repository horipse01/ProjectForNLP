package Plugins.CommonUtils.TypedSystem.RootBehavior

/** 给每个线程取个名字，根据名字确定线程 */
case class ThreadName(name:String)

/** 对于常用的线程，我们把它们的名字定义在这里，可以多方引用 */
object ThreadName{
  val mainHandlerThreadName:ThreadName=ThreadName("mainHandler")
}
