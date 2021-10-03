package com.example.basic_ch02_lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        initClearBtn()

    }

    // 자동 생성 버튼
    private fun initRunBtn() {
        mainbinding.runBtn.setOnClickListener {
            val list = getRandomNumber()    // 랜덤으로 6자리 수를 불러온 리스트

            // 번호 생성 완료됐음을 설정
            didRun = true

            // 생성된 리스트 원소에 인덱스로 각각 접근하도록 함수 생성
            list.forEachIndexed { index, num ->
                val textView = numberTextViewList[index]    // 텍스트뷰를 원소로 가지는 리스트에 인덱스로 접근
                textView.text = num.toString()    // 각각 원소의 text를 list에서 해당 수로 변경
                textView.isVisible = true    // 보여주기 on
                setNumBackground(num,textView)
            }

            Log.d("MainActivity", list.toString())
        }
    }

    // 랜덤 번호를 6개 정렬 시켜서 가져오는 함수
    private fun getRandomNumber() : List<Int> {

        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    // 이미 선택된 번호는 제외하고 추가
                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }

        numberList.shuffle()

        // 사용자가 선택한 번호들이 담긴 리스트와 랜덤으로 추출한 수들을 합쳐준다
        val newList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)

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

            setNumBackground(mainbinding.numberPicker.value,textView)    // numberPicker에서 고른 수의 범위에 따른 배경색 지정 함수


            pickNumberSet.add(mainbinding.numberPicker.value)    // 선택된 번호를 담는 리스트에 추가

        }
    }

    private fun initClearBtn() {
        mainbinding.clearBtn.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false    // 각각 요소들의 가시성을 false로 설정하여 없애준다
            }

            didRun = false
        }
    }

    // 번호 범위에 따른 다른 배경색 지정 함수
    private fun setNumBackground(number:Int, textView: TextView) {

        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this,R.drawable.yellow_circle)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this,R.drawable.blue_circle)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this,R.drawable.red_circle)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this,R.drawable.gray_circle)
            else -> textView.background = ContextCompat.getDrawable(this,R.drawable.green_circle)
        }
    }


}

/*

모듈화가 중요!! -> 버튼 클릭에 해당하는 이벤트를 함수로 만들어서 사용, 나는 이때가지 onCreate() 함수안에 그냥 생성
by lazy 사용에 주의


*/