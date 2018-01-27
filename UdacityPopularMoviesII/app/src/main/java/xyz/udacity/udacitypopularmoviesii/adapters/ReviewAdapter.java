package xyz.udacity.udacitypopularmoviesii.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.udacity.udacitypopularmoviesii.models.Result;
import xyz.udacity.udacitypopularmoviesii.R;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {


    private List<Result> reviewModelList;
    Context mContext;

    public ReviewAdapter(List<Result> reviewModelList, Context mContext) {
        this.mContext = mContext;
        this.reviewModelList = reviewModelList;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {


        holder.review.setText(reviewModelList.get(position).getContent());
        holder.author_name.setText(reviewModelList.get(position).getAuthor());

    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author_name, review;


        public MyViewHolder(View itemView) {
            super(itemView);

            author_name = (TextView) itemView.findViewById(R.id.author_name);
            review = (TextView) itemView.findViewById(R.id.review);

        }
    }

}


