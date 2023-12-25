package lic.swift.button;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FunctionButton functionButton = findViewById(R.id.function_button);
        functionButton.setText("查询固件版本更新");
        functionButton.setTextSizeSP(20, this);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setStartDelay(50);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(animation -> {
            float current = (float) animation.getAnimatedValue();
            functionButton.setText("正在下载 " + ((int) (current * 100)) + "%...");
            functionButton.setProgress(current);
            if(current == 1F)
                functionButton.setText("下载完成");
        });

        findViewById(R.id.start).setOnClickListener(v -> valueAnimator.start());
        findViewById(R.id.finish).setOnClickListener(v -> valueAnimator.cancel());
    }
}
