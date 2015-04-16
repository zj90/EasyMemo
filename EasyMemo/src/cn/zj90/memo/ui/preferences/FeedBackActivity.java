package cn.zj90.memo.ui.preferences;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.zj90.memo.R;
import cn.zj90.memo.mail.SimpleMailSender;
import cn.zj90.memo.ui.HomeActivity;

public class FeedBackActivity extends Activity implements OnClickListener
{
	private EditText etContent, etFrom;
	private Button bnSubmit;
	
	private String address = "easymemo123@163.com";
	private String password = "xorangecn";
	private String content, clientAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		initView();
		initEvent();
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	protected void initView()
	{
		etContent = (EditText) findViewById(R.id.feedback_content);
		etFrom = (EditText) findViewById(R.id.feedback_clientaddress);
		bnSubmit = (Button) findViewById(R.id.feedback_submit);
	}
	
	protected void initEvent()
	{
		bnSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.feedback_submit:
		    content = etContent.getText().toString();
		    clientAddress = etFrom.getText().toString().trim();
		    
		    new Thread()
            {

                @Override
                public void run()
                {
                    try
                    {
                        SimpleMailSender sender = new SimpleMailSender(address, password);
                        
                        
                        if (clientAddress.equals("") || clientAddress == null)
                        {
                            sender.send(address, "备忘录的匿名意见", content);
                            
                        }
                        else
                        {
                            sender.send(address, "来自<" + clientAddress + ">对于备忘录的意见", content);
                        }
                        
                    } catch (AddressException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (MessagingException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
            }.start();

            Toast.makeText(this, "谢谢你的反馈，意见已发送", 1000).show();
			break;
		}
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate(R.menu.feed_back, menu);     
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            onBackPressed();
            break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



}
