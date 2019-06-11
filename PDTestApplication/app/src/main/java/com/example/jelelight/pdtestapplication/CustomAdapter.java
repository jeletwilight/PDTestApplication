package com.example.jelelight.pdtestapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    List<Event> events;
    Context context;

    public CustomAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items

        final Actor actor = new Actor(events.get(position).getActor());
        final Repo repoUser = new Repo(events.get(position).getRepo());

        new DownloadImageTask(holder.avatar).execute(actor.getAvatar_url());

        holder.name.setText(actor.getDisplay_login());
        holder.eventID.setText(events.get(position).getId());
        holder.gitURL.setText(actor.getUrl());
        holder.repo.setText(repoUser.getName());
        holder.createdDate.setText(events.get(position).getCreated());

        String eventType = events.get(position).getType();
        if(eventType.contains("Push")){
            holder.action.setBackgroundResource(R.drawable.ic_cloud_upload_black_24dp);
        }else if(eventType.contains("PullRequestEvent")){
            holder.action.setBackgroundResource(R.drawable.ic_cloud_download_black_24dp);
        }else if(eventType.contains("Watch")){
            holder.action.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
        }else if(eventType.contains("IssuesEvent")){
            holder.action.setBackgroundResource(R.drawable.ic_warning_black_24dp);
        }else if(eventType.contains("Fork")){
            holder.action.setBackgroundResource(R.drawable.ic_device_hub_black_24dp);
        }else if(eventType.contains("CreateEvent")){
            holder.action.setBackgroundResource(R.drawable.ic_add_box_black_24dp);
        }else if(eventType.contains("Release")){
            holder.action.setBackgroundResource(R.drawable.ic_playlist_add_check_black_24dp);
        } else if(eventType.contains("PublicEvent")) {
            holder.action.setBackgroundResource(R.drawable.ic_public_black_24dp);
        }else if(eventType.contains("Delete")) {
            holder.action.setBackgroundResource(R.drawable.ic_delete_black_24dp);
        }else if(eventType.contains("Member")) {
            holder.action.setBackgroundResource(R.drawable.ic_group_black_24dp);
        }else if(eventType.contains("Comment")){
            holder.action.setBackgroundResource(R.drawable.ic_insert_comment_black_24dp);
        }else{
            holder.action.setBackgroundResource(R.drawable.ic_more_horiz_black_24dp);
        }

        holder.repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repoUser.getUrl()));
                context.startActivity(browserIntent);
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("act_img",actor.getAvatar_url());
                intent.putExtra("act_id",actor.getId());
                intent.putExtra("act_login",actor.getLogin());
                intent.putExtra("act_display",actor.getDisplay_login());
                intent.putExtra("act_gravatar",actor.getGravatar_id());
                intent.putExtra("act_url",actor.getUrl());
                context.startActivity(intent);
            }
        });

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                //Toast.makeText(context, events.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, eventID, gitURL, repo, createdDate;// init the item view's
        ImageView avatar, action;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.user_name_tv);
            eventID = (TextView) itemView.findViewById(R.id.event_id_tv);
            gitURL = (TextView) itemView.findViewById(R.id.git_tv);
            repo = (TextView) itemView.findViewById(R.id.repo_tv);
            createdDate = (TextView) itemView.findViewById(R.id.date_tv);

            avatar = (ImageView) itemView.findViewById(R.id.user_avatar_iv);
            action = (ImageView) itemView.findViewById(R.id.event_iv);
        }
    }



    /*public void onClick(View v) {
        startActivity(new Intent(this, IndexActivity.class));
        finish();

    }*/

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}