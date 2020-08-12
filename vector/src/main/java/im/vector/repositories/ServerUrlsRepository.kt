/*
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.repositories
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import im.vector.BuildConfig
import im.vector.R
import im.vector.fetchurl.GetServerAddress

/**
 * Object to store and retrieve home and identity server urls
 */
object ServerUrlsRepository {

    // Keys used to store default servers urls from the referrer
    private const val DEFAULT_REFERRER_HOME_SERVER_URL_PREF = "default_referrer_home_server_url"
    private const val DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF = "default_referrer_identity_server_url"
    private const val URL_NOT_PROVIDED = "url_Not_provided"

    // Keys used to store current home server url and identity url
    const val HOME_SERVER_URL_PREF = "home_server_url"
    const val IDENTITY_SERVER_URL_PREF = "identity_server_url"

    /**
     * Save home and identity sever urls received by the Referrer receiver
     */
    fun setDefaultUrlsFromReferrer(context: Context, homeServerUrl: String, identityServerUrl: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    if (!TextUtils.isEmpty(homeServerUrl)) {
                        putString(DEFAULT_REFERRER_HOME_SERVER_URL_PREF, homeServerUrl)
                    }

                    if (!TextUtils.isEmpty(identityServerUrl)) {
                        putString(DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF, identityServerUrl)
                    }
                }
    }

    /**
     * Save home and identity sever urls entered by the user. May be custom or default value
     */
    fun saveServerUrls(context: Context, homeServerUrl: String, identityServerUrl: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    putString(HOME_SERVER_URL_PREF, homeServerUrl)
                    putString(IDENTITY_SERVER_URL_PREF, identityServerUrl)
                }
    }

    /**
     * Return last used home server url, or the default one from referrer or the default one from resources
     */
    fun getLastHomeServerUrl(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        return prefs.getString(HOME_SERVER_URL_PREF,
                prefs.getString(DEFAULT_REFERRER_HOME_SERVER_URL_PREF,
                        getDefaultHomeServerUrl(context)))
    }


    /**
     * Return last used identity server url, or the default one from referrer or the default one from resources
     */
    fun getLastIdentityServerUrl(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        return prefs.getString(IDENTITY_SERVER_URL_PREF,
                prefs.getString(DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF,
                        getDefaultIdentityServerUrl(context)))
    }

    /**
     * Return true if url is the default home server url form resources
     */
    fun isDefaultHomeServerUrl(context: Context, url: String) = url == getDefaultHomeServerUrl(context)

    /**
     * Return true if url is the default identity server url form resources
     */
    fun isDefaultIdentityServerUrl(context: Context, url: String) = url == getDefaultIdentityServerUrl(context)

    /**
     * Return default home server url from resources
     */
    fun getDefaultHomeServerUrl(context: Context): String {
        if (BuildConfig.IS_SABA) {
            if (BuildConfig.ALLOW_HOME_SERVER_CHANGE) {
                return context.getString(R.string.default_hs_server_url)
            } else {
                if (URL_NOT_PROVIDED != getServerUrlFromMdm(context)) {
                    Log.d("MBD", "Url Received: " + getServerUrlFromMdm(context))
                    return getServerUrlFromMdm(context)
                } else {
                    Log.d("MBD", "Returned : default_hs_server_url_saba")
                    return context.getString(R.string.default_hs_server_url_saba)
                }


            }
        } else {
            return context.getString(R.string.default_hs_server_url)
        }
    }

    /**
     * Return default identity server url from resources
     */
    fun getDefaultIdentityServerUrl(context: Context): String = context.getString(R.string.default_identity_server_url)

    /**
     * Return Server Address from mdm-agent Application
     */
    private fun getServerUrlFromMdm(context: Context): String {
        val getServerAddress = GetServerAddress(context)
        return if (null == getServerAddress.url) {
            URL_NOT_PROVIDED
        } else
            getServerAddress.url
    }
}