package com.example.ch1.todo

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ch1.databinding.RecyclerFragmentAddBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddTodoFragment : Fragment() {
    lateinit var binding: RecyclerFragmentAddBinding

    // fragment 화면 준비
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            RecyclerFragmentAddBinding.inflate(LayoutInflater.from(activity), container, false)
        return binding.root
    }

    // onCreateView 에서 준비한 화면(View) 핸들링 작업.
    // 구체적으로 해당 view에 어떤 작업을 할것인지
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 시간, 날짜를 받아올건데 시간은 시스템 현재 시간으로 가져온다
        binding.addDateView.text = SimpleDateFormat("yyyy년 MM월 dd일").format(Date())

        binding.addDateView.setOnClickListener {
            // 날짜 조정
            // 년, 월, 일 획득해서 dialog에 지정한다.
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(activity as Context, { _, year, month, day ->
                binding.addDateView.text = "${year}년 ${month + 1}월 ${day}일"
            }, year, month, day).show()
        }

        binding.addBtn.setOnClickListener {
            if (!binding.addTitleEditView.text?.toString().equals("") &&
                !binding.addContentEditView.text?.toString().equals("")
            ) {
                // 유효성 검증 후 db insert하기
                val values = ContentValues().apply {
                    put("title", binding.addTitleEditView.text.toString())
                    put("content", binding.addContentEditView.text.toString())
                    put(
                        "date", SimpleDateFormat("yyyy년 MM월 dd일").parse(
                            binding.addDateView.text.toString()
                        ).time
                    )
                    put("completed", 0)
                }
                insertTodo(activity as Context, values)

                // 보통 자동으로 화면이 이전 화면으로 전환된다.
                // fragment back stack 에서 pop 해서 이전 화면으로 돌아간다.
                // fragment에서 activity는 fragment를 출력시킨 activity 자체를 지칭한다.
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }
}

