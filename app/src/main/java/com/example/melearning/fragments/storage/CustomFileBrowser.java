package com.example.melearning.fragments.storage;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.melearning.R;
import java.io.File;
import java.util.ArrayList;

interface BrowserItemCallback {
    void OnClickedItem(String absolutePath);
    void OnClickedBack(String absolutePath);
}

class FileBrowserViewHolder extends RecyclerView.ViewHolder {

    public FileBrowserViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onBind(String path, BrowserItemCallback callback) {
        TextView filenameField = itemView.findViewById(R.id.fileBrowserItem);

        File file = new File(path);
        filenameField.setText(file.getName());

        filenameField.setOnClickListener(view -> {
            if(path.equals("...")) {
                callback.OnClickedBack(path);
            }
            else {
                callback.OnClickedItem(path);
            }
        });
    }
}


class FileBrowserAdapter
        extends RecyclerView.Adapter<FileBrowserViewHolder>
        implements BrowserItemCallback {

    private ArrayList<String> items;
    public File currentPath;
    private BrowserItemCallback callback;

    FileBrowserAdapter(BrowserItemCallback parentCallback) {
        this.callback = parentCallback;
        currentPath = Environment.getExternalStorageDirectory();
        recalculateItems();
    }

    @NonNull
    @Override
    public FileBrowserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_file_browser_item, parent, false);
        return new FileBrowserViewHolder(view);
    }

    private boolean isRootFolder() {
        return currentPath.equals(Environment.getExternalStorageDirectory());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void recalculateItems() {
        items = new ArrayList<>();
        File[] files = currentPath.listFiles(file ->
                file.isDirectory()
                || file.getAbsolutePath().endsWith(".dwg")
        );

        if(files == null) {
            throw new RuntimeException("File list is null!");
        }

        if(!isRootFolder()) {
            items.add("...");
        }

        for (File file : files) {
            items.add(file.getAbsolutePath());
        }

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FileBrowserViewHolder holder, int position) {
        holder.onBind(items.get(position), this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void OnClickedItem(String absolutePath) {
        currentPath = new File(absolutePath);
        if(currentPath.isDirectory()) {
            recalculateItems();
        }

        callback.OnClickedItem(currentPath.getAbsolutePath());
    }

    @Override
    public void OnClickedBack(String absolutePath) {
        currentPath = currentPath.getParentFile();
        recalculateItems();
        callback.OnClickedBack(currentPath.getAbsolutePath());
    }
}

public class CustomFileBrowser extends AppCompatActivity implements BrowserItemCallback {
    public static final String EXTRA_FILE_PATH = "android.intent.extra.CustomFileBrowser.filepath";
    private TextView browserPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_file_browser);
        RecyclerView recyclerView = findViewById(R.id.fileBrowser);
        browserPath = findViewById(R.id.fileBrowserPath);

        FileBrowserAdapter adapter = new FileBrowserAdapter(this);
        recyclerView.setAdapter(adapter);
        updateBrowserPath(adapter.currentPath.getAbsolutePath());
    }

    @Override
    public void OnClickedItem(String absolutePath) {
        File file = new File(absolutePath);
        if(file.isDirectory()) {
            updateBrowserPath(absolutePath);
        }
        else {
            Intent intent = new Intent();
            intent.setData(Uri.fromFile(file));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void OnClickedBack(String absolutePath) {
        updateBrowserPath(absolutePath);
    }

    public void updateBrowserPath(String absolutePath) {
        browserPath.setText(absolutePath);
    }

}