package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "ARTICLE_REALTIME";

    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtView;

    private ImageView imgCover;
    private Button btnBack;

    private FirebaseFirestore db;

    private String articleId;

    private ListenerRegistration listener;


    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);


        // =========================
        // VIEW
        // =========================

        txtTitle =
                findViewById(R.id.txtDetailTitle);

        txtContent =
                findViewById(R.id.txtDetailContent);

        txtView =
                findViewById(R.id.txtDetailView);

        imgCover =
                findViewById(R.id.imgDetailCover);

        btnBack =
                findViewById(R.id.btnBackDetail);


        // =========================
        // FIREBASE
        // =========================

        FirebaseApp.initializeApp(this);

        db =
                FirebaseFirestore.getInstance();


        // =========================
        // LẤY DOCUMENT ID
        // =========================

        articleId =
                getIntent().getStringExtra(
                        "article_id"
                );


        Log.d(
                TAG,
                "Document ID = " + articleId
        );


        // =========================
        // KIỂM TRA ID
        // =========================

        if (
                articleId == null
                        || articleId.trim().isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "Không có Document ID",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        articleId = articleId.trim();


        // =========================
        // BACK
        // =========================

        btnBack.setOnClickListener(
                v -> finish()
        );


        // =========================
        // REALTIME
        // =========================

        startRealtime();
    }


    private void startRealtime() {

        Log.d(
                TAG,
                "Bắt đầu nghe document: "
                        + articleId
        );


        listener =
                db.collection("articles")
                        .document(articleId)

                        .addSnapshotListener(
                                (snapshot, error) -> {

                                    // =========================
                                    // ERROR
                                    // =========================

                                    if (error != null) {

                                        Log.e(
                                                TAG,
                                                "Firebase error",
                                                error
                                        );

                                        return;
                                    }


                                    // =========================
                                    // DOCUMENT KHÔNG TỒN TẠI
                                    // =========================

                                    if (
                                            snapshot == null
                                                    || !snapshot.exists()
                                    ) {

                                        Log.d(
                                                TAG,
                                                "Document không tồn tại"
                                        );

                                        return;
                                    }


                                    // =========================
                                    // FIREBASE ĐÃ THAY ĐỔI
                                    // =========================

                                    Log.d(
                                            TAG,
                                            "Realtime update: "
                                                    + snapshot.getId()
                                    );


                                    updateUI(snapshot);
                                }
                        );
    }


    private void updateUI(
            DocumentSnapshot snapshot
    ) {

        // =========================
        // TITLE
        // =========================

        String title =
                snapshot.getString("title");

        if (title != null) {

            txtTitle.setText(title);
        }


        // =========================
        // CONTENT
        // =========================

        String content =
                snapshot.getString("content");

        if (content != null) {

            txtContent.setText(content);
        }


        // =========================
        // VIEW
        // =========================

        Long view =
                snapshot.getLong("view");

        if (view != null) {

            txtView.setText(
                    "Views: " + view
            );
        }


        // =========================
        // IMAGE
        // =========================

        String imageUrl =
                snapshot.getString("img_cover");


        if (
                imageUrl != null
                        && !imageUrl.trim().isEmpty()
        ) {

            Picasso.get()
                    .load(imageUrl.trim())
                    .placeholder(
                            android.R.drawable.ic_menu_gallery
                    )
                    .error(
                            android.R.drawable.ic_delete
                    )
                    .fit()
                    .centerCrop()
                    .into(imgCover);

        } else {

            imgCover.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }
    }


    @Override
    protected void onDestroy() {

        // Hủy listener
        if (listener != null) {

            listener.remove();

            listener = null;
        }

        super.onDestroy();
    }
}