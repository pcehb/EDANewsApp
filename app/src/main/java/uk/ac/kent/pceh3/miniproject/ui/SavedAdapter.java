package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.abdularis.piv.VerticalScrollParallaxImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB;

/**
 * Created by pceh3 on 11/04/2019.
 */

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {
    private MyCursorAdapter mCursorAdapter;
    Context mContext;
    public View.OnClickListener itemCLickListener;
    public View.OnLongClickListener itemLongCLickListener;
    public List<Articles> savedArticleList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView desc;
        private TextView date;
        private TextView url;


        private VerticalScrollParallaxImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
            desc = (TextView) itemView.findViewById(R.id.article_desc);
            date = (TextView) itemView.findViewById(R.id.date);
            photo = (VerticalScrollParallaxImageView) itemView.findViewById(R.id.photo);
            url = (TextView) itemView.findViewById(R.id.url);
            itemView.setOnClickListener(itemCLickListener);
            itemView.setOnLongClickListener(itemLongCLickListener);
        }

        public void bindCursor(Cursor cursor) {
            title.setText(cursor.getString(cursor.getColumnIndexOrThrow(SavedArticlesDB.SAVED_COLUMN_TITLE)));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(SavedArticlesDB.SAVED_COLUMN_DESC));
            description = StringUtils.abbreviate(description, 200);
            desc.setText(description);
            date.setText(cursor.getString(cursor.getColumnIndexOrThrow(SavedArticlesDB.SAVED_COLUMN_DATE)));
            url.setText(cursor.getString(cursor.getColumnIndexOrThrow(SavedArticlesDB.SAVED_COLUMN_URL)));

            byte[] imgByte = cursor.getBlob
                    (cursor.getColumnIndexOrThrow(SavedArticlesDB.SAVED_COLUMN_PHOTO));
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
            photo.setImageBitmap(bmp);

            itemView.setTag(cursor.getPosition());
        }

    }

    public SavedAdapter(Context context, Cursor cursor, View.OnClickListener clickListener, View.OnLongClickListener longClickListener) {
        mContext = context;
        mCursorAdapter = new MyCursorAdapter(mContext, cursor, 0);
        this.itemCLickListener = clickListener;
        this.itemLongCLickListener = longClickListener;
    }

    public static class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(
                    R.layout.adapter_feed_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        }

        public void bindViewHolder(ViewHolder viewHolder, Context context, Cursor cursor) {
            viewHolder.bindCursor(cursor);
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindViewHolder(holder, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}
