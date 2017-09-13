package com.roaddeum.imagecapturedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener{

    static int date;
    FileInputStream inputStream = null;
    ImageView imageView;
    Button captureButton, viewButton;
    static File directory, dir;
    Context context;
    Bitmap bitmap;


    private MediaPlayer mp;
    private TextureView textureView;

    public static String TAG = "TextureViewActivity";

    static String fileName = "";
    String videoURL = "rtsp://r5---sn-vgqsknee.googlevideo.com/Cj0LENy73wIaNAkEi_ePF_8v8BMYDSANFC1KU7lZMOCoAUIASARgjZmj6qTm1NxZigELOWI5ZWVQUEZXQVEM/3B3F1AE06CB6F6B7CA32CE5A055112A50268FE2D.878336DABEBAC592CA6E3591B44E0006F9CC877F/yt6/1/video.3gp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context  = getBaseContext();
        captureButton = (Button)findViewById(R.id.capture_button);
        viewButton = (Button)findViewById(R.id.open_capture_button);
        textureView = (TextureView)findViewById(R.id.VideoView2);
        textureView.setSurfaceTextureListener(this);
         captureButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                System.out.println("--------------capturing screen-----");
                bitmap = textureView.getBitmap();
                createFile();
            }
        });

       viewButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
                load(fileName);
           }
       });

    }


    @NonNull
    private File createFile() {
        date = (int)(System.currentTimeMillis()*1000);

        if(Environment.isExternalStorageEmulated()&& this.isExternalStorageWritable()){
            fileName = "" + date;
            directory = getAlbumStorageDir(fileName);
            System.out.println("Entered if...");
        }
        else {
            directory = this.getDir(fileName, Context.MODE_PRIVATE);
            System.out.println("Entered else instead");
        }

        Toast.makeText(getApplicationContext(), "Capturing Screenshot: " + fileName, Toast.LENGTH_SHORT).show();
        return new File(directory, fileName);
    }


    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("ImageSaver", "Directory not created");
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap load(String pathName) {

        dir = getFilesDir();

        try {

            AlertDialog.Builder captured = new AlertDialog.Builder(MainActivity.this);
            captured.setNegativeButton("OK", null);
            imageView = new ImageView(MainActivity.this);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(imageParams);
            captured.setView(imageView);
            AlertDialog alertDialog;
            alertDialog = captured.show();
            captured.wait(1);
            alertDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Loaded..." + pathName , Toast.LENGTH_LONG).show();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(getApplicationContext(), "After loading ..." + dir, Toast.LENGTH_LONG).show();
        return null;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        Surface s = new Surface(surface);

        try
        {
            mp = new MediaPlayer();
            mp.setDataSource(videoURL);
            mp.setSurface(s);
            mp.prepare();

            mp.setOnBufferingUpdateListener(this);
            mp.setOnCompletionListener(this);
            mp.setOnPreparedListener(this);
            mp.setOnVideoSizeChangedListener(this);

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.start();


        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
