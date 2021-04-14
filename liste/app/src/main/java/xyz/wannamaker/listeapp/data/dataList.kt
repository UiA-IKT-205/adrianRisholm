package xyz.wannamaker.listeapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class dataList(val title:String, val elements:MutableList<String>):Parcelable
