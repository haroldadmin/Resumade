package resumade.kshitijchauhan.haroldadmin.com.resumade.di.components

import dagger.Component
import resumade.kshitijchauhan.haroldadmin.com.resumade.MainActivity
import resumade.kshitijchauhan.haroldadmin.com.resumade.ResumadeApplication
import resumade.kshitijchauhan.haroldadmin.com.resumade.di.modules.ContextModule
import resumade.kshitijchauhan.haroldadmin.com.resumade.di.modules.RetrofitModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RetrofitModule::class])
interface AppComponent {

    fun inject(app: ResumadeApplication)
    fun inject(mainActivity: MainActivity)

}