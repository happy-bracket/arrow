package arrow.effects.typeclasses

import arrow.Kind
import arrow.typeclasses.MonadSyntax
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension

@RestrictsSuspension
interface ConcurrentSyntax<F> : AsyncSyntax<F>, Concurrent<F>, FxSyntax<F>

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
open class ConcurrentContinuation<F, A>(private val CF: Concurrent<F>, override val context: CoroutineContext = EmptyCoroutineContext) :
  AsyncContinuation<F, A>(CF), Concurrent<F> by CF, ConcurrentSyntax<F> {
  override val fx: ConcurrentFx<F> = CF.fx
  override fun <B> binding(c: suspend MonadSyntax<F>.() -> B): Kind<F, B> = fx.monad(c)

  override fun <A> async(fa: Proc<A>): Kind<F, A> = CF.async(fa)
  override fun <A> asyncF(k: ProcF<F, A>): Kind<F, A> = CF.asyncF(k)
}