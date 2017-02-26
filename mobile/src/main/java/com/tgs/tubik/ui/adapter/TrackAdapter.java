package com.tgs.tubik.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tgs.tubik.R;
import com.tgs.tubik.api.lastfm.model.Image;
import com.tgs.tubik.api.lastfm.model.track.Track;
import com.tgs.tubik.ui.fragment.PopularFragment.OnPopularFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private final List<Track> mValues;
    private final OnPopularFragmentInteractionListener mListener;
    private AQuery aq;

    public TrackAdapter(Context context, OnPopularFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
        aq = new AQuery(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_top_lastfm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTrackClick(holder.mItem);
                }
            }
        });

        if (holder.mItem.getImageList().size() > 0) {
            String imageLink = Image.ImageSize.getLinkBy(Image.ImageSize.LARGE, holder.mItem.getImageList());
            if (imageLink != null && imageLink.length() > 0)
                aq.id(holder.mLogoView).image(imageLink);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAll(List<Track> items) {
        for (Track item : items) {
            mValues.add(item);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitle;
        final ImageView mLogoView;
        Track mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mLogoView = (ImageView) view.findViewById(R.id.image);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
