package resumade.kshitijchauhan.haroldadmin.com.resumade.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import resumade.kshitijchauhan.haroldadmin.com.resumade.remote.Config
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: Interceptor, cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(cache)
            .build()

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BASIC }
            .also { return it }

    @Singleton
    @Provides
    fun provideCache(context: Context): Cache = Cache(context.cacheDir, 5 * 1024 * 1024)
}