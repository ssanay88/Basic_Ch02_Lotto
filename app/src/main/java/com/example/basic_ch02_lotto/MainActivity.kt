package com.example.basic_ch02_lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.basic_ch02_lotto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainbinding : ActivityMainBinding

    private var didRun = false    // 번호가 생성되었는지 판단

    private val pickNumberSet = hashSetOf<Int>()    // 중복되는 번호가 추가되지 않도록 막아준다

    // 뽑은 번호 6개를 담을 리스트
    private val numberTextViewList: List<TextView> by lazy {

        listOf(
            mainbinding.number1,
            mainbinding.number2,
            mainbinding.number3,
            mainbinding.number4,
            mainbinding.number5,
            mainbinding.number6
        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)

        mainbinding.numberPicker.minValue = 1
        mainbinding.numberPicker.maxValue = 45

        initRunBtn()
        initAddBtn()

    }

    // 자동 생성 버튼
    private fun initRunBtn() {
        mainbinding.runBtn.setOnClickListener {
            val list = getRandomNumber()

            Log.d("MainActivity", list.toString())
        }
    }

    // 랜덤 번호를 6개 정렬 시켜서 가져오는 함수
    private fun getRandomNumber() : List<Int> {

        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    this.add(i)
                }
            }

        numberList.shuffle()

        val newList = numberList.subList(0,6)

        return newList.sorted()

    }

    // numberPicker에서 선택한 수를 로또 번호로 추가하는 함수
    private fun initAddBtn() {
        mainbinding.addBtn.setOnClickListener {

            // 모든 번호가 나와있는 경우 조건 확인
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener    // 리턴 시 setOnClickListener만 리턴
            }

            // 6개 초과로 번호를 추가하려는 경우 조건 확인
            if (pickNumberSet.size >= 6) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이미 선택된 번호일 경우 조건 확인
            if (pickNumberSet.contains(mainbinding.numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 들어가야할 텍스트 뷰의 자리를 구해준다
            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true    // 가시 상태를 true로 변경
            textView.text = mainbinding.numberPicker.value.toString()    // 해당 텍스트 뷰를 선택한 수로 변경

            pickNumberSet.add(mainbinding.numberPicker.value)    // 선택된 번호를 담는 리스트에 추가

        }
    }


}

/*

모듈화가 중요!! -> 버튼 클릭에 해당하는 이벤트를 함수로 만들어서 사용, 나는 이때가지 onCreate() 함수안에 그냥 생성
by lazy 사용에 주의


*/