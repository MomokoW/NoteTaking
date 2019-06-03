package com.example.notes.notetaking.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.DateTime;
import com.example.notes.notetaking.Util.MapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AddNotesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int IMAGE_PICKER = 1001;
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;
    private final int TAKE_PHOTO = 1;
    private Bitmap bmp;
    private int bmpflag=0;
    private BottomNavigationView bottomNavigationView;

    //标签内容
    final String items[] = {"未标签","生活","个人","旅游","工作"};
    final String picItems[] = {"拍照","从相册选择"};
    private String tag = "未标签";
    private String mediaPath = "";

    //笔记内容,时间
    private String content = "";
    private String dateNow;
    private String timeNow;
    private Uri ImageUri;

    //初始化控件
    private Button btnSave,addTag;
    private EditText editText;
    private TextView timeTv;
    private NotesDB notesDB;
    private ImageView ivContent;
    private ImageView ivCamera;
    private SQLiteDatabase dbWriter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        initView();

    }

    public void initView(){
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle("编辑笔记");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //初始化按钮及变量
        dateNow = DateTime.getTime();
        timeNow = dateNow.substring(12);

        btnSave = (Button)findViewById(R.id.btn_ok);
        addTag = (Button)findViewById(R.id.tag);
        editText = (EditText)findViewById(R.id.edit_note);
        timeTv = (TextView)findViewById(R.id.showtime);
        ivContent = (ImageView)findViewById(R.id.imageContent1);
        ivCamera = (ImageView)findViewById(R.id.imageContent2);
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

    }

    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        notesDB = new NotesDB(this,"notes.db",null,1);
        return notesDB.getWritableDatabase();
    }
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
        }
        return false;
    }

    public void choosePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择图片");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
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

    public void callGallery() {
        ivContent.setVisibility(View.VISIBLE);	//设置图片可见
        setImage();
    }

    private void setImage() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);
    }

    public void takePhoto() {
        ivCamera.setVisibility(View.VISIBLE);
        String f = System.currentTimeMillis()+".jpg";
        File outputImage=new File(Environment.getExternalStoragePublicDirectory("").getPath()+f);

        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            ImageUri= FileProvider.getUriForFile(getApplicationContext(),
                    "com.example.camerralbumtest.fileprovider",outputImage);
        }else {
            ImageUri=Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    //得到便笺的分类
    public void setTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择标签");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
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

    private void addNotes() {
        ContentValues cv = new ContentValues();
        content = editText.getText().toString();
//        cv.put(NotesDB.USER_ID,MainUser.user.getId());
        cv.put(NotesDB.USER_ID,"11111");
        cv.put(NotesDB.NOTES_TAG,tag);
        cv.put(NotesDB.NOTES_TIME,dateNow);
        cv.put(NotesDB.NOTES_CONTENT,content);
        cv.put(NotesDB.MEDIA_PATH,mediaPath);
        cv.put(NotesDB.NOTES_STATUS,"0");
        dbWriter.insert(NotesDB.TABLE_NOTE,null,cv);
        Toast.makeText(getApplicationContext(),"添加便笺成功!",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){// the onActivityResult() begin
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK) {//获取相机照片
            if(requestCode==TAKE_PHOTO) {
                try {
                    //将拍摄的照片显示出来
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                    ivCamera.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == IMAGE_CODE) {//获取相册照片
            ContentResolver resolver = getContentResolver();
            try {
                Uri originalUri = data.getData();
                bmp = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                ivContent.setImageBitmap(bmp);
                bmpflag=1;
            } catch (IOException e) {
                e.printStackTrace();
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


}