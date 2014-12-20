package com.example.chibifacerecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MainActivity extends Activity {

	private static String TAG = "MainActivity";
	
	// variables to be used
	private File mCascadeFile;
	private CascadeClassifier faceCascade;
	private PersonRecognizer classifier;
	private static String mPath;
	
	
	
	// the class which you need
	ChibiFaceRecognizer mFaceRecognizer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// you can choose wherever you want
		mPath = Environment.getExternalStorageDirectory().toString() + "/chibifacerecognitionfiles/";
		
		// constructor which is already the same
		mFaceRecognizer = new ChibiFaceRecognizer(mPath, classifier, faceCascade);
	
	}
	
	
	// must be included (just copy-paste)
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	    @Override
	    public void onManagerConnected(int status) {
	        switch (status) {
	            case LoaderCallbackInterface.SUCCESS:
	            {
                    try {
                    	classifier = new PersonRecognizer(mPath); 
                    	// load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
                        faceCascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (faceCascade.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            faceCascade = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
                        cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
	                Log.i(TAG, "OpenCV loaded successfully");
	            } break;
	            default:
	            {
	                super.onManagerConnected(status);
	            } break;
	        }
	    }
	};
	
	@Override
	
	public void onResume(){
		super.onResume();
		
		// must be included in onResume
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
	}
	
	
	
}
