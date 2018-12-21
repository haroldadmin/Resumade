package resumade.kshitijchauhan.haroldadmin.com.resumade.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Report(val errors: List<Error>): Parcelable

@Parcelize
data class Error(val code: String,
                 val message: String,
                 val path: String): Parcelable