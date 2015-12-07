package cn.apputest.ctria.section2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 主页面第二个Tab页 路查路检功能
 * 
 */
public class Section2Activity extends Activity {
	List<View> listViews;
	Context context = null;
	LocalActivityManager manager = null;
	TabHost tabHost = null;
	String username = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.section2activity);
		Log.i("Section2Activity", "Section2Activity");
		String aname = null;
		// username = QuairData("User_DB","userinformation","1",aname);
		context = Section2Activity.this;

		// 定放一个放view的list，用于存放viewPager用到的view
		listViews = new ArrayList<View>();

		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		Intent i1 = new Intent(context, Lucha1Activity.class);
		listViews.add(getView("A", i1));
		Intent i2 = new Intent(context, Lucha2Activity.class);
		listViews.add(getView("B", i2));

		tabHost = (TabHost) findViewById(R.id.tabhostL);
		tabHost.setup();
		tabHost.setup(manager);

		// 这儿主要是自定义一下tabhost中的tab的样式
		View tabIndicator1 =  LayoutInflater.from(this).inflate(
				R.layout.section2activity_luchatab, null);
		TextView tvTab1 = (TextView) tabIndicator1.findViewById(R.id.tvL);
		tvTab1.setText("车辆检查");

		View tabIndicator2 =  LayoutInflater.from(this).inflate(
				R.layout.section2activity_luchatab, null);
		TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tvL);
		tvTab2.setText("人员检查");

//		Intent intent = new Intent(context, EmptyActivity.class);
		// 注意这儿Intent中的activity不能是自身,所以我弄了个空的acitivity
		tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator1)
				.setContent(i1));
		tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2)
				.setContent(i2));

		// 点击tabhost中的tab时，要切换下面的viewPager
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				if ("A".equals(tabId)) {
					tabHost.setCurrentTab(0);
				}
				if ("B".equals(tabId)) {

					tabHost.setCurrentTab(1);
				}

			}
		});

	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	private class MyPageAdapter extends PagerAdapter {

		private List<View> list;

		private MyPageAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
}
