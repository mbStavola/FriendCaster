package ninja.stavola.friendcaster.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ninja.stavola.friendcaster.R;

public class BottomSheetButtonView extends LinearLayout {
    @Bind(R.id.image)
    ImageView imageView;

    @Bind(R.id.text)
    TextView textView;

    public BottomSheetButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_button_bottom_sheet, this, true);
        ButterKnife.bind(this);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetButtonView);

        String text = a.getString(R.styleable.BottomSheetButtonView_text);
        Drawable image = a.getDrawable(R.styleable.BottomSheetButtonView_image);

        textView.setText(text);
        imageView.setImageDrawable(image);

        a.recycle();
    }
}
