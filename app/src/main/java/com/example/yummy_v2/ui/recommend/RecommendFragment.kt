package com.example.yummy_v2.ui.recommend

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseFragment
import com.example.yummy_v2.databinding.FragmentRecommendBinding

class RecommendFragment : BaseFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    private lateinit var recommendViewModel : RecommendViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recommendViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
            .get(RecommendViewModel::class.java)
        val a = recommendViewModel.getAll()
    }
}