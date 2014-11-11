package com.example.andriod;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ProgressBar bar=(ProgressBar) findViewById(R.id.progressBar1);
        
        
        
        new AsyncTask<String, String, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				  String zipFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/compressed.zip";
			        String destDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/testZip";
			        UnzipUtility unzipper = new UnzipUtility();
			        try {
			            unzipper.unzip(zipFilePath, destDirectory,new UnzipUtility.UnzipListener() {

							@Override
							public void onExtractStart() {
								// TODO Auto-generated method stub
								
							}
							@Override
							public void onExtractProgress(final int progress, String filePath) {
								
										publishProgress(new String[]{progress+"",filePath});
									
								
							}

							@Override
							public void onExtractCompleted() {
								// TODO Auto-generated method stub
								
							}
						});
			        } catch (Exception ex) {
			            // some errors occurred
			            ex.printStackTrace();
			        }
				return null;
			}
			
			protected void onProgressUpdate(String...values) {
				bar.setProgress(Integer.parseInt(values[0]));
			};
		}.execute();
      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
