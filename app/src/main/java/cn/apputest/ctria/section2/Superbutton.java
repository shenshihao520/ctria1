package cn.apputest.ctria.section2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dcrc2.R;

/**
 * @author 作者Shihao Shen:
 * @version 创建时间：2015-11-23 下午5:11:11 类说明
 */
public class Superbutton extends RelativeLayout {

	private TextView textview;
	private ImageView imageview;
	private RelativeLayout relativelayout;

	public Superbutton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inti(context);
	}

	public Superbutton(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		inti(context);
	}

	public Superbutton(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		inti(context);
	}

	private void inti(Context context) {
		if (!isInEditMode()) {
			relativelayout = (RelativeLayout) LayoutInflater.from(context)
					.inflate(R.layout.superbutton, this, true);
			imageview = (ImageView) relativelayout
					.findViewById(R.id.SuperButton_I);
			textview = (TextView) relativelayout.findViewById(R.id.textView1);
		}
	}

	public void setText(String text) {
		textview.setText(text);
	}

	public void setImage(int id) {
		imageview.setImageResource(id);
	}
}
