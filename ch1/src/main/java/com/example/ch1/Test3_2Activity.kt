package com.example.ch1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch1.databinding.ActivityTest32Binding
import com.example.ch1.databinding.RecyclerItemBinding
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

        // case - notifyXXX 함수 이용하는 방법
        holder.binding.updateButton.setOnClickListener {
            var newData = data[position].toInt()
            newData++
            data[position] = newData.toString()
            // 해당 position의 아이템 항목만 변경 요청한다.
            notifyItemChanged(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            data.removeAt(position)
            // 해당 항목이 삭제 되었을을 알려준다.
            notifyItemRemoved(position)

            // 그 하위에 있는 항목들의 위치가 변경되었음을 알려줘야한다.
            notifyItemRangeChanged(position, data.size - position)
        }
    }

    override fun getItemCount() = data.size
}