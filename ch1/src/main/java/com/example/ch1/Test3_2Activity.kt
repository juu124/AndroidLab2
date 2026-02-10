package com.example.ch1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch1.databinding.ActivityTest32Binding
import com.example.ch1.databinding.RecyclerItemUpdateDeleteBinding

class Test3_2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest32Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data = mutableListOf<String>()
        for (i in 1..20) {
            data.add("${i * 100}")
        }

        binding.main.adapter = MyAdapter2(data)
        binding.main.layoutManager = LinearLayoutManager(this)
        binding.main.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}

class MyViewHoler2(val binding: RecyclerItemUpdateDeleteBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter2(val data: MutableList<String>) : RecyclerView.Adapter<MyViewHoler2>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHoler2 {
        return MyViewHoler2(
            RecyclerItemUpdateDeleteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHoler2, position: Int) {
        holder.binding.itemData.text = data[position]

        // case1 - notifyXXX 함수 이용하여 리스트를 업데이트하는 방법
//        holder.binding.updateButton.setOnClickListener {
//            var newData = data[position].toInt()
//            newData++
//            data[position] = newData.toString()
//            // 해당 position의 아이템 항목만 변경 요청한다.
//            notifyItemChanged(position)
//        }
//        holder.binding.deleteButton.setOnClickListener {
//            data.removeAt(position)
//            // 해당 항목이 삭제 되었을을 알려준다.
//            notifyItemRemoved(position)
//
//            // 그 하위에 있는 항목들의 위치가 변경되었음을 알려줘야한다.
//            notifyItemRangeChanged(position, data.size - position)
//        }

        // case2 - DiffUtil객체를 만들어서 리스트를 업데이트하는 방법
        holder.binding.updateButton.setOnClickListener {
            if (position in  0 until data.size) {
                val currentData = data[position].toInt()
                val newValue = (currentData + 1).toString()

                // 기존에 있는 데이터를 복제본으로 만들기
                val newList = data.toMutableList()
                newList[position] = newValue

                setData(newList)
            }
        }

        holder.binding.deleteButton.setOnClickListener {
            if (position in 0 until data.size) {
                // 복재본 준비
                val newList = data.toMutableList()
                newList.removeAt(position)
                setData(newList)
            }
        }
    }

    override fun getItemCount() = data.size

    fun setData(newNumbers: MutableList<String>) {
        // 준비한 DiffUtil을 등록해야 한다.
        val diffCallback = MyDiffCallback(data, newNumbers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data.clear()
        data.addAll(newNumbers)
        diffResult.dispatchUpdatesTo(this)
    }
}

// notify 말고 다른 방법으로 리스트 업데이트 하는 방법
// 변경 사항을 판단하기 위해서 이용되는 클래스 준비
class MyDiffCallback(
    private val oldList: MutableList<String>,
    private val newList: MutableList<String>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    // 각 항목의 데이터 객체가 동일한지를 판단한다. true/false를 리턴한다.
    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        // 알고리즘은 개발자 임의의 알고리즘을 작성했다.
        // 두 객체가 동일한지만 비교하면 된다.
        // 이 실습에서는 각 항목이 문자열이라서 이런식으로 알고리즘을 작성했다.
        return oldItemPosition == newItemPosition && oldItemPosition < oldList.size && newItemPosition < newList.size
    }

    // 각 항목의 데이터가 동일한지를 판단한다.
    // 객체 내에 있는 데이터가 같은지를 판단한다.
    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}