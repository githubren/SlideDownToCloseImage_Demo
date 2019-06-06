package com.example.yfsl.slidedowntocloseimage_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Bitmap> bitmaps;
    private List<Integer> imageIds;

    public RecyclerViewAdapter(Context mContext, List<Bitmap> bitmaps,List<Integer> imageIds) {
        this.mContext = mContext;
        this.bitmaps = bitmaps;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //创建布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_show_thum_picture,viewGroup,false);
        return new ViewHoldre(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (bitmaps != null && !bitmaps.isEmpty()){
            //获取数据
            Bitmap bitmap = bitmaps.get(i);
            ((ViewHoldre)viewHolder).picture.setImageBitmap(bitmap);
            ((ViewHoldre)viewHolder).picture.setScaleType(ImageView.ScaleType.FIT_XY);
            ((ViewHoldre)viewHolder).picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item中包含控件的点击监听  将点击的view 和viewId用自定义接口传递出去  也可以直接在此处对点击后做处理
                    onItemClickListener.onItemClick(v,i);
                }
            });
        }
//        if (imageIds != null && !imageIds.isEmpty()){
//            int imageId = imageIds.get(i);
//            ((ViewHoldre)viewHolder).picture.setImageResource(imageId);
//        }

    }

    @Override
    public int getItemCount() {
        return bitmaps == null ? 0 : bitmaps.size();
    }

    class ViewHoldre extends RecyclerView.ViewHolder{
        ImageView picture;

        public ViewHoldre(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.item_picture);
        }
    }

    /**
     * recyclerview中没有item点击监听 所以自定义一个接口实现item的点击事件监听
     */
    public interface OnItemClickListener{

        void onItemClick(View view , int id);
    }

    private OnItemClickListener onItemClickListener;

    /**
     * 此方法将item的点击监听暴露出去 使得在外部可以调用接口从而实现对item的点击事件监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
