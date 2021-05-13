package ml.moneo.app.util

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private var instance: ApolloClient? = null
private const val token = ""

fun apolloClient(): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.builder()
        .serverUrl("https://staging.moneo.ml/api")
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor())
                .build()
        )
        .build()

    return instance!!
}

private class AuthorizationInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}
