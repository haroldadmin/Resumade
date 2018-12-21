package resumade.kshitijchauhan.haroldadmin.com.resumade

import android.app.Application
import resumade.kshitijchauhan.haroldadmin.com.resumade.di.components.AppComponent
import resumade.kshitijchauhan.haroldadmin.com.resumade.di.components.DaggerAppComponent
import resumade.kshitijchauhan.haroldadmin.com.resumade.di.modules.ContextModule

class ResumadeApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .contextModule(ContextModule(this.applicationContext))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

}