package com.example.myblog;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> bloglist;

    public BlogRecyclerAdapter(Context context, List<Blog> bloglist) {
        this.context = context;
        this.bloglist = bloglist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog=bloglist.get(position);
        String imageURL=null;
        holder.title.setText(blog.getTitle());
        holder.description.setText(blog.getDesc());

        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        holder.timestamp.setText(formattedDate);
        imageURL=blog.getImage();

        //TODO add picasso library to load image
        Picasso.get().load(imageURL).into(holder.image);



    }

    @Override
    public int getItemCount() {
        return bloglist.size();
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{

        public TextView title;
        public TextView description;
        public TextView timestamp;
        public ImageView image;
        String userid;

        public ViewHolder(@NonNull View itemView,Context ctx) {

            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.postTitleList);
            description=itemView.findViewById(R.id.postDescription);
            timestamp=itemView.findViewById(R.id.postTimeStamp);
            image=itemView.findViewById(R.id.postImageList);
            userid=null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //we can go to the next activity
                }
            });
        }
    }
}
