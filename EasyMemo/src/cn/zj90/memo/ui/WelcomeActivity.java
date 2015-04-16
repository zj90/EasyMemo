package cn.zj90.memo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.zj90.memo.R;

public class WelcomeActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void beforeInit() {
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				forward(HomeActivity.class);
			}
		}, 1000);
	}
}
