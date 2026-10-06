package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtView;
    private ImageView imgCover;

    private ArticleViewAdapter adapter;

    public ArticleViewHolder(
            @NonNull View itemView,
            ArticleViewAdapter adapter
    ) {
        super(itemView);

        this.adapter = adapter;

        txtTitle = itemView.findViewById(R.id.txt_title);
        txtContent = itemView.findViewById(R.id.txt_content);
        txtView = itemView.findViewById(R.id.txt_view);
        imgCover = itemView.findViewById(R.id.img_cover);

        itemView.setOnClickListener(v -> {

            int position = getAdapterPosition();

            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            Article article =
                    adapter.getArticles().get(position);

            String articleId = article.getId();

            // Kiểm tra ID
            if (articleId == null || articleId.trim().isEmpty()) {
                return;
            }

            articleId = articleId.trim();

            Context context = v.getContext();

            // Tăng lượt xem trên Firebase
            FirebaseFirestore.getInstance()
                    .collection("articles")
                    .document(articleId)
                    .update(
                            "view",
                            FieldValue.increment(1)
                    );

            // Mở DetailActivity
            Intent intent = new Intent(
                    context,
                    DetailActivity.class
            );

            // Gửi ĐÚNG document ID
            intent.putExtra(
                    "article_id",
                    articleId
            );

            context.startActivity(intent);
        });
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public TextView getTxtContent() {
        return txtContent;
    }

    public TextView getTxtView() {
        return txtView;
    }

    public ImageView getImgCover() {
        return imgCover;
    }
}