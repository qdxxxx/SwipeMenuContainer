package qdx.swipemenucontainer.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import qdx.swipemenucontainer.R;
import qdx.swipemenucontainer.SwipeMenuLayout;


public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    private List<DataBean> list;

    public RecAdapter() {
        list = Datas.getDatas();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_view, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DataBean dataBean = list.get(position);
        Glide.with(holder.iv_avatar.getContext()).load(dataBean.getImgUrl()).into(holder.iv_avatar);
        holder.tv_title.setText(dataBean.getName());
        holder.tv_subTitle.setText(dataBean.getContent());
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(holder.itemView.getContext(), dataBean.getContent(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.view_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                holder.swipeMenuLayout.quickClose();
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBean data = list.get(position);
                list.remove(position);
                list.add(0, data);
                notifyDataSetChanged();


//                holder.swipeMenuLayout.quickClose();
            }
        });
        holder.view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                holder.swipeMenuLayout.smoothClose();
                Toast.makeText(holder.itemView.getContext(), "编辑", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_subTitle;
        TextView view_del;
        TextView view_edit;
        TextView tv_top;
        ImageView iv_avatar;
        SwipeMenuLayout swipeMenuLayout;
        View contentView;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.item_tv_title);
            tv_subTitle = (TextView) itemView.findViewById(R.id.item_tv_subTitle);
            tv_top = (TextView) itemView.findViewById(R.id.item_tv_top);
            contentView = itemView.findViewById(R.id.item_content);

            view_del = (TextView) itemView.findViewById(R.id.item_tv_del);
            view_edit = (TextView) itemView.findViewById(R.id.item_tv_edit);
            swipeMenuLayout = (SwipeMenuLayout) itemView.findViewById(R.id.item_layout_swip);
            iv_avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
        }
    }
}
