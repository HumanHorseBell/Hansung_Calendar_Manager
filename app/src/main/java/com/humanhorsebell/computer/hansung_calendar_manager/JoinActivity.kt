package com.humanhorsebell.computer.hansung_calendar_manager


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_join.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class JoinActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val database = firebaseReference.reference.child("user")
    var overlap: Boolean = false
    var id: Int = 0 //현재 사용자 갯수 세어서 user기본키로 두려구
    var checkbitrh = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        //이멜 누르면 바꾼가능성이 있느니 다시 중복체크하도록
        editTextNewID.setOnClickListener {
            overlap = false
        }

        //아이디 중복체크
        checkOverlapBtn.setOnClickListener {
            database.addListenerForSingleValueEvent(checkRegister)
        }

        //취소다
        cancelbtn.setOnClickListener {
            finish()
        }

        //회원가입
        joinbtn.setOnClickListener {
            if (checkbitrh) {
                if (overlap) {
                    //비번같은지 확인
                    if (edittextNewPW1.text.toString() == edittextNewPW2.text.toString()) {
                        //kkey에 이메일 넣기
                        database.child("u"+id.toString()).child("email").setValue(editTextNewID.text.toString())
                        //이메일를 key로 이름 넣어주기
                        database.child("u"+id.toString()).child("name").setValue(editTextName.text.toString())
                        //비번넣어주기
                        database.child("u"+id.toString()).child("passwd").setValue(edittextNewPW1.text.toString())
                        //생일넣어주기
                        database.child("u"+id.toString()).child("birth").setValue(birthcheckbtn.text.toString())
                        //Toast.makeText(getApplicationContext(), id.toString() + " : 회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                        Dialogmessage(this@JoinActivity,"알림","회원가입 되었어요.", "close")
                        //finish()
                    } else {
                        //비번두개가 동일하지않아.
                        Dialogmessage(this@JoinActivity,"경고","비번 확인해주세요.")
                    }
                } else {
                    //아이디 중복확인바람
                    Dialogmessage(this@JoinActivity,"경고","이메일 중복 확인해주세요.")
                }
            } else {
                //생일알려줘
                Dialogmessage(this@JoinActivity,"경고","생일 체크해주세요.")
            }
        }

        //생일 DatePicker사용
        birthcheckbtn.setOnClickListener {
            val newFragment = BirthCheckFragment()   //DatePickerFragment 객체 생성
            newFragment.show(getSupportFragmentManager(), "BirthCheck") //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
            checkbitrh = true
        }
    }

    val checkRegister = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            this@JoinActivity.overlap = true
            for (child in dataSnapshot.children) {
                id = dataSnapshot.childrenCount.toInt()
                val email = editTextNewID.text.toString()
                if (child.child("email").value.toString().equals(email)) {
                    this@JoinActivity.overlap = false
                }
            }
            if (overlap) {
                Dialogmessage(this@JoinActivity,"알림","사용가능한 이메일이예요.")
            } else {
                Dialogmessage(this@JoinActivity,"경고","이미 있는 이메일이예요.")
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {}
    }
}
