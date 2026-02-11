package com.example.ch1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch1.databinding.FragmentOneBinding

class OneFragment : Fragment() {
    // 프래그먼트의 화면을 결정하기 위해서 자동 호출된다.
    // 이 함수의 리턴 View가 프래그먼트의 화면이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOneBinding.inflate(inflater, container, false)
        val datas = mutableListOf<String>()
        for (i in 1..20) {
            datas.add("Item $i")
        }
        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.adapter = MyAdapter(datas)
        binding.recycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        return binding.root
    }
}