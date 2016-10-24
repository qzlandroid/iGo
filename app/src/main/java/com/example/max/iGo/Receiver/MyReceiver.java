package com.example.max.iGo.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.max.iGo.UserDefined.HomeMessageActivity;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
   //     throw new UnsupportedOperationException("Not yet implemented");
        String action=intent.getAction();
        if (action.equals("cn.jpush.android.intent.NOTIFICATION_OPENED")){
            Intent in=new Intent(context, HomeMessageActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPP++");

        }
    }
}
