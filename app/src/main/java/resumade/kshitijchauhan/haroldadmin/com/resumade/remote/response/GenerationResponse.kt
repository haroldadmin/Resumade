package resumade.kshitijchauhan.haroldadmin.com.resumade.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenerationResponse(
    val html: String?,
    val report: Report?): Parcelable