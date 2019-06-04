package com.example.notes.notetaking.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.notetaking.Activity.LoginActivity;
import com.example.notes.notetaking.Activity.MainActivity;
import com.example.notes.notetaking.Activity.RegisterActivity;
import com.example.notes.notetaking.Activity.UpdateInformationActivity;
import com.example.notes.notetaking.Manager.AppManager;
import com.example.notes.notetaking.Manager.NotesDB;
import com.example.notes.notetaking.Manager.UserManage;
import com.example.notes.notetaking.Model.MainUser;
import com.example.notes.notetaking.R;
import com.example.notes.notetaking.Util.FilePathUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    private RoundedImageView photoHead;
    private Button logoutButton;
    private Button cancelButton;
    private Button changeBtn;
    private TextView usernameText;
    private NotesDB dbManage;
    private UserManage userManage;
    private AlertDialog alert = null;      //提示框
    private AlertDialog.Builder builder = null;
    private int photoChoice;
    private Bitmap bitmap;
    private Uri imageUri;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine,null);
        userManage = new UserManage();
        //bitmap=BitmapFactory.decodeResource(getActivity().getResources(),R.mipmap.qqphoto);
        dbManage = new NotesDB(getActivity(),"data.db",null,1);
        photoHead = (RoundedImageView) view.findViewById(R.id.headphotoView_mine);
        if(MainUser.user.getHeadPhoto()=="") {
            photoHead.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.qqphoto));
        }
        else{
            File file = new File(MainUser.user.getHeadPhoto());
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(MainUser.user.getHeadPhoto());
                bitmap=getCroppedRoundBitmap(bitmap,800);
                photoHead.setImageBitmap(bitmap);
            }
        }
        usernameText=(TextView)view.findViewById(R.id.usernameText_mine);
        usernameText.setText(MainUser.user.getName());
        changeBtn = (Button)view.findViewById(R.id.changeBtn);
        //修改个人资料
        changeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UpdateInformationActivity.class);
                startActivity(intent);
            }
        });

        //点击头像，修改头像。
        photoHead.setOnClickListener(new View.OnClickListener(){

            final String [] choice=new String[]{"拍照","相册"};
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(getActivity());
                alert = builder.setTitle("请选择修改头像方式")
                        .setItems(choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"你选择了"+choice[which],Toast.LENGTH_SHORT).show();
                                if(which==0){
                                    /*
                                    调用拍照
                                     */
                                    photoChoice=1;
                                    takePhoto();
                                }
                                else if(which==1){
                                    /*
                                    调用相册
                                     */
                                    photoChoice=0;
                                    goXiangChe();
                                }
                            }
                        }).create();
                alert.show();
            }
        });
        //退出程序
        logoutButton = (Button)view.findViewById(R.id.logoutButton_mine);
        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //AppManager.getAppManager.AppExit(this);
                AppManager.finishAllActivity();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);

            }
        });
        //注销帐号
        cancelButton = (Button)view.findViewById(R.id.cancalButton_mine);
        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                userManage.deleteUser(dbManage.getWritableDatabase(),MainUser.user.getId());
                /*
                帐号注销成功后，返回登录界面
                 */
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    /*调用相机*/
    private void goXiangJi() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,"data");
        startActivityForResult(intent, 1);

       // File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
    }

    /*调用相册*/
    protected void goXiangChe() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 111);
    }
    //修改图片

    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }



    //得到结果
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            ContentResolver cr = getActivity().getContentResolver();
            try {
                if (photoChoice==0){
                    //从相册获得图片
                    imageUri= data.getData();
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageUri));
                    //获得路径
                }else {
                    //从相机获得图片

                    try {
                        //bitmap= getBitmapFormUri(imageUri);
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Bundle bundle = data.getExtras();//获取封装好传递过来的数据，整个图片的二进制流
                    //bitmap = data.getParcelableExtra("data");
                    //bitmap = (Bitmap) data.getExtras().get("data");
                }
                if (imageUri ==null){
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));
                }
                MainUser.user.setHeadPhoto(FilePathUtils.getRealPathFromUri(getActivity(), imageUri));
                userManage.changePhoto(dbManage.getWritableDatabase(),MainUser.user.getId(),MainUser.user.getHeadPhoto());
                //photoHead.setImageBitmap(changePhoto(bitmap,80));
                bitmap=getCroppedRoundBitmap(bitmap,800);
                photoHead.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //压缩图片，避免发生OOM
    public Bitmap getBitmapFormUri(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getActivity().getContentResolver().openInputStream(uri);

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480x800为标准
        float hh = 80f;//这里设置高度为800f
        float ww = 80f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = getActivity().getContentResolver().openInputStream(uri);
        Bitmap bitmap1 = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        //return compressImage(bitmap);//再进行质量压缩
        return bitmap1;
    }

    public void takePhoto() {
        //ivContent.setVisibility(View.VISIBLE);    //设置图片可见
        String f = System.currentTimeMillis()+".jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(),f);
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
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                Toast.makeText(getActivity(),"请开启存储权限",Toast.LENGTH_SHORT).show();
                return;
            }
            imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else {
            imageUri=Uri.fromFile(outputImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(intent, 1);
    }

    //压缩图片
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options<=0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
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
    //刷新页面
    @Override
    public void onResume(){
        super.onResume();
        /*
        if(bitmap!=null) {
            photoHead.setImageBitmap(bitmap);
        }
        */
        if(bitmap!=null) {
            File file = new File(MainUser.user.getHeadPhoto());
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(MainUser.user.getHeadPhoto());
                bitmap=getCroppedRoundBitmap(bitmap,800);
                photoHead.setImageBitmap(bitmap);
            }
        }
    }

}
