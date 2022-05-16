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

        // de views som fylls via adaptern
        private TextView penguinName;
        private TextView penguinEats;
        private TextView penguinSize;
        private ImageView penguinImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            penguinName = itemView.findViewById(R.id.penguin_name);
            penguinEats = itemView.findViewById(R.id.eat_information);
            penguinSize = itemView.findViewById(R.id.height_information);
            penguinImage = itemView.findViewById(R.id.penguin_image);
        }

        // Skickar vidare informationen om vilket den i recyclerViewn som klickats på
        @Override
        public void onClick(View view) {
            onClickListener.onClick(penguins.get(getAdapterPosition()));
        }
    }

    // hämtar layouten för raderna
    @NonNull
    @Override
    public PenguinRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pinguin_item_template, parent, false));
    }

    // fyller layouten med information
    @Override
    public void onBindViewHolder(@NonNull PenguinRecyclerAdapter.ViewHolder holder, int position) {
        holder.penguinName.setText(penguins.get(position).getName());
        holder.penguinEats.setText(penguins.get(position).getEats());
        holder.penguinSize.setText(penguins.get(position).getSize());
        Picasso.get().load(penguins.get(position).getAuxdata().getImg()).resize(200, 200).into(holder.penguinImage);
    }

    // har koll på hur många objekt det finns i recyclern
    @Override
    public int getItemCount() {
        return penguins.size();
    }


    public interface OnClickListener {
        void onClick(Penguin penguin);
    }

    // ändrar innehållet i listan som recyclern arbetar med.
    public void setPenguins(ArrayList<Penguin> penguins){           // anropas när GSON unmarhel JSON strängen (från main activity)
        this.penguins = penguins;
    }
}
