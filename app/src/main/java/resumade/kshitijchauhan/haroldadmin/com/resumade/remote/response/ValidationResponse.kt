package resumade.kshitijchauhan.haroldadmin.com.resumade.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidationResponse(
    val isValid: Boolean,
    val report: Report): Parcelable