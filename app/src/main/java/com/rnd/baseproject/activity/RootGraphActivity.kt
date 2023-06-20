package com.rnd.baseproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.rnd.baseproject.R
import com.rnd.baseproject.tools.fetchFcmId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootGraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_graph)
        fetchFcmId()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.fragmentContainerView),null)
    }
}