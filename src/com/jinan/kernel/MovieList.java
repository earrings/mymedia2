//ӰƬ���б�
package com.jinan.kernel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
public class MovieList extends Activity{
 //   Button btnplay = null;
    final static int UPDATE = Menu.FIRST;
    final static int QUIT   = Menu.FIRST+1;
    private ListView lv;  
    ArrayList playList;
   // String path = Environment.getExternalStorageDirectory().getPath();//+"/movie.mp4";
   String path = "http://116.211.111.250/share.php?method=Share.download&cqid=1acc93049fd469acbe349ff025131a51&dt=53.e3050a26cec2eb1422c3ae24ecf9f4b2&e=1403107761&fhash=eae8797988d66421a32798437ae6674e537c29f8&fname=MOV008.mp4&fsize=15497693&nid=14029320224978335&scid=53&st=b4654676cf4300035c2118c2b3ff1004&xqid=198731395";
    String file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
     //   btnplay = (Button)findViewById(R.id.btnplay);
        setTitle("������̬԰");
        lv = (ListView) findViewById(R.id.lv);  
//        if (Environment.getExternalStorageState().equals(  
//                Environment.MEDIA_MOUNTED)) {  
//            File path = Environment.getExternalStorageDirectory();// ���SD��·��   
//            // File path = new File("/mnt/sdcard/");   
//            File[] files = path.listFiles();// ��ȡ   
//            getFileName(files);  
//        }  
        SimpleAdapter adapter = new SimpleAdapter(this, getPlayList(), R.layout.sd_list,  
                new String[] { "path" }, new int[] { R.id.mp4 });  
        lv.setAdapter(adapter);  
        for (int i = 0; i < playList.size(); i++) {  
            Log.i("zeng", "list.  name:  " + playList.get(i));  
        }
        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                    long id) {
                // TODO Auto-generated method stub
                //setTitle(lv.getItemAtPosition(position).toString()+":"+String.valueOf(lv.getItemIdAtPosition(position)));
                String str = lv.getItemAtPosition(position).toString();
             //   file = str.substring("{��Ƶ����=".length(), str.lastIndexOf("}"));
                String moviename = path;
                //setTitle(moviename+":"+lv.getItemAtPosition(position).toString());
                Intent intent = new Intent(MovieList.this,Myvideoplayer.class);
                intent.putExtra("moviename", moviename);
                startActivity(intent);
            }
        });
//        btnplay.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                /*
//                Intent intent = new Intent(MovieList.this,Myvideoplayer.class);
//                intent.putExtra("moviename", path);
//                startActivity(intent);
//                */
//            }
//        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        int i = Menu.FIRST;
        menu.add(i, UPDATE, i, "�����б�");
        menu.add(i, QUIT, i, "�˳�");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
        case UPDATE:
            File path = Environment.getExternalStorageDirectory();// ���SD��·��   
            File[] files = path.listFiles();// ��ȡ   
  //          getFileName(files);                   //�����б�
            break;
        case QUIT:            //�˳�
            this.finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
//    private void getFileName(File[] files) {   //��������ǰĿ¼�µ��ļ�
//        if (files != null) {// ���ж�Ŀ¼�Ƿ�Ϊ�գ�����ᱨ��ָ��   
//            for (File file : files) {  
//                /* if (file.isDirectory()) {  
//                    Log.i("zeng", "�����ļ�Ŀ¼��������1" + file.getName().toString()  
//                            + file.getPath().toString());  
//                    getFileName(file.listFiles());  
//                    Log.i("zeng", "�����ļ�Ŀ¼��������2" + file.getName().toString()  
//                            + file.getPath().toString());  
//                } else {  
//                   */ String fileName = file.getName();  
//                    if (fileName.endsWith(".mp4")||fileName.endsWith(".3gp")) {   
//                        HashMap map = new HashMap();  
//                        String s = fileName.substring(0,  
//                                fileName.lastIndexOf(".")).toString();  
//                        Log.i("zeng", "�ļ���mp4��3gp����   " + s);                         
//                        map.put("��Ƶ����", fileName);
//                        name.add(map);  
//                    }  
//                }  
//            }  
//        //}   
//    }  
    
  //�õ���Ƶ�б�
    private List<Map<String,String>> getPlayList(){
      playList = new ArrayList<Map<String,String>>();  
      HashMap<String,String> map1 = new HashMap<String,String>(); 
      HashMap<String,String> map2 = new HashMap<String,String>();
      HashMap<String,String> map3 = new HashMap<String,String>();
      map1.put("path", "��Ƶ1");
      map2.put("path", "��Ƶ2");
      map3.put("path", "��Ƶ3");
      playList.add(map1);
      playList.add(map2);
      playList.add(map3);
      return playList;
    }
}