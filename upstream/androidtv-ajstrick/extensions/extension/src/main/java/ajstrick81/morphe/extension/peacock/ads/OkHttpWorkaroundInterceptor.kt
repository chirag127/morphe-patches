package ajstrick81.morphe.extension.peacock.ads

import okhttp3.Interceptor
import okhttp3.Response

/**
 * No-op pass-through interceptor. Exists only so buildOkHttpClient() chains
 * a second interceptor after AdBlockInterceptor, matching the original
 * client's interceptor count/order in case the Sky SDK inspects it.
 */
class OkHttpWorkaroundInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}
