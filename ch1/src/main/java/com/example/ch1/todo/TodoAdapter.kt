package com.example.ch1.todo

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ch1.databinding.RecyclerItemHeaderBinding
import com.example.ch1.databinding.RecyclerItemMainBinding

// 항목 타입이 여러타입이다.
// 항목 데이터 최상위 타입
// 추상 클래스로 타입을 만들어 놓는다.
abstract class Item {
    abstract val type: Int

    companion object {
        const val TYPE_HEADER = 0       // 헤더 데이터
        const val TYPE_DATA = 1         // 데이터 타입
    }
}

data class HeaderItem(override val type: Int = TYPE_HEADER, var date: String) : Item()

data class DataItem(
    override val type: Int = TYPE_DATA,
    var id: Int,
    var title: String,
    var content: String,
    var completed: Boolean = false
) : Item()

// viewHolder - 항목을 구성하기 위한 뷰를 가지는 역할이다
// 항목의 타입이 두가지다. viewholder 두개를 준비해서 해본다.
class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)

class DataViewHolder(val binding: RecyclerItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class TodoAdapter(val activity: TodoActivity, val itemList: MutableList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == Item.TYPE_HEADER) {
            HeaderViewHolder(
                RecyclerItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            DataViewHolder(
                RecyclerItemMainBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        // 항목 데이터 획득
        val item = itemList[position]
        if (item.type == Item.TYPE_HEADER) {
            val viewHolder = holder as HeaderViewHolder
            val headerItem = item as HeaderItem
            viewHolder.binding.itemHeaderView.text = headerItem.date
        } else {
            val viewHolder = holder as DataViewHolder
            val dataItem = item as DataItem

            viewHolder.binding.itemTitleView.text = dataItem.title
            viewHolder.binding.itemContentView.text = dataItem.content

        }
    }

    override fun getItemCount() = itemList.size

    // recyclerView 내에 항목의 타입이 여러개인 경우
    // 해당 항목의 타입을 적절하게 결정해서 리턴시키면,
    // 리턴시킨 type Int가 onCreateViewHolder 의 두번째 매개변수에 전달된다.
    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }
}

class TodoDecoration(var itemList: MutableList<Item>) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view)
        val item = itemList[index]
        if (item.type == Item.TYPE_DATA) {
            view.setBackgroundColor(0xFFFFFFFF.toInt())
            ViewCompat.setElevation(view, 10.0f)
        }
        outRect.set(20, 10, 20, 10)
    }
}