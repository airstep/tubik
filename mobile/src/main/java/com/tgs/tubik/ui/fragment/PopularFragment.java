package com.tgs.tubik.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tgs.tubik.R;
import com.tgs.tubik.api.lastfm.model.album.Album;
import com.tgs.tubik.api.lastfm.model.album.Albums;
import com.tgs.tubik.api.lastfm.model.track.Track;
import com.tgs.tubik.api.lastfm.model.track.Tracks;
import com.tgs.tubik.tools.Tools;
import com.tgs.tubik.ui.adapter.AlbumAdapter;
import com.tgs.tubik.ui.adapter.TrackAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPopularFragmentInteractionListener}
 * interface.
 */
public class PopularFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnPopularFragmentInteractionListener mListener;
    private TrackAdapter mTracksAdapter;
    private AlbumAdapter mAlbumsAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PopularFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PopularFragment newInstance(int columnCount) {
        PopularFragment fragment = new PopularFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_grid, container, false);
        Context context = view.getContext();

        RecyclerView gridTracksView = (RecyclerView) view.findViewById(R.id.gridLastFMTopTracks);

        mTracksAdapter = new TrackAdapter(getContext(), mListener);
        gridTracksView.setLayoutManager(new GridLayoutManager(context, mColumnCount, LinearLayoutManager.HORIZONTAL, false));
        gridTracksView.setAdapter(mTracksAdapter);

        RecyclerView gridAlbumsView = (RecyclerView) view.findViewById(R.id.gridTopAlbums);

        mAlbumsAdapter = new AlbumAdapter(getContext(), mListener);
        gridAlbumsView.setLayoutManager(new GridLayoutManager(context, mColumnCount, LinearLayoutManager.HORIZONTAL, false));
        gridAlbumsView.setAdapter(mAlbumsAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPopularFragmentInteractionListener) {
            mListener = (OnPopularFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPopularFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        runGetLastFMTopTracks();
        runGetLastFMTopAlbums();
    }

    private void runGetLastFMTopAlbums() {
        mApp.getAPILastFM().getTopAlbumsByTag("rap", 100, 1)
            .subscribe(
                response -> mAlbumsAdapter.addAll(response.get(Albums.ROOT).getList()),
                this::fail
            );
    }

    private void runGetLastFMTopTracks() {
        String countryCode = Tools.getCountry(getContext());
        String countryFullName = Tools.getEnglishCountryName(countryCode);

        mApp.getAPILastFM().getTopTracksGeo(countryFullName)
            .subscribe(
                response -> mTracksAdapter.addAll(response.get(Tracks.ROOT).getList()),
                this::fail
            );
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPopularFragmentInteractionListener {
        void onTrackClick(Track item);
        void onAlbumClick(Album item);
    }
}
