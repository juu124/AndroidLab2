package com.example.ch1.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch1.databinding.RecyclerFragmentListBinding
import java.text.SimpleDateFormat
import java.util.Date

class ListTodoFragment : Fragment() {
    lateinit var binding: RecyclerFragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            RecyclerFragmentListBinding.inflate(
                LayoutInflater.from(activity), container, false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addBtn.setOnClickListener {
            // addTodoFragment가 화면에 나오게 하고 싶다.
            // FragmentTransaction을 이용해야한다. activity에서만 작성할 수 있다.
            // activity 함수 호출해서 fragment에서 조정되게할 수 있다.
            (activity as TodoActivity).goAddFragment()      // activity에 커스텀 함수 만들기
        }
        // 목록을 구성하기 위한 데이터 획득
        val itemList = setRecyclerViewData(activity as Context)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = TodoAdapter(activity as TodoActivity, itemList)
        binding.recyclerView.addItemDecoration(TodoDecoration(itemList))
    }

    fun setRecyclerViewData(context: Context): MutableList<Item> {
        // db data 획득
        val dbList = selectTodos(context)

        // 화면에 찍기 위한 데이터 List
        val itemList = mutableListOf<Item>()

        var preDate: String? = null
        for (vo in dbList) {
            if (!vo.data.equals(preDate)) {
                // 이전 데이터과 날짜가 다르다면
                // 날짜 항목이 추가되어야 한다.
                val headerItem = HeaderItem(
                    date = SimpleDateFormat("yyyy년 MM월 dd일").format(Date(vo.data.toLong()))
                )
                itemList.add(headerItem)
                preDate = vo.data
            }
            val dataItem = DataItem(
                id = vo.id,
                title = vo.title,
                content = vo.content,
            )
            itemList.add(dataItem)
        }
        return itemList
    }
}