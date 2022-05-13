package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PenguinRecyclerAdapter extends RecyclerView.Adapter<PenguinRecyclerAdapter.ViewHolder> {

    private ArrayList<Penguin> penguins;
    private LayoutInflater layoutInflater;
    private OnClickListener onClickListener;

    public PenguinRecyclerAdapter(Context context, ArrayList<Penguin> penguins, OnClickListener onClickListener) {
        this.penguins = penguins;
        this.layoutInflater = layoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView penguinName;
        private TextView penguinEats;
        private TextView penguinSize;
        private TextView penguinDetaildInfo;     // kanske bör tas bort
        private ImageView penguinImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            penguinName = itemView.findViewById(R.id.penguin_name);
            penguinEats = itemView.findViewById(R.id.eat_information);
            penguinSize = itemView.findViewById(R.id.height_information);
            penguinImage = itemView.findViewById(R.id.penguin_image);
            penguinDetaildInfo = itemView.findViewById(R.id.information_window);// kanske bör tas bort

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(penguins.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public PenguinRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pinguin_item_template, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PenguinRecyclerAdapter.ViewHolder holder, int position) {
        holder.penguinName.setText(penguins.get(position).getName());
        holder.penguinEats.setText(penguins.get(position).getEats());
        holder.penguinSize.setText(penguins.get(position).getSize());
        Picasso.get().load(penguins.get(position).getAuxdata().getImg()).resize(200, 200).into(holder.penguinImage);
    }

    @Override
    public int getItemCount() {
        return penguins.size();
    }

    public interface OnClickListener {
        void onClick(Penguin penguin);
    }

    public void setPenguins(ArrayList<Penguin> penguins){           // anropas när GSON unmarhel JSON strängen (från main activity)
        this.penguins = penguins;
    }
}
