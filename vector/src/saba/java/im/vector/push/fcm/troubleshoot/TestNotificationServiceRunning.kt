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
package im.vector.push.fcm.troubleshoot

//import android.support.v4.app.Fragment
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.fragments.troubleshoot.TroubleshootTest
import im.vector.services.EventStreamService

class TestNotificationServiceRunning(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_foreground_service_started_title) {

    override fun perform() {
        if (EventStreamService.isStopped()) {
            description = fragment.getString(R.string.settings_troubleshoot_test_foreground_service_started_failed)
            status = TestStatus.FAILED
            quickFix = null
        } else {
            description = fragment.getString(R.string.settings_troubleshoot_test_foreground_service_startedt_success)
            quickFix = null
            status = TestStatus.SUCCESS
        }
    }

}