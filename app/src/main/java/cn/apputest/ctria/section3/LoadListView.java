package cn.apputest.ctria.section3;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dcrc2.R;

/**
 * 
 * @author Shihao Shen 重写ListView 加入底部元素  顶部元素
 * 
 */
public class LoadListView extends ListView implements OnScrollListener {
	View footer;// 底部布局；
	View header; // 顶部布局
	int totalItemCount;// 总数量
	int firstVisibleItem; // 第一个可见的item位置
	int lastVisibleItem;// 最后一个可见的item；
	boolean isLoading;// 正在加载；
	ILoadListener iLoadListener;
	IReflashLisener iReflashLisener;
	int headerHeight;
	boolean isRemark; // 标记 当前实在listview最顶端按下的
	int startY;

	int state; // 当前的状态
	final int NONE = 0;// 正常状态
	final int PULL = 1;// 下拉可以刷新
	final int RELESE = 2;// 松开释放在刷新
	final int REFLASHING = 3;// 正在刷新

	int scrollState;// 当期滚动状态

	public LoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 添加底部加载提示布局到listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(
				R.layout.section3activity_uploadtask_footerlayout, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		header = inflater.inflate(R.layout.section4activity_headerlayout, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.addFooterView(footer); //
		this.setOnScrollListener(this);
	}

	/**
	 * 通知父布局 我到底站多大地方
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 滑动方法
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.scrollState = scrollState;
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading) {
				isLoading = true;
				footer.findViewById(R.id.load_layout).setVisibility(
						View.VISIBLE);
				// 加载更多
				iLoadListener.onLoad();
			}
		}
	}
/**
 * 手势触发
 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			scrollState = 1;
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RELESE) {
				state = REFLASHING;
				reflashViewByState();
				iReflashLisener.onReflash();
			} else if (state == PULL) {
				isRemark = false;
				state = NONE;
				reflashViewByState();
			} 
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 判断移动过程中的操作
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY; // 滑动的距离
		int toPadding = space - headerHeight;
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL:
			topPadding(toPadding);
			if (space > headerHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewByState();
			}
//			else
//			{
//				state = RELESE;
//			}

			break;
		case RELESE:
			topPadding(toPadding);
			if (space < headerHeight + 30) {
				state = PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = NONE;
				reflashViewByState();
				isRemark = false;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 加载完毕
	 */
	public void loadComplete() {
		isLoading = false;
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
	}

	public void setInterface(ILoadListener iLoadListener,
			IReflashLisener iReflashLisener) {
		this.iLoadListener = iLoadListener;
		this.iReflashLisener = iReflashLisener;
	}

	// 加载更多数据的回调接口
	public interface ILoadListener {
		void onLoad();
	}

	/**
	 * 
	 * @param toPadding
	 */
	private void topPadding(int toPadding) {
		header.setPadding(header.getPaddingLeft(), toPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
/**
 * 刷新UI  下拉  松开
 */
	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tip);
		ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
		ProgressBar process = (ProgressBar) header.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			topPadding(-headerHeight);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			process.setVisibility(View.GONE);
			tip.setText("下拉可以刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			process.setVisibility(View.GONE);
			tip.setText("松开可以刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case REFLASHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			process.setVisibility(View.VISIBLE);
			tip.setText("正在刷新...");
			arrow.clearAnimation();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取完数据
	 */
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) header
				.findViewById(R.id.lastupdate_time);
		SimpleDateFormat formate = new SimpleDateFormat("hh:mm");
		Date date = new Date(System.currentTimeMillis());
		String time = formate.format(date);
		lastupdatetime.setText("上次刷新时间：" + time);
	}

	/**
	 * 刷新数据接口
	 * 
	 * @author Shihao Shen
	 * 
	 * 
	 */
	public interface IReflashLisener {
		void onReflash();
	}
}
