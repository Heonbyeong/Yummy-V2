package com.example.yummy_v2.ui.home

import android.os.Bundle
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseActivity
import com.example.yummy_v2.databinding.ActivitySelectBinding

class SelectActivity : BaseActivity<ActivitySelectBinding>(R.layout.activity_select) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
    }
}