package com.humanhorsebell.computer.hansung_calendar_manager


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_join.*
import android.content.Intent
import android.view.View
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by lovel on 2019-10-21.
 */
class JoinActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val database = firebaseReference.reference
    var overlap:Boolean = false

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

        cancelbtn.setOnClickListener {
            finish()
        }

        //회원가입
        joinbtn.setOnClickListener {
            if(overlap){
                //비번같은지 확인
                if(edittextNewPW1.text.toString()==edittextNewPW2.text.toString()){
                    //온점 오류!!!!.
                    //이메일를 key로 이름 넣어주기
                    database.child(editTextNewID.text.toString()).child("name").setValue(editTextName.text.toString())
                    //비번넣어주기
                    database.child(editTextNewID.text.toString()).child("password").setValue(edittextNewPW1.text.toString())
                    //생일넣어주기
                    database.child(editTextNewID.text.toString()).child("birth").setValue(editTextName.text.toString())
                }
                else{
                    //비번두개가 동일하지않아.
                    Toast.makeText(getApplicationContext(), "비번두개 다르다", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                //아이디 중복확인바람
                Toast.makeText(getApplicationContext(), "아이디 중복 확인해봐", Toast.LENGTH_SHORT).show()
            }
        }

//        //intent에서 rootPath받아오기
//        val intent = Intent(intent)
//        rootPath = intent.getStringExtra("rootPath")

        //생일 DatePicker사용
        birthcheckbtn.setOnClickListener {
            val newFragment = BirthCheckFragment()   //DatePickerFragment 객체 생성
            newFragment.show(getSupportFragmentManager(), "BirthCheck")                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
        }

    }

    val checkRegister = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            this@JoinActivity.overlap = true
            for(child in dataSnapshot.children) {
                if(child.key.toString().equals(editTextNewID.text.toString())) {
                    this@JoinActivity.overlap = false
                }
            }
            //val myRef : DatabaseReference = databaseReference.getReference("message")

            //databaseReference.child(editTextNewID.text.toString())
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }
//    //리스너들
//    val listener = object : View.OnClickListener{
//        override fun onClick(v: View) {
//            when(v.id) {
//                //중복체크
//                R.id.checkOverlapBtn->CheckIDOverlap()
//                //취소
//
//                //가입
//            }
//        }
//    }


//    fun CheckIDOverlap() {
//        var newid = editTextNewID.text.toString()
//        for (i in 0 until userDatabase.size()) {
//            //아이디 중복
//            if (userDatabase.get(i).isMeByName(newid)) {
//                checkoverlap = false //중복임
//            } else {
//                checkoverlap = true //중복아님 사용가능
//            }
//        }
//        if (checkoverlap) {//아이디 중복아님
//            message.information(this@Registration, "알림", "사용가능한 아이디입니다.")
//        } else {
//            message.information(this@Registration, "경고", "아이디 중복입니다. 사용하실 수 없습니다.")
//        }
//    }
//
//    //회원가입
//    fun CreateUser() {
//        newpw = edittextNewPW.getText().toString() //새 비밀번호 받아오기
//        newpwc = edittextnewPWC.getText().toString() //새 비밀번호 확인 받아오기
//        //아이디 중복검사가 통과인지 확인
//        if (checkoverlap) {
//            //비밀번호와 비밀번호 확인 같을 때
//            if (newpw.equals(newpwc)) {
//                //새로운 유저 만들기
//                user = User(newid, newpw)
//                //새로운 유저 database에 넣기
//                userDatabase.add(user)
//                databaseBroker.saveUserDatabase(this@Registration, userDatabase)
//                message.information(this@Registration, "알림", "회원가입 되었습니다.")
//                finish()
//            } else {
//                //비밀번호와 비밀번호 확인 다를때 => 경고 : 비밀번호가 동일하지 않습니다.
//                message.information(this@Registration, "경고", "비밀번호가 동일하지 않습니다.")
//            }
//        } else {
//            message.information(this@Registration, "경고", "아이디 중복검사를 확인하십시오.")
//        }//아이디 중복검사 통과 못함
//    }

//
//
//

//

//
//        //가입누르면 가입해줘
//        joinbtn.setOnClickListener {
//            //이름가져와서 루트에 달기
//
//            //이름단곳에 이메일, 생일, 비번 넣어주기
//
//            //firebase에 저장
//
//        }
}
