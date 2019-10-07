package com.example.brandonblog.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.R;

import java.util.ArrayList;

public class BlogsRecyclerViewAdapter extends RecyclerView.Adapter<BlogsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Blog> dataSet;
    private Listener listener;
    private Context context;

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public BlogsRecyclerViewAdapter(ArrayList<Blog> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewBody = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getTitle());
        textViewBody.append(dataSet.get(listPosition).getUsername());

        String body = dataSet.get(listPosition).getBody();

        if(body.length() > 20) {
            body = body.substring(0, 20);
        }

        textViewBody.append("\n\n" + body + "...");


        Glide.with(context).load(dataSet.get(listPosition).getImage_url()).into(imageView);

        CardView cardView = holder.cardView;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(listPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void updateData(ArrayList<Blog> blogs){
        this.dataSet = blogs;
        notifyDataSetChanged();
    }
}

