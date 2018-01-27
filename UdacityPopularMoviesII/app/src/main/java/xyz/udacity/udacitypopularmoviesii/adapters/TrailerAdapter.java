package xyz.udacity.udacitypopularmoviesii.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.udacity.udacitypopularmoviesii.models.VideoResult;
import xyz.udacity.udacitypopularmoviesii.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {


    private List<VideoResult> trailerList;
    Context mContext;

    public TrailerAdapter(List<VideoResult> trailerList, Context mContext) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {


        holder.trailer_number.setText(mContext.getString(R.string.trailer) + position);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trailer_number;


        public MyViewHolder(View itemView) {
            super(itemView);

            trailer_number = (TextView) itemView.findViewById(R.id.trailer_number);


        }
    }

}

