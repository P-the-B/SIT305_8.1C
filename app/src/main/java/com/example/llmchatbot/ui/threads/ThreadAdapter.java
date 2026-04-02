package com.example.llmchatbot.ui.threads;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llmchatbot.databinding.ItemThreadBinding;
import com.example.llmchatbot.model.ChatThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThreadAdapter extends ListAdapter<ChatThread, ThreadAdapter.ThreadViewHolder> {

    public interface OnThreadClickListener {
        void onClick(ChatThread thread);
    }

    private final OnThreadClickListener clickListener;
    private final OnThreadClickListener longClickListener;

    public ThreadAdapter(OnThreadClickListener clickListener, OnThreadClickListener longClickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    // DiffUtil avoids full redraws — only changed rows update
    private static final DiffUtil.ItemCallback<ChatThread> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ChatThread>() {
                @Override
                public boolean areItemsTheSame(@NonNull ChatThread a, @NonNull ChatThread b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull ChatThread a, @NonNull ChatThread b) {
                    return a.title.equals(b.title) && a.updatedAt == b.updatedAt;
                }
            };

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemThreadBinding binding = ItemThreadBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ThreadViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ThreadViewHolder extends RecyclerView.ViewHolder {

        private final ItemThreadBinding binding;
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault());

        ThreadViewHolder(ItemThreadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatThread thread) {
            binding.tvThreadTitle.setText(thread.title);
            binding.tvThreadDate.setText(sdf.format(new Date(thread.updatedAt)));

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(thread));
            // long press triggers the delete confirmation dialog in the activity
            binding.getRoot().setOnLongClickListener(v -> {
                longClickListener.onClick(thread);
                return true;
            });
        }
    }
}