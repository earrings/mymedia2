package com.jinan.kernel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Myvideoplayer extends Activity implements OnPreparedListener,SurfaceHolder.Callback{
    //String path = Environment.getExternalStorageDirectory().getPath()+"/movie.mp4";
	private int vWidth,vHeight; 
	private Display currDisplay; 
	boolean isPause = false;
    public static String path;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    private Intent floatingwindow = null;
    String TAG = "android.intent.action.myvideoplayer";
    MyBroadcastReceiver mr;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     this.requestWindowFeature(Window.FEATURE_NO_TITLE);  //�ޱ�����
   //     this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // this.getWindow().setFlags(500, 600);

   //     this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //����Ϊ����
        setContentView(R.layout.main);
        getWindow().setFormat(PixelFormat.UNKNOWN);//ʹ����֧��͸����
         //��ʼ������� �����������
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);   
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//��������ʽ
        mediaPlayer.setDisplay(surfaceHolder);//����VideoӰƬ��SurfaceHolder����
        currDisplay = this.getWindowManager().getDefaultDisplay(); 
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	@Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        //mp.stop();
        mediaPlayer.stop();
        Myvideoplayer.this.finish();   //�ر���Ƶ�������Ľ���

	    }
	
	});
        floatingwindow = new Intent();
        floatingwindow.setClass(Myvideoplayer.this, FloatingService.class);
        mr = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAG);
        this.registerReceiver(mr, intentFilter);
        Intent intent = getIntent();
        path = intent.getStringExtra("moviename");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub     
        int position = this.mediaPlayer.getCurrentPosition();
        floatingwindow.putExtra("position", position);
        floatingwindow.putExtra("max",mediaPlayer.getDuration());
        floatingwindow.putExtra("visable",true);    //
        startService(floatingwindow);
        return super.onTouchEvent(event);
    }
    public void playVideo(String strPath){//�Զ��岥��ӰƬ����
        if(mediaPlayer.isPlaying()==true){
            mediaPlayer.reset();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(surfaceHolder);//����VideoӰƬ��SurfaceHolder����
        try{
            mediaPlayer.setDataSource(strPath);   //����MediaPlayer������Դ
            mediaPlayer.prepare();                  //׼��
        }
        catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        try{
            mediaPlayer.setDataSource(path);   //����MediaPlayer������Դ
            mediaPlayer.prepare();             //׼��
            mediaPlayer.start();               //����       
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        stopService(floatingwindow);
        this.unregisterReceiver(mr);
        if(this.mediaPlayer!=null)
        {
            this.mediaPlayer.release();    
            mediaPlayer = null;
        }
        super.onDestroy();
    }
    public class MyBroadcastReceiver extends BroadcastReceiver{   //�Զ���㲥������
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
             if(action.equals(TAG)) {
                 String flag = intent.getStringExtra("flag");
                 if(flag.equals("play"))
                 {
                     if(!mediaPlayer.isPlaying())
                         mediaPlayer.start();
                 }
                 else if(flag.equals("pause"))
                 {
                     if(mediaPlayer.isPlaying())
                         mediaPlayer.pause();     //��ͣ
                 }
                 else if(flag.equals("change"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();    
                     }//��ͣ
                     mediaPlayer.seekTo(pos);
                     mediaPlayer.start();
                 }
                 else if(flag.equals("forward"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();    
                     }//��ͣ
                     mediaPlayer.seekTo(pos);
                     mediaPlayer.start();
                 }
                 else if(flag.equals("backward"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();  
                     }//��ͣ
                     mediaPlayer.seekTo(pos);
                     mediaPlayer.start();
                 }
                 else
                 {
                     Myvideoplayer.this.finish();
                 }
             }
        }
    }
    
    @Override 
    public void onPrepared(MediaPlayer player) { 
        // ��prepare��ɺ󣬸÷������������������ǲ�����Ƶ 
           
        //����ȡ��video�Ŀ�͸� 
        vWidth = player.getVideoWidth(); 
        vHeight = player.getVideoHeight(); 
           
        if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){ 
            //���video�Ŀ���߸߳����˵�ǰ��Ļ�Ĵ�С����Ҫ�������� 
            float wRatio = (float)vWidth/(float)currDisplay.getWidth(); 
            float hRatio = (float)vHeight/(float)currDisplay.getHeight(); 
               
            //ѡ����һ���������� 
            float ratio = Math.max(wRatio, hRatio); 
               
            vWidth = (int)Math.ceil((float)vWidth/ratio); 
            vHeight = (int)Math.ceil((float)vHeight/ratio); 
               
            //����surfaceView�Ĳ��ֲ��� 
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight)); 
               
            //Ȼ��ʼ������Ƶ 
               
    //        player.start(); 
        } 
    } 
}