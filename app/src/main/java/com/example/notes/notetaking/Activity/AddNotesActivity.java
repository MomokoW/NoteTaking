package com.example.notes.notetaking.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
//整合User里面的UserID
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.CustomVideoView;
import com.example.notes.notetaking.Util.DateTime;
import com.example.notes.notetaking.Util.FilePathUtils;
import com.example.notes.notetaking.Util.GraffitiActivity;
import com.example.notes.notetaking.Util.MapUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class AddNotesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //调用系统相机和相册回调参数
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;
    private final int TAKE_PHOTO = 1;
    private final int CROP_PHOTO = 2;
    private final int REQUST_VIDEO = 3;

    //调用涂鸦界面的请求参数
    private final int REQUEST_GRQFFITI = 4;
    private Bitmap bmp;
    private int bmpflag=0;


    //标签内容，媒体路径
    final String items[] = {"未标签","生活","个人","旅游","工作"};
    final String picItems[] = {"拍照","从相册选择"};
    private String tag = "未标签";
    private String picPath = "";
    private String audioPath = "";
    private String videoPath = "";
    private String graffiti = "";

    //笔记内容,时间，图片路径
    private String content = "";
    private String dateNow;
    private String timeNow;
    private Uri ImageUri;

    //初始化控件
    private ImageButton btnSave;
    private Button addTag;
    private EditText editText;
    private TextView timeTv;
    private NotesDB notesDB;
    private ImageView ivContent,iv,ivGraffiti;
    private SQLiteDatabase dbWriter;
    private CustomVideoView video;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        initView();
    }
    public void initView(){

        //初始化导航栏Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle("编辑笔记");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //获取系统时间
        dateNow = DateTime.getTime();
        timeNow = dateNow.substring(12);

        //初始化视频播放控件
        video = ((CustomVideoView) findViewById(R.id.cv_video));
        iv = ((ImageView) findViewById(R.id.iv));
        videoPath = System.currentTimeMillis()+".jpg";

        //初始化Button及图片控件
        btnSave = (ImageButton)findViewById(R.id.btn_ok);
        addTag = (Button)findViewById(R.id.tag);
        editText = (EditText)findViewById(R.id.edit_note);
        timeTv = (TextView)findViewById(R.id.showtime);
        ivContent = (ImageView)findViewById(R.id.imageContent1);
        ivGraffiti = (ImageView)findViewById(R.id.imageGraffti);
        timeTv.setText(timeNow);

        //给按钮添加绑定事件
        btnSave.setOnClickListener(this);
        addTag.setOnClickListener(this);
        editText.setOnClickListener(this);

        //创建数据库对象
        dbWriter = getDataBase();

        //初始化底部导航栏
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.addnotes_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.setItemIconTintList(null);  //icon使用原色

        //播放视频控件
        MediaController controller = new MediaController(this);
        video.setMediaController(controller);
        if (video.isPlaying()){
            iv.setVisibility(View.INVISIBLE);
        }
        video.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                Toast.makeText(getApplicationContext(),"播放",Toast.LENGTH_SHORT).show();
                iv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPause() {
                Toast.makeText(getApplicationContext(),"暂停",Toast.LENGTH_SHORT).show();
                iv.setVisibility(View.VISIBLE);
            }
        });

    }
    ///点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                addNotes();
                finish();
                break;
            case R.id.tag:
                setTag();
                break;
        }
    }
    private void graffiti() {
        Intent intent = new Intent(getApplicationContext(),GraffitiActivity.class);
        startActivityForResult(intent,REQUEST_GRQFFITI);
    }
    //得到便笺的分类
    public void setTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择标签");
        //设置图标
        builder.setIcon(R.mipmap.login6);
        //设置单选按钮
        builder.setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = items[which];
                tag = item;
                addTag.setText(tag);
                //根据不同的标签来设置ImageView
                addTag.setCompoundDrawablesWithIntrinsicBounds(MapUtils.imageMap.get(tag),0,0,0);
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //播放视频
    private void playVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,videoPath);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(intent,REQUST_VIDEO);
    }
    //获取视频的二进制文件
    private Bitmap getVideoBitmap(String videoPath){
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath);
            Bitmap bitmap = retriever.getFrameAtTime();
            return bitmap;
        }finally {
            retriever.release();
        }
    }
    //获取视频的位图
    private Bitmap getVideoBitmap2(Uri uri){
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this,uri);
            Bitmap bitmap = retriever.getFrameAtTime();
            return bitmap;
        }finally {
            retriever.release();
        }
    }

    public void start(View view){
        iv.setVisibility(View.INVISIBLE);
        video.start();
    }

    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        notesDB = new NotesDB(this,"notes.db",null,1);
        return notesDB.getWritableDatabase();
    }

    //设置Toolbar上面的返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            dbWriter.close();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //添加底部导航栏点击事件的响应
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.picture:
                choosePic();
                break;
            case R.id.video:
                playVideo();
                break;
            case R.id.graffiti:
                graffiti();
                break;
            case R.id.audio:
                initSpeech(this);
                break;
        }
        return false;
    }

    //选择图片的方式
    public void choosePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择图片");
        //设置图标
        builder.setIcon(R.mipmap.login6);
        //设置单选按钮
        builder.setSingleChoiceItems(picItems,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = picItems[which];
                //根据不同的选择来获取图片
                switch(item)
                {
                    case "拍照":
                        takePhoto();
                        break;
                    case "从相册选择":
                        callGallery();
                        break;
                }
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //从图库添加图片
    public void callGallery() {
        ivContent.setVisibility(View.VISIBLE);	//设置图片可见
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);
    }

    //调用相机获取图片
    public void takePhoto() {
        ivContent.setVisibility(View.VISIBLE);    //设置图片可见
        //设置图片的路径
        picPath = System.currentTimeMillis()+".jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(),picPath);
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, outputImage.getAbsolutePath());
            //检查是否有存储权限，以免崩溃
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                return;
            }
            ImageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        }else {
            ImageUri=Uri.fromFile(outputImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        }
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
       startActivityForResult(intent, TAKE_PHOTO);
    }

    //获取图片后回调函数
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){// the onActivityResult() begin
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==TAKE_PHOTO) {//获取相机照片
            if(resultCode==RESULT_OK) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(ImageUri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
                startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
            }
        }
        if (requestCode == IMAGE_CODE) {//获取相册照片
            ContentResolver resolver = getContentResolver();
            try {
                if (data == null){  //未选中图片
                   ivContent.setVisibility(View.INVISIBLE);
                   return;
                }
                else {
                    //获取图片存储的路径
                    Uri originalUri = data.getData();
                    picPath = FilePathUtils.getRealPathFromUri(this, data.getData());
                    bmp = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    ivContent.setImageBitmap(bmp);
                    bmpflag = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == CROP_PHOTO)
        {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                    ivContent.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUST_VIDEO){
           if (resultCode == RESULT_OK){
                iv.setVisibility(View.VISIBLE);
                video.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                video.setVideoURI(uri);
//                Bitmap bitmap = getVideoBitmap(videoPath);
                Bitmap bitmap = getVideoBitmap2(uri);
                iv.setImageBitmap(bitmap);
            }
        }
        if(requestCode == REQUEST_GRQFFITI) {
            if(resultCode == RESULT_OK) {
                graffiti = data.getStringExtra("graffiti_data_return");
                Bitmap bitmap = BitmapFactory.decodeFile(graffiti);
                ivGraffiti.setVisibility(View.VISIBLE);
                ivGraffiti.setImageBitmap(bitmap);
            }
        }
    }//the onActivityResult() end

    // get the image    将图片转化为二进制流
    public byte[] getimage()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Resources res=getResources();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }//the end

    //将二进制码转化为bitmap图片
    public Bitmap getBitmapFromByte(byte[] temp)
    {   //将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

    //语音识别模块
    //语音识别当前的文字
    /**
     * 初始化语音识别
     */
    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    //返回的result为识别后的汉字,直接赋值到TextView上即可
                    String result = parseVoice(recognizerResult.getResultString());
                    content = editText.getText().toString()+" "+result;
                    editText.setText(content);
                }
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }
    public void startRecod(final Context context)
    {

    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 语音对象封装
     */
    public class Voice {
        public ArrayList<WSBean> ws;
        public class WSBean {
            public ArrayList<CWBean> cw;
        }
        public class CWBean {
            public String w;
        }
    }


    //添加便笺到程序中
    private void addNotes() {
        ContentValues cv = new ContentValues();
        content = editText.getText().toString();
//        cv.put(NotesDB.USER_ID,MainUser.user.getId());
        cv.put(NotesDB.USER_ID,"11111");
        cv.put(NotesDB.NOTES_TAG,tag);
        cv.put(NotesDB.NOTES_TIME,dateNow);
        cv.put(NotesDB.NOTES_CONTENT,content);
        cv.put(NotesDB.NOTES_PIC,picPath);
        cv.put(NotesDB.NOTES_AUDIO,audioPath);
        cv.put(NotesDB.NOTES_VIDEO,videoPath);
        cv.put(NotesDB.NOTES_GRAFIITI,graffiti);
        cv.put(NotesDB.NOTES_STATUS,"0");
        dbWriter.insert(NotesDB.TABLE_NOTE,null,cv);
        Toast.makeText(getApplicationContext(),"添加便笺成功!",Toast.LENGTH_LONG).show();
    }


}