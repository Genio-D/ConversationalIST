package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String chatId;

    private ChatMessage getMessage(int position) {
        return Data.getMessageCache().getMessage(chatId + "/" + position);
    }

    public ChatAdapter(String chatId) {
        this.chatId = chatId;
    }

    public static class TextMessageViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView message;
        TextView time;

        public TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.messageAuthorId);
            this.message = (TextView) itemView.findViewById(R.id.messageTextId);
            this.time = (TextView) itemView.findViewById(R.id.textTimeId);
        }
    }

    public static class ImageMessageViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        ImageView image;
        TextView time;

        public ImageMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.imageAuthorId);
            this.image = (ImageView) itemView.findViewById(R.id.imageId);
            this.time = (TextView) itemView.findViewById(R.id.imageTimeId);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch(viewType) {
            case ChatMessage.TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_row, parent, false);
                return new TextMessageViewHolder(view);
            case ChatMessage.IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_image_row, parent, false);
                return new ImageMessageViewHolder(view);
            default:
                throw new RuntimeException("Chat message type not recognized");
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*make requests here*/

        return getMessage(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = getMessage(position);
        if(message != null) {
            switch(message.getType()) {
                case ChatMessage.TEXT:
                    ((TextMessageViewHolder) holder).author.setText(message.getAuthor());
                    ((TextMessageViewHolder) holder).message.setText(message.getContent());
                    ((TextMessageViewHolder) holder).time.setText(message.getTimestamp());
                    break;
                case ChatMessage.IMAGE:
                    ((ImageMessageViewHolder) holder).author.setText(message.getAuthor());
                    byte[] decodedString = Base64.decode(message.getContent(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ((ImageMessageViewHolder) holder).image.setImageBitmap(decodedByte);
                    ((ImageMessageViewHolder) holder).time.setText(message.getTimestamp());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        var count = Data.getJoinedChatrooms().getChatrooms().get(chatId);
        return Objects.requireNonNullElse(count, 0);
    }
}
