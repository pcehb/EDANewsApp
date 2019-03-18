package uk.ac.kent.pceh3.miniproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.abdularis.piv.VerticalScrollParallaxImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.R;

/**
 * Created by pceh3 on 23/01/2019.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView desc;
        private TextView date;
        private String dateFormatted;
        private VerticalScrollParallaxImageView photo;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
            desc = (TextView) itemView.findViewById(R.id.article_desc);
            date = (TextView) itemView.findViewById(R.id.date);
            photo = (VerticalScrollParallaxImageView) itemView.findViewById(R.id.photo);

            itemView.setOnClickListener(itemCLickListener);

        }

        public void setData(Articles articles, int position) {

            title.setText(articles.getTitle());
            desc.setText(articles.getDescription());
            dateFormatted = articles.getDateTime();
            String[] dateFormattedPart = dateFormatted.split("T"); // yyyy/mm/dd
            String[] dateSplit = dateFormattedPart[0].split("-");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int month = Integer.parseInt(dateSplit[1]);
            month = month -1;
            dateFormatted = dateSplit[2].toString() + " " + months[month] + " " + dateSplit[0].toString();
            date.setText(dateFormatted);

            Picasso.get()
                    .load(articles.getImageUrl())
                    .placeholder(R.drawable.newspaper)
                    .into(photo);
            itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    private List<Articles> articles = new ArrayList<>();

    private View.OnClickListener itemCLickListener;

    public FeedAdapter(View.OnClickListener clickListener){
        super();

        this.itemCLickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_feed_item, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Articles article = articles.get(position);
        holder.setData(article, position);
    }

    public void updateData(List<Articles> newFeeds){
        this.articles = newFeeds;
        this.notifyDataSetChanged();
    }


}
