package ninja.stavola.friendcaster.view.holder;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;

@SuppressWarnings("unused")
public class BottomSheetViewHolder {
    private Context context;

    public String episodeMediaFileUrl;
    public String episodeMediaMime;

    public String fileName;

    public String linkToEpisode;

    private final Intent intent;

    public BottomSheetViewHolder(View view, Context context) {
        ButterKnife.bind(this, view);
        this.context = context;

        intent = new Intent();
    }

    @OnClick(R.id.button_stream)
    void onClickStream() {
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(episodeMediaFileUrl), episodeMediaMime);
        context.startActivity(intent);
    }

    @OnClick(R.id.button_download)
    void onClickDownload() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(episodeMediaFileUrl));

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    @OnClick(R.id.button_cast)
    void onClickCast() {
        //TODO: Actually cast to a device
    }

    @OnClick(R.id.button_browser)
    void onClickBrowser() {
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkToEpisode));
        context.startActivity(intent);
    }
}
