package com.example.brandonblog.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.R;


public class BlogsRecyclerViewAdapter extends PagedListAdapter<Blog, BlogsRecyclerViewAdapter.MyViewHolder> {
    private static Listener listener;
    private static Context context;

    public interface Listener {
        void onClick(Blog blog);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewBody;
        ImageView imageViewIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewBody = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(final Blog blog) {
            textViewName.setText(blog.getTitle());
            textViewBody.append(blog.getUsername());

            String body = blog.getBody();

            if (body.length() > 20) {
                body = body.substring(0, 20);
            }

            textViewBody.append("\n\n" + body + "...");


            Glide.with(context).load(blog.getImage_url()).signature(new ObjectKey(blog)).into(imageViewIcon);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(blog);
                    }
                }
            });
        }
    }

    public BlogsRecyclerViewAdapter(Context context) {
        super(BLOG_COMPARATOR);
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
        holder.bind(getItem(listPosition));
    }


    private static final DiffUtil.ItemCallback<Blog> BLOG_COMPARATOR = new DiffUtil.ItemCallback<Blog>() {
        @Override
        public boolean areItemsTheSame(@NonNull Blog oldItem, @NonNull Blog newItem) {
            return oldItem.getPrimaryKey() == newItem.getPrimaryKey();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Blog oldItem, @NonNull Blog newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    };


}

