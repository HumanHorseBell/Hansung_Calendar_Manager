package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import android.R.*
/**
 * Created by lovel on 2019-10-21.
 */
class LoginActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val database = firebaseReference.reference.child("user")
    var findEmail = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        creatuserbtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        //현재 로그인시 바로 그룹추가 fragment사용-> 나중에 바꾸기
        loginbtn.setOnClickListener {
            database.addListenerForSingleValueEvent(checkLogin)


        }
    }
    //로그인 확인
    //email있는지 확인
    val checkLogin = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (child in dataSnapshot.children) {
                //이멜있나
                if (child.child("email").value.toString().equals(editTextID.text.toString())) {
                    findEmail = true
                    //비번맞으면
                    if(child.child("passwd").value.toString().equals(editTextPW.text.toString())){


                        //맞으면 그룹원추가로. -> 나중에 바꾸기
                        val intent2 = Intent(this@LoginActivity, Add_Category::class.java)
                        //기본키넘김=>얘로 나중에 사용자누군지 계속 구분해야지
                        intent2.putExtra("userNo",child.key.toString())
                        startActivity(intent2)

                    }
                    //비번틀리면
                    else{
                        Dialogmessage(this@LoginActivity,"경고","비밀번호가 틀렸어요.")
                    }
                }

            }
            //이멜 못찾음
            if(!findEmail){
                Dialogmessage(this@LoginActivity,"경고","없는 이메일이예요.")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    }
}