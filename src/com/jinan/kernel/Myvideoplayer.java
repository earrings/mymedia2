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
   //     this.requestWindowFeature(Window.FEATURE_NO_TITLE);  //无标题栏
   //     this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // this.getWindow().setFlags(500, 600);

   //     this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置为竖屏
        setContentView(R.layout.main);
        getWindow().setFormat(PixelFormat.UNKNOWN);//使窗口支持透明度
         //初始化相关类 设置相关属性
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);   
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置流格式
        mediaPlayer.setDisplay(surfaceHolder);//设置Video影片以SurfaceHolder播放
        currDisplay = this.getWindowManager().getDefaultDisplay(); 
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	@Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        //mp.stop();
        mediaPlayer.stop();
        Myvideoplayer.this.finish();   //关闭视频播放器的界面

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
    public void playVideo(String strPath){//自定义播放影片函数
        if(mediaPlayer.isPlaying()==true){
            mediaPlayer.reset();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(surfaceHolder);//设置Video影片以SurfaceHolder播放
        try{
            mediaPlayer.setDataSource(strPath);   //设置MediaPlayer的数据源
            mediaPlayer.prepare();                  //准备
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
            mediaPlayer.setDataSource(path);   //设置MediaPlayer的数据源
            mediaPlayer.prepare();             //准备
            mediaPlayer.start();               //播放       
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
    public class MyBroadcastReceiver extends BroadcastReceiver{   //自定义广播接受者
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
                         mediaPlayer.pause();     //暂停
                 }
                 else if(flag.equals("change"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();    
                     }//暂停
                     mediaPlayer.seekTo(pos);
                     mediaPlayer.start();
                 }
                 else if(flag.equals("forward"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();    
                     }//暂停
                     mediaPlayer.seekTo(pos);
                     mediaPlayer.start();
                 }
                 else if(flag.equals("backward"))
                 {
                     int pos = intent.getIntExtra("newpos",0);
                     if(mediaPlayer.isPlaying()){
                         mediaPlayer.pause();  
                     }//暂停
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
        // 当prepare完成后，该方法触发，在这里我们播放视频 
           
        //首先取得video的宽和高 
        vWidth = player.getVideoWidth(); 
        vHeight = player.getVideoHeight(); 
           
        if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){ 
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放 
            float wRatio = (float)vWidth/(float)currDisplay.getWidth(); 
            float hRatio = (float)vHeight/(float)currDisplay.getHeight(); 
               
            //选择大的一个进行缩放 
            float ratio = Math.max(wRatio, hRatio); 
               
            vWidth = (int)Math.ceil((float)vWidth/ratio); 
            vHeight = (int)Math.ceil((float)vHeight/ratio); 
               
            //设置surfaceView的布局参数 
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight)); 
               
            //然后开始播放视频 
               
    //        player.start(); 
        } 
    } 
}