/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.content.Intent
 *  android.media.MediaPlayer
 *  android.media.MediaPlayer$OnPreparedListener
 *  android.os.Bundle
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.Button
 *  androidx.appcompat.app.AppCompatActivity
 *  com.blogspot.atifsoftwares.animatoolib.Animatoo
 *  com.example.shadowapp.R$id
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package com.example.shadowapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.shadowapp.ControllerPage;
import com.example.shadowapp.MainActivity;
import com.example.shadowapp.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0015R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lcom/example/shadowapp/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "mediaPlayer", "Landroid/media/MediaPlayer;", "handleTouch", "", "event", "Landroid/view/MotionEvent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class MainActivity
extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private HashMap _$_findViewCache;

    @SuppressLint(value={"ClickableViewAccessibility"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131558428);
        MediaPlayer mediaPlayer = this.mediaPlayer = MediaPlayer.create((Context)((Context)this), (int)2131755008);
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener)onCreate.1.INSTANCE);
        }
        ((Button)this._$_findCachedViewById(R.id.getReady)).setOnTouchListener(new View.OnTouchListener(this){
            final /* synthetic */ MainActivity this$0;

            public final boolean onTouch(View $noName_0, MotionEvent event) {
                MotionEvent motionEvent = event;
                Intrinsics.checkExpressionValueIsNotNull((Object)motionEvent, (String)"event");
                MainActivity.access$handleTouch(this.this$0, motionEvent);
                Animatoo.animateSplit((Context)((Context)this.this$0));
                this.this$0.startActivity(new Intent((Context)this.this$0, ControllerPage.class));
                return true;
            }
            {
                this.this$0 = mainActivity;
            }
        });
    }

    private final void handleTouch(MotionEvent event) {
        switch (event.getAction()) {
            case 0: {
                String string = "Car horn shrieks!";
                boolean bl = false;
                System.out.println((Object)string);
                Thread.sleep(15L);
                MediaPlayer mediaPlayer = this.mediaPlayer;
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
                Thread.sleep(800L);
                MediaPlayer mediaPlayer2 = this.mediaPlayer;
                if (mediaPlayer2 != null) {
                    mediaPlayer2.pause();
                }
                string = "Car horn shut up.";
                bl = false;
                System.out.println((Object)string);
                break;
            }
        }
    }

    public static final /* synthetic */ void access$handleTouch(MainActivity $this, MotionEvent event) {
        $this.handleTouch(event);
    }

    public View _$_findCachedViewById(int n) {
        View view;
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        if ((view = (View)this._$_findViewCache.get(n)) == null) {
            view = this.findViewById(n);
            this._$_findViewCache.put(n, view);
        }
        return view;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }
}
