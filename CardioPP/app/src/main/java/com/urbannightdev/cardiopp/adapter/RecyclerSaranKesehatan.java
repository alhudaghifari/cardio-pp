package com.urbannightdev.cardiopp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.urbannightdev.cardiopp.R;
import com.urbannightdev.cardiopp.model.SaranKesehatanModel;

import java.util.List;

/**
 * Created by ghifar on 24/02/18.
 */

public class RecyclerSaranKesehatan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerSaranKesehatan.class.getSimpleName();

    private List<SaranKesehatanModel> mPackageDataList;

    private int lengthOfData = 0;

    private Context mContext;

    public RecyclerSaranKesehatan(Context context, List<SaranKesehatanModel> list) {
        mContext = context;
        mPackageDataList = list;
        lengthOfData = mPackageDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_healthy_suggestion, parent, false);
        return new ViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderList viewHolderList = (ViewHolderList) holder;

        String level = mPackageDataList.get(position).getLevel();
        String pesanAwalKesehatan = mPackageDataList.get(position).getPesanAwalKesehatan();
        String namaPenyakit = mPackageDataList.get(position).getNamaPenyakit();
        String perawatanDiri = "";
        perawatanDiri = mPackageDataList.get(position).getPerawatanDiri();
        String spesialis = mPackageDataList.get(position).getSpesialis();

        viewHolderList.mTextViewpesanAwalKesehatan.setText(pesanAwalKesehatan);
        viewHolderList.mTextViewNamaPenyakit.setText(namaPenyakit);
        viewHolderList.mTextViewPerawatanDiri.setText(perawatanDiri);
        viewHolderList.mTextViewSpesialis.setText(spesialis);
    }

    @Override
    public int getItemCount() {
        return lengthOfData;
    }


    protected class ViewHolderList extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public ImageView mImageHeart;

        public TextView mTextViewpesanAwalKesehatan;
        public TextView mTextViewNamaPenyakit;
        public TextView mTextViewPerawatanDiri;
        public TextView mTextViewSpesialis;

        public CardView mCardViewContainer;

        public ViewHolderList(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            mCardViewContainer = (CardView) itemView.findViewById(R.id.cardview_container);

            mImageHeart = (ImageView) itemView.findViewById(R.id.iv_heart);

            mTextViewpesanAwalKesehatan =  (TextView) itemView.findViewById(R.id.tv_pesanAwalKesehatan);
            mTextViewNamaPenyakit =  (TextView) itemView.findViewById(R.id.tv_namaPenyakit);
            mTextViewPerawatanDiri = (TextView) itemView.findViewById(R.id.tv_perawatanDiri);
            mTextViewSpesialis = (TextView) itemView.findViewById(R.id.tv_spesialis);
        }
    }


    public void addNewData(SaranKesehatanModel list) {
        mPackageDataList.add(list);
        lengthOfData = mPackageDataList.size();
    }

    public void refreshRecyclerView() {
        RecyclerSaranKesehatan.this.notifyDataSetChanged();
    }
}
