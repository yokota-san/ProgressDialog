package com.example.yokotasan.progressdialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.TimingLogger;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity
        implements MyProgressDialogFragment.CommonDialogInterface.onClickListener,
        MyProgressDialogFragment.CommonDialogInterface.onItemClickListener,
        MyProgressDialogFragment.CommonDialogInterface.onShowListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button) findViewById(R.id.dialog_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgress() {
        Bundle args = new Bundle();

        args.putString(MyProgressDialogFragment.FIELD_TITLE, "タイトル");
        args.putString(MyProgressDialogFragment.FIELD_MESSAGE, "メッセージメッセージメッセージメッセージ");
        args.putString(MyProgressDialogFragment.FIELD_LABEL_POSITIVE, "POSITIVE");
        args.putString(MyProgressDialogFragment.FIELD_LABEL_NEGATIVE, "NEGATIVE");

        MyProgressDialogFragment newFragment = MyProgressDialogFragment.newInstance(args);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.add(newFragment, "progress");
//        transaction.commit();

        // ここでCancelable(false)をしないと効果が無い
        newFragment.setCancelable(false);
        newFragment.setStartValue(1000);
        newFragment.show(getFragmentManager(), "progress");

        final Thread thread = new Thread(new Runnable() {
            //TODO: showしてからすぐだとダイアログが取得できない？
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeProgress();
            }
        });
        thread.setName("バックグラウンド");
        thread.start();
    }

    private void changeProgress() {
        //別スレッドからを想定
        int i = 5000;
        MyProgressDialogFragment fragment = (MyProgressDialogFragment)getFragmentManager().findFragmentByTag("progress");
        TimingLogger logger = new TimingLogger("TAG_TEST", "testTimingLogger");
        fragment.changeProgress((i+1));
        logger.addSplit("setProgress");

//        for (i=0; i<50000; i++) {
//            MyProgressDialogFragment fragment = (MyProgressDialogFragment)getFragmentManager().findFragmentByTag("progress");
//            if (fragment != null) {
//                // 測定開始
//                TimingLogger logger = new TimingLogger("TAG_TEST", "testTimingLogger");
//                fragment.changeProgress((i+1));
//                logger.addSplit("setProgress");
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//            }
//        }


    }

    @Override
    public void onDialogButtonClick(String tag, Dialog dialog, int which) {
//        MyProgressDialogFragment fragment = (MyProgressDialogFragment)getFragmentManager().findFragmentByTag("progress");
//        if (fragment != null) {
//            fragment.dismiss();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.remove(fragment);
//            transaction.commit();
//        }


    }

    @Override
    public void onDialogItemClick(String tag, Dialog dialog, String title, int which) {

    }

    @Override
    public void onDialogShow(String tag, Dialog dialog) {

    }
}