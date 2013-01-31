/**
 * 
 */
package net.wisedog.android.whooing.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class SmsReceiver extends BroadcastReceiver {

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }

    }
   /* public void onReceive1(Context paramContext, Intent paramIntent)
    {
      int i = 0;
      new StringBuilder("#인텐트_리시버 시작### ").append(paramIntent.getAction()).toString();
      Object[] arrayOfObject;
      SmsMessage[] arrayOfSmsMessage;
      int j;
      if (paramIntent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
      {
        Bundle localBundle = paramIntent.getExtras();
        if (localBundle != null)
        {
          arrayOfObject = (Object[])localBundle.get("pdus");
          arrayOfSmsMessage = new SmsMessage[arrayOfObject.length];
          if (arrayOfObject != null)
            j = 0;
        }
      }
      while (true)
      {
        je localje;
        Long localLong2;
        int m;
        if (j >= arrayOfObject.length)
        {
          localje = new je();
          Long localLong1 = Long.valueOf(111L);
          int k = arrayOfSmsMessage.length;
          localLong2 = localLong1;
          m = 0;
          if (m < k)
            break label403;
          if ((localje.b != null) && (localje.b.contains("+")))
            localje.b = localje.b.replaceAll("+", "");
          new StringBuilder("#인텐트_수신문자 = ").append(localje.c).toString();
          if (localje.c.length() > 10)
          {
            localje.c = localje.c.replaceAll("[\r\n]", " ");
            localje.c = localje.c.replaceAll("[^가-힣xfe0-9０-９Ａ-Ｚａ-ｚa-zA-Z\\s()（）,-:.*_#&+$!<>\\]\\[/]", " ");
            if ((localje.b == null) || (localje.b.length() <= 0))
              localje.b = "02114";
          }
        }
        try
        {
          localje.d = String.valueOf(localLong2);
          String str = PreferenceManager.getDefaultSharedPreferences(paramContext).getString("Forward", "01000");
          if (new gs().a(localje.c, localje.b, str))
          {
            new StringBuilder("#인텐트_문자수신->SPAM체크통과#telno=").append(localje.b).toString();
            localhi = new hi(paramContext);
            localhi.a();
            if (!localhi.a(localje))
              break label490;
            localhi.b(localje);
            localhi.b();
            i = 1;
            if (i != 0)
              new hu(localje, paramContext, true, true, "broadcast");
          }
          return;
          arrayOfSmsMessage[j] = SmsMessage.createFromPdu((byte[])arrayOfObject[j]);
          j++;
          continue;
          label403: SmsMessage localSmsMessage = arrayOfSmsMessage[m];
          localje.c += localSmsMessage.getDisplayMessageBody();
          localje.b = localSmsMessage.getDisplayOriginatingAddress();
          localLong2 = Long.valueOf(localSmsMessage.getTimestampMillis());
          m++;
        }
        catch (Exception localException)
        {
          while (true)
          {
            hi localhi;
            localje.d = String.valueOf(new Date().getTime());
            continue;
            label490: localhi.b();
          }
        }
      }
    }*/

}
