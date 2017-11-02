package com.textmaxx.Utils;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.textmaxx.Interfaces.ImageClickListenerInterface;
import com.textmaxx.Interfaces.ImageClickListenerInterfaceRel;
import com.textmaxx.Interfaces.OnNotificationReceived;
import com.textmaxx.Interfaces.onRecyclerClick;

/**
 * Created by sumit on 18/10/16.
 */
public class InterfaceListener {
//    private static ImageClickListenerInterface imageClickListenerInterface2;
    private static ImageClickListenerInterface imageClickListenerInterface;
    private static ImageClickListenerInterfaceRel imageClickListenerInterfaceRel;
    private static OnNotificationReceived mOnNotificationReceived;
    private static onRecyclerClick monOnRecyclerClick;

    public static void getMonOnRecyclerClick(int pos) {
        monOnRecyclerClick.onRecyclerItemClick(pos);
    }

    public static void setMonOnRecyclerClick(onRecyclerClick monOnRecyclerClick) {
        InterfaceListener.monOnRecyclerClick = monOnRecyclerClick;
    }

//    private static OnNotificationReceived2 mOnNotificationReceived2;

    public static void getImageClickListenerInterface(ImageView imageView) {
        imageClickListenerInterface.ImageClick(imageView);
    }

    public static void setImageClickListenerInterface(ImageClickListenerInterface imageClickListenerInterface) {
        InterfaceListener.imageClickListenerInterface = imageClickListenerInterface;
    }

    public static void getimageClickListenerInterfaceRel(RelativeLayout imageView) {
        imageClickListenerInterfaceRel.ImageClickRel(imageView);
    }

    public static void setImageClickListenerInterfaceRel(ImageClickListenerInterfaceRel imageClickListenerInterfaceRel) {
        InterfaceListener.imageClickListenerInterfaceRel = imageClickListenerInterfaceRel;
    }


    public static void getOnNotificationReceived() {
        mOnNotificationReceived.Notificationreceived();
    }

    public static void setmOnNotificationReceived(OnNotificationReceived mOnNotificationReceived) {
        InterfaceListener.mOnNotificationReceived = mOnNotificationReceived;
    }


//
//    public static void getOnNotificationReceived2() {
//        mOnNotificationReceived2.Notificationreceived2();
//    }
//
//    public static void setmOnNotificationReceived2(OnNotificationReceived2 mOnNotificationReceived) {
//        InterfaceListener.mOnNotificationReceived2 = mOnNotificationReceived2;
//    }



//    public static void getimageClickListenerInterfaceInfo(ImageView imageView) {
//        imageClickListenerInterface.ImageClick(imageView);
//    }
//
//    public static void setImageClickListenerInterfaceInfo(ImageClickListenerInterface imageClickListenerInterface) {
//        InterfaceListener.imageClickListenerInterface = imageClickListenerInterface;
//    }

}
