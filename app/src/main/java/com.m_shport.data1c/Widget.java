package com.m_shport.data1c;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Widget extends AppWidgetProvider {

    public static String ACTION_WIDGET_UPDATE = "ClickWidget";
    public static final String BASE_URL = "http://example.com/db_name/odata/standard.odata/";
    static String username = "user";
    static String password = "password";

    public static String txt;

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.tv, getJson());

        Intent update = new Intent(context, Widget.class);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, update, 0);
        remoteViews.setOnClickPendingIntent(R.id.tv, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.renew, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_WIDGET_UPDATE.equals(intent.getAction())) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.tv, getJson());
        }
    }

    public static String getJson() {

        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthInt = calendar.get(Calendar.MONTH);
        monthInt = monthInt + 1;
        String month = "";

        if (monthInt < 10){
            month = "0" + monthInt;
        } else {
            month = String.valueOf(monthInt);
        }

        int firstDayInt = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        String firstDay = "";

        if (firstDayInt < 10) {
            firstDay = "0" + firstDayInt;
        } else {
            firstDay = String.valueOf(firstDayInt);
        }

        String lastDay = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        OkHttpClient httpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder().header("Authorization", Credentials.basic(username, password));
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataInterface dataInterface = retrofit.create(DataInterface.class);

        Call<ListJson> call = dataInterface.getData("AccumulationRegister_itRelease/Turnovers(StartPeriod=datetime'" + year + "-" + month + "-" + firstDay + "',EndPeriod=datetime'" + year + "-" + month + "-" + lastDay + "T23:59:59')?$select=FaktRunMetrTurnover&$format=application/json;odata=nometadata");
        call.enqueue(new Callback<ListJson>() {
            @Override
            public void onResponse(Call<ListJson> call, Response<ListJson> response) {
                if (response.isSuccessful()) {
                    List<Value> listValue = response.body().getValue();
                    txt = String.valueOf(listValue.get(0).getFaktRunMetrTurnover());
                } else {
                    txt = String.valueOf(response.code());
                }
            }

            @Override
            public void onFailure(Call<ListJson> call, Throwable t) {
                txt = "FAIL";
            }
        });
        return txt;
    }

}