package love.kotori.lovelive.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import love.kotori.lovelive.R;
import love.kotori.lovelive.model.Image;
import love.kotori.lovelive.widget.RatioImageView;

import java.util.List;


public abstract class ImageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Image> images;
    private Context context;

    public ImageRecyclerViewAdapter(Context context, List<Image> images) {
        this.images = images;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, null);
        ImageViewHolder ivh = new ImageViewHolder(layoutView);
        return ivh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        Image image = images.get(position);
        imageViewHolder.imageView.setOriginalSize(100, 100);
        Glide.with(context)
                .load(image.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewHolder.imageView);
        ViewCompat.setTransitionName(imageViewHolder.imageView, image.getUrl());


    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    protected abstract void onItemClick(View v, int position);

    public Image getImage(int position) {
        return images.get(position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private RatioImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (RatioImageView) itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}

