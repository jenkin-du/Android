package com.android.milkapp2.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.milkapp2.R;
import com.android.milkapp2.model.Image;
import com.android.milkapp2.model.SharedStory;
import com.android.milkapp2.network.SendMessageTask;
import com.android.milkapp2.util.DatagramParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetPhoto extends Activity {

    Button btnOK;
    Button btnAdd;
    Button btnKaca;
    Uri imageUri;
    ImageView pic;
    EditText et;
    Time time;
    String imageuri;
    String s;

    Bitmap bitmap;
    String imageFileName = "";
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.getphoto);

        btnOK = (Button) findViewById(R.id.btnSure);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnKaca = (Button) findViewById(R.id.btnKaca);
        pic = (ImageView) findViewById(R.id.pic);
        et = (EditText) findViewById(R.id.etxt);

        intent = getIntent();

        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent backIntent = new Intent();

                s = et.getText().toString();
                backIntent.putExtra("text", s);
                backIntent.putExtra("imageFileName", imageFileName);
                setResult(RESULT_OK, backIntent);

                try {
                    SharedStory story = new SharedStory();

                    String imageName = "image-" + createOrder(getNowTime());
                    story.setImageName(imageName);
                    story.setMessage(s);

                    //传故事
                    story.setTime(getNowTime());
                    String id = createOrder(getNowTime());
                    story.setId(id);
                    String lati = intent.getStringExtra("latitude");
                    String longi = intent.getStringExtra("longitude");
                    story.setLatitude(lati);
                    story.setLongitude(longi);

                    String jsonDatagram = DatagramParser.toJsonDatagram("sendStory", "story", story);

                    SendMessageTask task = new SendMessageTask(jsonDatagram, new Handler());
                    task.start();

                    //传照片
                    Image image = new Image();
                    image.setImageCodeFromFile(imageFileName);
                    image.setImageName(imageName);

                    jsonDatagram = DatagramParser.toJsonDatagram("sendImage", "image", image);

                    SendMessageTask imageTask = new SendMessageTask(jsonDatagram, new Handler());
                    imageTask.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                finish();
            }
        });

        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        btnKaca.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                time.setToNow();
//                String s = "pic" + time.format2445() + ".jpg";
//                File outputImage = new File(Environment.getExternalStorageDirectory(), s);
//                try {
//                    if (outputImage.exists())
//                        outputImage.delete();
//                    outputImage.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(GetPhoto.this, "Take Photo Fail", Toast.LENGTH_SHORT).show();
//                }
//                imageUri = Uri.fromFile(outputImage);
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, 1);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 1:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        bitmap = Media.getBitmap(this.getContentResolver(), imageUri);
//                        pic.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }

                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.v("TestFile", "SD card is not avaiable/writeable right now.");
                        return;
                    }

                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    pic.setImageBitmap(bitmap);


                    String filePath = Environment.getExternalStorageDirectory().getPath();
                    File file = new File(filePath + "/myImage/");
                    FileOutputStream bout = null;

                    if (!file.exists()) {
                        file.mkdirs(); // 创建文件夹
                    }

                    imageFileName = Environment.getExternalStorageDirectory()
                            .getPath() + "/myImage/1.jpg";

                    try {
                        bout = new FileOutputStream(imageFileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bout != null) {
                                bout.flush();
                            }
                            if (bout != null) {
                                bout.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
                case 2:

                    if (Build.VERSION.SDK_INT >= 19)
                        handleImageOnKitKat(data);
                    else
                        handleImageBeforeKitKat(data);

                    break;
                default:
                    break;
            }
        }


    }

    private void handleImageBeforeKitKat(Intent data) {
        // TODO Auto-generated method stub
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        // TODO Auto-generated method stub
        String imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            String docID = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docID.split(":")[1];
                String selection = Media._ID + "=" + id;
                imagePath = getImagePath(Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docID));
                imagePath = getImagePath(contentUri, null);

            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = getImagePath(imageUri, null);
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        // TODO Auto-generated method stub
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            pic.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "faild to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        // TODO Auto-generated method stub

        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    /**
     * 获取当前时间
     */
    public String getNowTime() {

        Date now = new Date(); //获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return dateFormat.format(now);
    }

    // 生成工单号
    public String createOrder(String str) {

        return str.replaceAll("[^0-9]", "");
    }

}
