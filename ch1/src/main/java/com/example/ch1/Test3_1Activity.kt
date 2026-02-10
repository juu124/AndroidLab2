package com.example.ch1

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch1.databinding.ActivityTest31Binding
import com.example.ch1.databinding.RecyclerItemBinding

class Test3_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest31Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val datas = mutableListOf<String>()
        for (i in 1..20) {
            datas.add("Item $i")
        }

        binding.main.adapter = MyAdapter(datas)
        binding.main.layoutManager = LinearLayoutManager(this)
        binding.main.addItemDecoration(MyDecoration(this))
    }
}

class MyViewHoler(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>) : RecyclerView.Adapter<MyViewHoler>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHoler {
        return MyViewHoler(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHoler, position: Int) {
        holder.binding.itemData.text = datas[position]
    }

    override fun getItemCount() = datas.size
}

class MyDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    // 항목이 출력되기 전, 최초 한번 호출한다.
    // 이곳에서 그린것 위에 항목이 배치된다.
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        // 그리고자 하는 이미지(bitmap)을 지정한다.
        // Canvas : drawaXXX() - 그리는 작업
        // Paint : 그리기 옵션 color, size
        c.drawBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.stadium),
            0f, 0f, null
        )
    }

    // 모든 항목이 출력된 후, 최초에 한번 항목 위에 그려진다.
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        // RecyclerView의 화면 사이즈 획득
        val width = parent.width
        val height = parent.height

        // 그리고자 하는 이미지 사이즈
        val dr = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        val drwidth = dr?.intrinsicWidth
        val drheight = dr?.intrinsicHeight

        // 이미지 그리기 위한 좌표 계산
        // 화면 center
        val left = width / 2 - drwidth?.div(2) as Int
        val top = height / 2 - drheight?.div(2) as Int
        // 이미지 그리기
        c.drawBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.kbo),
            left.toFloat(),
            top.toFloat(),
            null
        )
    }

    // 항목 하나당 한번씩
    // outRect - 항목 그리기 사각형 정보
    override fun getItemOffsets(
        outRect: Rect,
        view: View, // view객체를 사용해서 해당 데이터를 뽑아낼 수도 있다.
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // 몇 번째 항목을 위해서 호출된 것인지 획득한다.
        val index = parent.getChildAdapterPosition(view) + 1 // 의도 없이 계산 편하라고 +1한 것일뿐
        if (index % 3 == 0) {
            outRect.set(10, 10, 10, 60)
        } else {
            outRect.set(10, 20, 10, 0)
        }

        view.setBackgroundColor(Color.LTGRAY)
        ViewCompat.setElevation(view, 20.0f)
    }
}