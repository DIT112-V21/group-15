/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 *  androidx.appcompat.app.AppCompatActivity
 *  com.blogspot.atifsoftwares.animatoolib.Animatoo
 *  com.example.shadowapp.R$id
 *  kotlin.Metadata
 *  org.jetbrains.annotations.Nullable
 */
package com.example.shadowapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.shadowapp.MainActivity;
import com.example.shadowapp.R;
import java.util.HashMap;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0014\u00a8\u0006\u0007"}, d2={"Lcom/example/shadowapp/ControllerPage;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class ControllerPage
extends AppCompatActivity {
    private HashMap _$_findViewCache;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131558429);
        ((Button)this._$_findCachedViewById(R.id.button3)).setOnClickListener(new View.OnClickListener(this){
            final /* synthetic */ ControllerPage this$0;

            public final void onClick(View it) {
                Animatoo.animateWindmill((Context)((Context)this.this$0));
                this.this$0.startActivity(new Intent((Context)this.this$0, MainActivity.class));
            }
            {
                this.this$0 = controllerPage;
            }
        });
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
