package uk.ac.kent.pceh3.miniproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.model.Feed;
import uk.ac.kent.pceh3.miniproject.R;

/**
 * Created by pceh3 on 23/01/2019.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView fullName;
        private TextView phoneNumber;
        private ImageView photo;


        public ViewHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.full_name);
            phoneNumber = (TextView) itemView.findViewById(R.id.phone_number);
            photo = (ImageView) itemView.findViewById(R.id.photo);

            itemView.setOnClickListener(itemCLickListener);
        }

        public void setData(Articles articles, int position) {

            fullName.setText(articles.getTitle());
            phoneNumber.setText(articles.getDescription());
            Picasso.get()
                    .load(articles.getImageUrl())
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
