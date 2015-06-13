package com.example.yokotasan.progressdialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yokotasan on 2015/06/13.
 */
public class MyProgressDialogFragment extends DialogFragment {

    public interface CommonDialogInterface {
        public interface onClickListener {
            void onDialogButtonClick(String tag, Dialog dialog, int which);
        }

        public interface onShowListener {
            void onDialogShow(String tag, Dialog dialog);
        }

        public interface onItemClickListener {
            void onDialogItemClick(String tag, Dialog dialog, String title, int which);
        }
    }

    private int startValue = 0;

    private CommonDialogInterface.onShowListener mListenerShow;
    private CommonDialogInterface.onClickListener mListenerOnClick;
    private CommonDialogInterface.onItemClickListener mListenerItemClick;

    public static final String FIELD_LAYOUT = "layout";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_LIST_ITEMS = "list_items";
    public static final String FIELD_LIST_ITEMS_STRING = "list_items_string";
    public static final String FIELD_LABEL_POSITIVE = "label_positive";
    public static final String FIELD_LABEL_NEGATIVE = "label_negative";
    public static final String FIELD_LABEL_NEUTRAL = "label_neutral";

    private ProgressDialog mAlertDialog;

    static MyProgressDialogFragment newInstance(Bundle args) {
        MyProgressDialogFragment f = new MyProgressDialogFragment();
        // Supply num input as an argument.
        f.setArguments(args);
        return f;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        ProgressDialog builder = new ProgressDialog(getActivity(), R.style.StyledDialog);
        builder.setMax(50000);
        builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // listener check
        if (getTargetFragment() != null) {
            setListener(getTargetFragment());
        } else if (getActivity() != null) {
            setListener(getActivity());
        }

        // dialog title
        if (args.containsKey(FIELD_TITLE)) {
            builder.setTitle(args.getString(FIELD_TITLE));
        }

        // dialog message
        if (args.containsKey(FIELD_MESSAGE)) {
            builder.setMessage(args.getString(FIELD_MESSAGE));
        }

        // dialog customize content view
        if (args.containsKey(FIELD_LAYOUT)) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(args.getInt(FIELD_LAYOUT), null);
            builder.setView(content);
        }

        // dialog string list
        final List<String> items = new ArrayList<String>();
        if (args.containsKey(FIELD_LIST_ITEMS)) {
            final int[] listItems = args.getIntArray(FIELD_LIST_ITEMS);
            for (int i = 0; i < listItems.length; i++) {
                items.add(getString(listItems[i]));
            }
        }
        if (args.containsKey(FIELD_LIST_ITEMS_STRING)) {
            final String[] listItems = args.getStringArray(FIELD_LIST_ITEMS_STRING);
            for (int i = 0; i < listItems.length; i++) {
                items.add(listItems[i]);
            }
        }

        // positive button title and click listener
        if (args.containsKey(FIELD_LABEL_POSITIVE)) {
            builder.setButton(DialogInterface.BUTTON_POSITIVE, args.getString(FIELD_LABEL_POSITIVE), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mListenerOnClick != null) {
                        mListenerOnClick.onDialogButtonClick(getTag(), mAlertDialog, which);
                    }

                }
            });
        }

        // negative button title and click listener
        if (args.containsKey(FIELD_LABEL_NEGATIVE)) {
            builder.setButton(DialogInterface.BUTTON_NEGATIVE, args.getString(FIELD_LABEL_NEGATIVE), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (mListenerOnClick != null) {
                        mListenerOnClick.onDialogButtonClick(getTag(), mAlertDialog, which);
                    }
                }
            });
        }

        // neutral button title and click listener
        if (args.containsKey(FIELD_LABEL_NEUTRAL)) {
            builder.setButton(DialogInterface.BUTTON_NEUTRAL, args.getString(FIELD_LABEL_NEUTRAL), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mListenerOnClick != null) {
                        mListenerOnClick.onDialogButtonClick(getTag(), mAlertDialog, which);
                    }
                }
            });
        }

        // make dialog
        mAlertDialog = builder;

        // show listener
        if (mListenerShow != null) {
            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {
                    mListenerShow.onDialogShow(getTag(), mAlertDialog);
                }
            });
        }

//        adjust(builder);
        return mAlertDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        adjust(getDialog());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO: setProgresはDialog 表示後にのみ作用するので、初期値はこのタイミング
        ((ProgressDialog)getDialog()).setProgress(startValue);
        adjust(getDialog());
    }

    private void setListener(Object target) {

        // on click listener
        if (target instanceof CommonDialogInterface.onClickListener) {
            mListenerOnClick = (CommonDialogInterface.onClickListener) target;
        }

        // on item click listener
        if (target instanceof CommonDialogInterface.onItemClickListener) {
            mListenerItemClick = (CommonDialogInterface.onItemClickListener) target;
        }

        // on show listener
        if (target instanceof CommonDialogInterface.onShowListener) {
            mListenerShow = (CommonDialogInterface.onShowListener) target;
        }

    }

    private void adjust(Dialog dialog) {
        //ダイアログウィンドウを指定
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1560);
        //ダイアログのparentPanelを指定
        ViewGroup parentPanel = (ViewGroup)((ViewGroup) dialog.getWindow().findViewById(android.R.id.content)).getChildAt(0);
        FrameLayout.LayoutParams frameCenterMatch = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER
        );
        parentPanel.setLayoutParams(frameCenterMatch);
        //ダイアログのView群を指定
        ViewGroup mainLinear = (ViewGroup)((ViewGroup) dialog.findViewById(android.R.id.content)).getChildAt(0);
        mainLinear.setPadding(20,0,20,20);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        mainLinear.getChildAt(1).setLayoutParams(contentParams);
        LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0
        );
        mainLinear.getChildAt(0).setLayoutParams(otherParams);
        mainLinear.getChildAt(2).setLayoutParams(otherParams);
        mainLinear.getChildAt(3).setLayoutParams(otherParams);
        //content 内のTextView を指定
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        );
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        ((ViewGroup) messageView.getParent()).setLayoutParams(scrollParams);
        messageView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams frameCenterWrap = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        );
        messageView.setLayoutParams(frameCenterWrap);
        //タイトルを指定
        TextView titleView = (TextView) dialog.findViewById(getActivity().getResources().getIdentifier("alertTitle", "id", "android"));
        titleView.setGravity(Gravity.CENTER);
        //TitleDivider
        int titleDividerId = getActivity().getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
//        titleDivider.setBackgroundColor(getActivity().getResources().getColor(R.color.test_color));
    }

    public void changeProgress(final int progress) {
        //TODO: UI スレッドからsetProgressを呼ぶと、おかしなことが起きる
        //TODO: スレッド立てなくても、よきに計らってくれるし、そっちのほうが処理早い


        System.out.println("書き込んでるよ");
        if(getDialog() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
                    if(Looper.getMainLooper().getThread() == Thread.currentThread()) {
                        // Current Thread is Main Thread.
                        System.out.println("UIスレッドからだよ");
                    }
                    ((ProgressDialog) getDialog()).setProgress(progress);
//                }
//            });
        }

    }
}
