package service;


import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.allenliu.versionchecklib.core.AVersionService;
import com.example.lanzun.R;
import com.example.lanzun.tools.Utils;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 自动版本更新
 */

public class VersionService extends AVersionService {
    public VersionService(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implemented");

    }

    @Override
    public void onResponses(AVersionService service, String response) {
        Log.e("DemoService", response);
        //可以在判断版本之后在设置是否强制更新或者VersionParams
        //eg
        // versionParams.isForceUpdate=true;
        int versioncode= Utils.getVersionCode(VersionService.this);//版本号
        if (1>2) {
            showVersionDialog("http://down1.uc.cn/down2/zxl107821.uc/miaokun1/UCBrowser_V11.5.8.945_android_pf145_bi800_(Build170627172528).apk",
                    "检测到新版本", getString(R.string.updatecontent));
        }
//        or
//        showVersionDialog("http://www.apk3.com/uploads/soft/guiguangbao/UCllq.apk", "检测到新版本", getString(R.string.updatecontent),bundle);

    }

}
