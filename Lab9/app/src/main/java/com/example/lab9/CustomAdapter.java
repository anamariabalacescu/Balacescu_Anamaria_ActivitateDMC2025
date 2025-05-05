package com.example.lab9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAdapter extends BaseAdapter {
    private final List<BankItem> items;
    private final Context context;
    private final ExecutorService executor;
    private final Handler handler;

    public CustomAdapter(Context context, List<BankItem> items) {
        this.context = context;
        this.items = items;
        this.executor = Executors.newFixedThreadPool(4);
        this.handler  = new Handler(Looper.getMainLooper());
    }

    @Override public int getCount()              { return items.size(); }
    @Override public BankItem getItem(int pos)   { return items.get(pos); }
    @Override public long getItemId(int pos)     { return pos; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            int paddingPx = dpToPx(8);
            row.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            ImageView iv = new ImageView(context);
            int sizePx = dpToPx(150);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(sizePx, sizePx);
            iv.setLayoutParams(ivParams);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            row.addView(iv);

            TextView tv = new TextView(context);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f
            );
            tvParams.setMarginStart(dpToPx(12));
            tv.setLayoutParams(tvParams);
            tv.setTextSize(18);
            tv.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.addView(tv);

            holder = new ViewHolder(iv, tv);
            row.setTag(holder);
            convertView = row;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BankItem item = getItem(position);
        holder.tv.setText(item.getTitle());
        holder.iv.setImageBitmap(null);

        executor.execute(() -> {
            try {
                InputStream in = new URL(item.getImageUrl()).openStream();
                final Bitmap bmp = BitmapFactory.decodeStream(in);
                handler.post(() -> holder.iv.setImageBitmap(bmp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }

    private static class ViewHolder {
        final ImageView iv;
        final TextView tv;
        ViewHolder(ImageView iv, TextView tv) {
            this.iv = iv;
            this.tv = tv;
        }
    }
}
