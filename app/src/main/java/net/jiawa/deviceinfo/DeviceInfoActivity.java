package net.jiawa.mytools.deviceinfo;

import net.jiawa.mytools.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DeviceInfoActivity extends Activity {

	final String TAG = "DeviceInfo";
	final boolean DEBUG = true;
	final String SPLIT = ",";
	void debugLog(String msg){
		if(DEBUG){
			Log.i(TAG, msg);
		}
	}
	
	final String LDPI = "ldpi";
	final String MDPI = "mdpi";
	final String HDPI = "hdpi";
	final String XHDPI = "xhdpi";
	final String XXHDPI = "xxhdpi";
	final String XXXHDPI = "xxxhdpi";
	final String _400DPI = "400dpi";
	final String _560DPI = "560dpi";
	final String TVDPI = "tvdpi";
	
	TextView mTvBrand;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceinfo);
		
		mTvBrand = (TextView) this.findViewById(R.id.tv_deviceinfo_brand);
		initLayout();
		detectDeviceInfo();
	}
	
	TextView mWidth;
	TextView mHeight;
	TextView mDensity;
	TextView mDensityDpi;
	TextView mSmallestScreenWidthDp;
	TextView mDpi;
	TextView mSugest;
	private void initLayout(){
		RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_width);
		TextView title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("Width:");
		mWidth = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_height);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("Height:");
		mHeight = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_density);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("Density:");
		mDensity = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_density_dpi);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("DensityDpi:");
		mDensityDpi = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_dpi);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("Dpi:");
		mDpi = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_smallestScreenWidthDp);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("SmallestWidthDp:");
		mSmallestScreenWidthDp = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
		
		rl = (RelativeLayout) this.findViewById(R.id.deviceinfo_layout_suggest);
		title = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_title);
		title.setText("Suggest:");
		mSugest = (TextView) rl.findViewById(R.id.tv_deviceinfo_info_group_info);
	}

	private String getDPIString(int densityDpi){
		if(densityDpi == DisplayMetrics.DENSITY_LOW)
			return LDPI;   /* 120 */
		if(densityDpi == DisplayMetrics.DENSITY_MEDIUM)
			return MDPI;   /* 160 */
		if(densityDpi == DisplayMetrics.DENSITY_HIGH)
			return HDPI;   /* 240 */
		if(densityDpi == DisplayMetrics.DENSITY_XHIGH)
			return XHDPI;  /* 320 */
		if(densityDpi == DisplayMetrics.DENSITY_XXHIGH)
			return XXHDPI; /* 480 */
		if(densityDpi == DisplayMetrics.DENSITY_XXXHIGH)
			return XXXHDPI; /* 640 */
		if(densityDpi == DisplayMetrics.DENSITY_400)
			return _400DPI; /* 400 */
		if(densityDpi == DisplayMetrics.DENSITY_560)
			return _560DPI; /* 560 */
		if(densityDpi == DisplayMetrics.DENSITY_TV)
			return TVDPI; /* 213 */
		throw new RuntimeException("Unsupported densityDpi");
	}
	
	private void getDeviceInfoFromBuild(DeviceInfo di){
		di.model = android.os.Build.MODEL;
		di.device = android.os.Build.DEVICE;
		di.product = android.os.Build.PRODUCT;
	}
	
	private void getDeviceInfoFromDisplayMetrics(DeviceInfo di) {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		di.width = metric.widthPixels; // 宽度（PX）
		di.height = metric.heightPixels; // 高度（PX）
		di.density  = metric.density;     // 密度       0.75 / 1.0 / 1.5 / 2.0 / 3.0
		di.densityDpi = metric.densityDpi;  // 密度DPI 120  / 160 / 240 / 320 / 480
		di.dpi = getDPIString(di.densityDpi);
	}
	
	private void getDeviceInfoFromConfiguration(DeviceInfo di){
		// 参考网址:
		// http://blog.csdn.net/yangyanfengjiayou/article/details/19082715
		Configuration config = getResources().getConfiguration();
		di.smallestScreenWidthDp = config.smallestScreenWidthDp;
	}
	
	private void getDeviceInfoFromAndroidDetect(DeviceInfo di){
		di.androidDetectResult = this.getResources().getString(R.string.deviceinfo_sw_info);
	}
	
	private void detectDeviceInfo(){
		DeviceInfo di = new DeviceInfo();
		getDeviceInfoFromConfiguration(di);
		getDeviceInfoFromDisplayMetrics(di);
		getDeviceInfoFromAndroidDetect(di);
		getDeviceInfoFromBuild(di);
		debugLog(di.toString());
		showInfo(di);
	}
	
	private void showInfo(DeviceInfo di){
		mWidth.setText(String.valueOf(di.width));
		mHeight.setText(String.valueOf(di.height));
		mDensity.setText(String.valueOf(di.density));
		mDensityDpi.setText(String.valueOf(di.densityDpi));
		mDpi.setText(di.dpi);
		mSmallestScreenWidthDp.setText(String.valueOf(di.smallestScreenWidthDp));
		mSugest.setText(di.androidDetectResult);
		mTvBrand.setText(di.model);
	}
	
	class DeviceInfo {
		final String TAG = "DeviceInfo";
		String model = "";
		String device = "";
		String product = "";
		int width = 0;
		int height = 0; 
		float density  = 0f;
		int densityDpi = 0; 
		int smallestScreenWidthDp = 0;
		String dpi = "";
		String androidDetectResult = "";
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(TAG);
			sb.append(" { ");
				sb.append("model:"+model);                                 sb.append(SPLIT);
				sb.append("device:"+device);                               sb.append(SPLIT);
				sb.append("product:"+product);                             sb.append(SPLIT);
				sb.append("width:"+width);                                 sb.append(SPLIT);
				sb.append("height:"+height);                               sb.append(SPLIT);
				sb.append("density:"+density);                             sb.append(SPLIT);
				sb.append("densityDpi:"+densityDpi);                       sb.append(SPLIT);
				sb.append("smallestScreenWidthDp:"+smallestScreenWidthDp); sb.append(SPLIT);
				sb.append("dpi:"+dpi);                                     sb.append(SPLIT);
				sb.append("androidDetectResult:"+androidDetectResult);
			sb.append(" }");
			return sb.toString();
		}
	}
}
