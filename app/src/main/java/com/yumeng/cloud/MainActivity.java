package com.yumeng.cloud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;



import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import cn.wecloud.storage.Cloud;
import cn.wecloud.storage.cloud.callback.OnFileUploadCallback;
import cn.wecloud.storage.cloud.down.DownloadManager;
import cn.wecloud.storage.cloud.model.CustomImageInfo;
import cn.wecloud.storage.cloud.model.FileInfo;

/**
 * 需要手动获取权限
 */
public class MainActivity extends AppCompatActivity {

    Button uploadBtn;
    Button uploadImgBtn;
    Button getThumbnailBtn;
    Button downImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cloud.Companion.init();
        uploadBtn = (Button) findViewById(R.id.uploadFile);
        uploadImgBtn = (Button) findViewById(R.id.uploadPicture);
        getThumbnailBtn = (Button) findViewById(R.id.getThumbnail);
        downImage = (Button)findViewById(R.id.downImage);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickVideoFile();
            }
        });
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFile();
            }
        });
        getThumbnailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setGetThumbnailBtn(cloud);
                downFile(Cloud.Companion.getNet());
            }
        });
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downFileImage(Cloud.Companion.getNet());
            }
        });
    }

    // 打开系统的文件选择器
    public void pickVideoFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
        this.startActivityForResult(intent, 1001);
    }

    public void pickImageFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent, 1002);
    }

    // 获取文件的真实路径
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        String path = Utils.getRealFilePath(MainActivity.this, uri);
        Log.e("pickFile", "path" + path);

        if (requestCode == 1001) {
            uploadFileFast(Cloud.Companion.getNet(), path);
//            uploadFile(cloud, path);
        } else if (requestCode == 1002) {
//            uploadPicture(Cloud.Companion.getNet(), path);
            uploadFileFast(Cloud.Companion.getNet(), path);
        }

    }

    private void uploadFile(Cloud cloud, String path) {
        cloud.doUploadFile(new File(path), new OnFileUploadCallback() {
            @Override
            public void onComplete() {
                Log.e("upload", "onComplete");
            }

            @Override
            public void onFailure(int code, @NotNull String message) {
                Log.e("upload", "onFailure:" + "code:" + code + " message :" + message);
            }

            @Override
            public void onStart() {
                Log.e("upload", "onStart");
            }

            @Override
            public void onSuccess(@NotNull FileInfo fileInfo) {
                Log.e("upload", "onSuccess:" + fileInfo.getUrl());
            }
        }, -1L, null, false);
    }

    private void uploadFileFast(Cloud cloud, String path) {
        cloud.doUploadFileFast(new File(path), new OnFileUploadCallback() {
            @Override
            public void onComplete() {
                Log.e("upload", "onComplete");
            }

            @Override
            public void onFailure(int code, @NotNull String message) {
                Log.e("upload", "onFailure:" + "code:" + code + " message :" + message);
            }

            @Override
            public void onStart() {
                Log.e("upload", "onStart");
            }

            @Override
            public void onSuccess(@NotNull FileInfo fileInfo) {
                if(fileInfo!=null){
                    Log.e("upload", "onSuccess: fileInfo not Null："+fileInfo.getId());
                }else{
                    Log.e("upload", "onSuccess: fileInfo  Null");
                }

            }
        }, -1L, null,false);
    }

    private void downFile(Cloud cloud) {
        try {
            String dirPath = FileUtils.INSTANCE.getSDPath() + "/WeCloud";
            FileUtils.INSTANCE.createSDDirectory(dirPath);
            Long sourceId = Long.parseLong("1319522008958111747");//1319517974218018819图片  1318805744392519682视频
            cloud.downFile(null, sourceId, dirPath, "good" + System.currentTimeMillis()+".jpg", new DownloadManager.OnDownloadListener() {
                @Override
                public void onStart() {
                    Log.e("downLoad","onStart");
                }

                @Override
                public void onProgress(long fileDownloadedSize, long total) {
                    Log.e("downLoad","onProgress:"+fileDownloadedSize);
                }

                @Override
                public void onSuccess(@NotNull File file) {
                    Log.e("downLoad","success");
                }

                @Override
                public void onFailure(@NotNull Throwable e) {
                    Log.e("downLoad","fail"+e.getMessage());
                }

                @Override
                public void onDownloaded(@NotNull File file) {
                    Log.e("downLoad","onDownloaded");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void downFileImage(Cloud cloud) {
        try {
            String dirPath = FileUtils.INSTANCE.getSDPath() + "/WeCloud";
            FileUtils.INSTANCE.createSDDirectory(dirPath);
            Long sourceId = Long.parseLong("1319517974218018819");
            CustomImageInfo optionImage = new CustomImageInfo();
            optionImage.setHeight(200);
            optionImage.setWidth(200);
            optionImage.setRotate(180.0);
            optionImage.setScale(1.2);
            optionImage.setQuality(0.8f);
            cloud.downImageFile(null, sourceId, dirPath, "Image" + System.currentTimeMillis()+".jpg",optionImage, new DownloadManager.OnDownloadListener() {
                @Override
                public void onStart() {
                    Log.e("downLoad","onStart");
                }

                @Override
                public void onProgress(long fileDownloadedSize, long total) {
                    Log.e("downLoad","onProgress:"+fileDownloadedSize);
                }
                @Override
                public void onSuccess(@NotNull File file) {
                    Log.e("downLoad","success");
                }

                @Override
                public void onFailure(@NotNull Throwable e) {
                    Log.e("downLoad","fail"+e.getMessage());
                }

                @Override
                public void onDownloaded(@NotNull File file) {
                    Log.e("downLoad","onDownloaded");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void setGetThumbnailBtn(Cloud cloud){
//        long useFileId = Long.parseLong("1318806404928290818");
//        String baseName = "test"+ System.currentTimeMillis();
//        String extension = "jpg";
//        String size = "800x600";
//        cloud.getVideoThumbnail(useFileId, baseName, extension, size, false, null, new OnFileUploadCallback() {
//            @Override
//            public void onComplete() {
//
//            }
//
//            @Override
//            public void onFailure(int code, @NotNull String message) {
//                Log.e("upload", "GetThumbnail:" + "code:" + code + " message :" + message);
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(@NotNull FileInfo fileInfo) {
//                Log.e("upload", "GetThumbnail:" + "fileId:" + fileInfo.getFileId() + " fileName :" + fileInfo.getFileName());
//            }
//        },null, -1L, null);
//    }

    private void uploadPicture(Cloud cloud, String path) {
        CustomImageInfo optionImage = new CustomImageInfo();
        optionImage.setHeight(200);
        optionImage.setWidth(200);
        optionImage.setRotate(180.0);
        optionImage.setScale(1.2);
        optionImage.setQuality(0.8f);
        cloud.doUploadPicture(new File(path), new OnFileUploadCallback() {
            @Override
            public void onComplete() {
                Log.e("upload", "Image_onComplete");
            }

            @Override
            public void onFailure(int code, @NotNull String message) {
                Log.e("upload", "Image_onFailure:" + "code:" + code + " message :" + message);
            }

            @Override
            public void onStart() {
                Log.e("upload", "Image_onStart");
            }

            @Override
            public void onSuccess(@NotNull FileInfo fileInfo) {
                Log.e("upload", "Image_onSuccess:" + fileInfo.getUrl());
            }
        }, optionImage, -1L, null, false);
    }

}
