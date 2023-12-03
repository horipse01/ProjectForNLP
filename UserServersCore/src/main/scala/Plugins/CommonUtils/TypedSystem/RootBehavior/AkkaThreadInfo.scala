package Plugins.CommonUtils.TypedSystem.RootBehavior

import akka.actor.typed.scaladsl.ActorContext

/** 初始化启动的各个Actor线程，每个线程都会调用setter初始化。setter会读入root: ActorContext[RootCommand]参数，此为root的监听地址。
 * 在初始化成功之后，setter会给root发送消息。 dependencies 是指当前的线程需要等待其他某些线程初始化成功之后才能够开始初始化。
 * 这些都是通过root统一调度的。*/
case class AkkaThreadInfo(setter: ActorContext[RootCommand]=>Unit, threadName:ThreadName, dependencies: List[ThreadName])
