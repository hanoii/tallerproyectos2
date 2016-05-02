package sebastian.orderTracker.listeners;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Senastian on 02/05/2016.
 */
class EndCallListener extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(TelephonyManager.CALL_STATE_RINGING == state) {
            Log.i("CALL", "RINGING, number: " + incomingNumber);
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
            Log.i("CALL", "OFFHOOK");
        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app
            Log.i("CALL", "IDLE");

        }
    }
}