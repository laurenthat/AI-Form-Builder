package com.sbma.linkup.nfc

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import com.sbma.linkup.nfc.extensions.toHex
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class AppNfcManager(
    private val activity: Activity,
    private val nfcAdapter: NfcAdapter?
) :
    DefaultLifecycleObserver, NfcAdapter.ReaderCallback {

    val liveTag: MutableStateFlow<String?> = MutableStateFlow(null)

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun enableReaderMode(
        flags: Int,
        extras: Bundle
    ) {
        nfcAdapter?.let {
            try {
                it.enableReaderMode(activity, this, flags, extras)
            } catch (ex: UnsupportedOperationException) {
                Timber.d("UnsupportedOperationException ${ex.message}", ex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun disableReaderMode() {
        nfcAdapter?.let {
            try {
                nfcAdapter.disableReaderMode(activity)
            } catch (ex: UnsupportedOperationException) {
                Timber.d("UnsupportedOperationException ${ex.message}", ex)
            }
        }
    }

    fun isSupported(): Boolean {
        return nfcAdapter != null
    }

    fun isEnabled(): Boolean {
        return nfcAdapter?.isEnabled ?: false
    }

    override fun onTagDiscovered(tag: Tag?) {
        if (tag != null) {
            Timber.d("OnTagDiscovered: ${tag.id.toHex()}")
            liveTag.value = tag.id.toHex()
        }
    }
}