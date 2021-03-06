package com.hewj.bangumi.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hewj.bangumi.data.AbsReturn;

import java.net.ConnectException;

import rx.Observer;

/**
 * Created by hewj on 2017/5/10.
 */
public abstract class EndObserver<T> implements Observer<T> {
    public abstract void onHideLoding();
    public abstract void onMyNext(T entity);


    static Context mContext;
    public static void init(Context context){
        mContext=context;
    }

    @Override
    public void onCompleted() {
        onHideLoding();
    }

    @Override
    public void onError(Throwable e) {

        e.printStackTrace();
        Throwable throwable = e;
        //获取最根源的异常
        while(throwable.getCause() != null){
            e = throwable;
            throwable = throwable.getCause();
        }

        Log.i("exception",e.toString());
        if (e instanceof ConnectException){             //HTTP错误
            Toast.makeText(mContext,"网络异常！",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext,"请求失败！!",Toast.LENGTH_SHORT).show();
        }
        onHideLoding();
//
//        Throwable e1=new Throwable(new ConnectException());
//        Log.i("error", e1.toString());
//
//        onEnd();
//        if(e instanceof ConnectException)
//            Log.i("error", e.toString());
//       // onEnd();
//        if(e.toString().indexOf("failed to connect to")!=-1)
//            Toast.makeText(mContext,"网络异常,请检查网络设置",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T entity) {
        if(entity instanceof AbsReturn) {
            if (((AbsReturn) entity).getCode() == 101) {
                Toast.makeText(mContext, "登录失效，请重新登录", Toast.LENGTH_SHORT).show();
            }
            else if (((AbsReturn) entity).getCode() == 0) {
                Toast.makeText(mContext, ((AbsReturn) entity).getMessage(), Toast.LENGTH_SHORT).show();
            }
            else onMyNext(entity);
        }
        else onMyNext(entity);
        onHideLoding();
    }
}
