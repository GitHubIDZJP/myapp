package com.example.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.example.myapp.R;
import com.example.myapp.entity.NewsEnity;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.listener.OnItemChildClickListener;
import com.example.myapp.listener.OnItemClickListener;
import com.example.myapp.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsEnity> datas;

    public void setDatas(List<NewsEnity> datas) {
        this.datas = datas;
    }

    public NewsAdapter(Context context) {
        this.mContext = context;

    }

    public NewsAdapter(Context context, List<NewsEnity> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        int type = datas.get(position).getType();
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_one, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_two, parent, false);
            return new ViewHolderTwo(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_three, parent, false);
            return new ViewHolderThree(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        NewsEnity newsEnity = datas.get(position);

        if (type == 1) {
            ViewHolderOne vh = (ViewHolderOne) holder;
            vh.title.setText(newsEnity.getNewsTitle());
            vh.author.setText(newsEnity.getAuthorName());
            vh.comment.setText(newsEnity.getCommentCount() + "评论 .");
            vh.time.setText(newsEnity.getReleaseDate());
            Picasso.with(mContext)
                    .load(newsEnity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEnity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);

        } else if (type == 2) {
            ViewHolderTwo vh = (ViewHolderTwo) holder;
            vh.title.setText(newsEnity.getNewsTitle());
            vh.author.setText(newsEnity.getAuthorName());
            vh.comment.setText(newsEnity.getCommentCount() + "评论 .");
            vh.time.setText(newsEnity.getReleaseDate());
            Picasso.with(mContext)
                    .load(newsEnity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEnity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.pic1);
            Picasso.with(mContext)
                    .load(newsEnity.getThumbEntities().get(1).getThumbUrl())
                    .into(vh.pic2);
            Picasso.with(mContext)
                    .load(newsEnity.getThumbEntities().get(2).getThumbUrl())
                    .into(vh.pic3);
        }
        else {
            ViewHolderThree vh = (ViewHolderThree) holder;
            vh.title.setText(newsEnity.getNewsTitle());
            vh.author.setText(newsEnity.getAuthorName());
            vh.comment.setText(newsEnity.getCommentCount() + "评论 .");
            vh.time.setText(newsEnity.getReleaseDate());
            Picasso.with(mContext)
                    .load(newsEnity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEnity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        }



    }

    @Override
    //列表有多少项
    public int getItemCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        } else {
            return 0;
        }

    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;

        public ViewHolderOne(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);

        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView pic1, pic2, pic3;
//        private NewsEnity newsEntity;

        public ViewHolderTwo(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);
        }


    }

    public class ViewHolderThree extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEnity newsEntity;

        public ViewHolderThree(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
        }
    }
}
