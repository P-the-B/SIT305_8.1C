package com.example.llmchatbot.ui.chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llmchatbot.databinding.ItemMessageAiBinding;
import com.example.llmchatbot.databinding.ItemMessageUserBinding;
import com.example.llmchatbot.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_AI = 1;

    public interface OnRetryClickListener {
        void onRetry(Message message);
    }

    private final OnRetryClickListener retryListener;

    public MessageAdapter(OnRetryClickListener retryListener) {
        super(DIFF_CALLBACK);
        this.retryListener = retryListener;
    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(@NonNull Message a, @NonNull Message b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Message a, @NonNull Message b) {
                    return a.content.equals(b.content)
                            && a.timestamp == b.timestamp
                            && a.status == b.status;
                }
            };

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isFromUser ? VIEW_TYPE_USER : VIEW_TYPE_AI;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            return new UserViewHolder(ItemMessageUserBinding.inflate(inflater, parent, false));
        } else {
            return new AiViewHolder(ItemMessageAiBinding.inflate(inflater, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = getItem(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
        } else {
            ((AiViewHolder) holder).bind(message);
        }
    }

    private static String formatTime(long timestamp) {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date(timestamp));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageUserBinding binding;

        UserViewHolder(ItemMessageUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.tvMessageContent.setText(message.content);
            binding.tvTimestamp.setText(formatTime(message.timestamp));

            // show retry button only on failed messages
            boolean failed = message.status == Message.STATUS_FAILED;
            binding.btnRetry.setVisibility(failed ? View.VISIBLE : View.GONE);

            binding.btnRetry.setOnClickListener(v -> {
                if (retryListener != null) retryListener.onRetry(message);
            });

            // long press copies message text to clipboard
            binding.bubbleContainer.setOnLongClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager)
                        v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("message", message.content);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

    static class AiViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageAiBinding binding;

        AiViewHolder(ItemMessageAiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.tvMessageContent.setText(message.content);
            binding.tvTimestamp.setText(formatTime(message.timestamp));

            // long press copies AI response to clipboard
            binding.getRoot().setOnLongClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager)
                        v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("message", message.content);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }
}