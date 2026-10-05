package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  FirebaseFirestore db;

  Button btAdd, btShow;

  EditText etTitle;
  EditText etContent;
  EditText etImgCover;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EdgeToEdge.enable(this);

    setContentView(R.layout.activity_main);

    ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            (v, insets) -> {

              Insets systemBars =
                      insets.getInsets(
                              WindowInsetsCompat.Type.systemBars()
                      );

              v.setPadding(
                      systemBars.left,
                      systemBars.top,
                      systemBars.right,
                      systemBars.bottom
              );

              return insets;
            }
    );

    // Khởi tạo Firebase
    FirebaseApp.initializeApp(this);

    // Kết nối Firestore
    db = FirebaseFirestore.getInstance();

    // Ánh xạ Button
    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);

    // Ánh xạ EditText
    etTitle = findViewById(R.id.etTitle);
    etContent = findViewById(R.id.etContent);
    etImgCover = findViewById(R.id.etImgCover);

    // Bắt sự kiện
    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {

    if (view.getId() == R.id.btAdd) {

      String title =
              etTitle.getText().toString().trim();

      String content =
              etContent.getText().toString().trim();

      String imgCover =
              etImgCover.getText().toString().trim();

      int initialView = 0;

      // Kiểm tra tiêu đề
      if (title.isEmpty()) {

        Toast.makeText(
                this,
                "Vui lòng nhập tiêu đề",
                Toast.LENGTH_SHORT
        ).show();

        return;
      }

      // Tạo Article
      Article newArticle =
              new Article(
                      title,
                      content,
                      imgCover,
                      initialView
              );

      // Thêm vào Firestore
      db.collection("articles")
              .add(newArticle)
              .addOnSuccessListener(documentReference -> {

                Toast.makeText(
                        MainActivity.this,
                        "Thêm bài viết thành công!",
                        Toast.LENGTH_SHORT
                ).show();

                // Xóa dữ liệu sau khi thêm
                etTitle.setText("");
                etContent.setText("");
                etImgCover.setText("");
              })
              .addOnFailureListener(e -> {

                Toast.makeText(
                        MainActivity.this,
                        "Lỗi: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
              });

    } else if (view.getId() == R.id.btShow) {

      Intent intent =
              new Intent(
                      MainActivity.this,
                      ShowDataActivity.class
              );

      startActivity(intent);
    }
  }
}