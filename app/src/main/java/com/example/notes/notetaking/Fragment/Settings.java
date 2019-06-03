package com.example.notes.notetaking.Fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
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
    private Uri imageUrl;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine,null);
        userManage = new UserManage();
        dbManage = new NotesDB(getActivity(),"data.db",null,1);
        photoHead = (RoundedImageView) view.findViewById(R.id.headphotoView_mine);
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
                                    goXiangJi();
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
    public Bitmap changePhoto(Bitmap bitmap,int edgeLength){
        if(null == bitmap || edgeLength <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLength && heightOrg > edgeLength)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

    //得到结果
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getActivity().getContentResolver();
            try {
                if (photoChoice==0){
                    //从相册获得图片
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                }else {
                    //从相机获得图片
                    //Bundle bundle = data.getExtras();//获取封装好传递过来的数据，整个图片的二进制流
                    bitmap = data.getParcelableExtra("data");
                    //bitmap = (Bitmap) data.getExtras().get("data");
                }
                if (uri ==null){
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));
                }
                photoHead.setImageBitmap(changePhoto(bitmap,80));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
