package com.tencent.ugsv_flutter.manager;

import android.content.Context;
import com.tencent.ugc.TXUGCBase;
import com.tencent.xmagic.XMagicImpl;

public class LicenseManager {
    private static String ugcLicenceUrl = "";
    private static String ugcKey = "";

    private static String xmagicAuthLicenceUrl = "";
    private static String xmagicAuthKey = "";

    public static void setUgcLicense(Context context) {
        TXUGCBase.getInstance().setLicence(context, ugcLicenceUrl, ugcKey);
    }

    public static void setUgcLicense(Context context, String licenseUrl, String licenseKey) {
        TXUGCBase.getInstance().setLicence(context, licenseUrl, licenseKey);
    }

    public static void setXMagicLicense() {
        XMagicImpl.setXmagicAuthKeyAndUrl(xmagicAuthLicenceUrl, xmagicAuthKey);
    }

    public static void setXMagicLicense(String licenseUrl, String licenseKey) {
        XMagicImpl.setXmagicAuthKeyAndUrl(licenseUrl, licenseKey);
    }

    public static void setLicense(Context context) {
        TXUGCBase.getInstance().setLicence(context, ugcLicenceUrl, ugcKey);
        XMagicImpl.setXmagicAuthKeyAndUrl(xmagicAuthLicenceUrl, xmagicAuthKey);
    }
}
