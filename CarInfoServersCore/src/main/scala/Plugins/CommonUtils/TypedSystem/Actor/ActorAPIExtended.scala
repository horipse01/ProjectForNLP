package Plugins.CommonUtils.TypedSystem.Actor
import scala.reflect.runtime.universe._

abstract class ActorAPIExtended[Ret:TypeTag] extends ActorAPI{
  override type ReturnType = Ret
  override def getReturnTypeTag: TypeTag[Ret] = typeTag[Ret]
}
