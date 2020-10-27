# we-cloud-android-sdk

root build.gradle

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


app build.gradle 

    /**   implementation 'com.github.shengqianlong:we-cloud-android-sdk:1.0.0' 


申请Key 最好 配置在主Module的 AndroidManiFest.xml 里
	
	<Application>
		<meta-data
		    android:name="BucketId"
		    android:value="key_你的buckId"
		    />
		<meta-data
		    android:name="AccessKey"
		    android:value="你的AccessKey"
		    />
		<meta-data
		    android:name="SecretKey"
		    android:value="你的SecretKey"
		    />
	</Application>
	
	
使用网络框架为

    /**   RxJava+Retrofit+OkHttp

涉及到的jar：

    /**  implementation "com.squareup.retrofit2:retrofit:2.4.0"
  
    /**  implementation "com.squareup.retrofit2:retrofit-converters:2.4.0"
  
    /**  implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
  
    /**       { exclude group: 'io.reactivex.rxjava2' }
          
    /**   implementation "com.squareup.retrofit2:converter-gson:2.4.0"
  
    /**  implementation "io.reactivex.rxjava2:rxjava:2.2.8"
  
    /**  implementation "io.reactivex.rxjava2:rxandroid:2.1.1"
  
  
  使用方式
  MyApplication oncreate 方法下
  
     override fun onCreate() {
        super.onCreate()
        Cloud.initContext(this)
    }
  
  
  在Activity onCreate 方法下
  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cloud.Companion.init();
       }
  
  调用方法
  
    /**  Cloud.Companion.getNet().doUploadFile(file,OnFileUploadCallback)
   

  
  提供的上传下载方法有以下：
  
      /**
     * 上传单个文件
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadFile(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    )
    
    
      /**
     * 单个文件秒传
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadFileFast(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            expired: Long? = -1,
            customId: String? = null,
            cover:Boolean?=false
    )
    
      /**
     * 上传单个图片
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * customImageInfo: 图片的一些参数,不要可以传 null
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadPicture(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            optionImage: CustomImageInfo? = null,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    ) 
    
    
     /**
     * 下载文件
      *customId 为自定义的id  （如果上传的时候有传）
      *userFileId  上传完成后返回回来的fileId
      *dirPath 文件夹地址
      *fileName 存储的文件名称
      *onDownloadListener 回调
     *
     */
    fun downFile(customId: String? = null, userFileId: Long, dirPath: String, fileName: String, onDownloadListener: DownloadManager.OnDownloadListener)
    
    
    /**
     * 下载图片
      *customId 为自定义的id  （如果上传的时候有传）
      *userFileId  上传完成后返回回来的fileId
      *dirPath 文件夹地址
      *fileName 存储的文件名称
      *optionImage 需要下载的图片的具体参数，如分辨率，旋转角度
      *onDownloadListener 回调
     */
    fun downImageFile(customId: String? = null, userFileId: Long, dirPath: String, fileName: String, optionImage: CustomImageInfo? = null, onDownloadListener: DownloadManager.OnDownloadListener) 
   
  
  
  
