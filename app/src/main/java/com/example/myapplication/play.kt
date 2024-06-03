package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.os.IResultReceiver._Parcel
import android.support.v4.os.IResultReceiver2
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class play : AppCompatActivity() {
    private lateinit var btnplay: Button
    private lateinit var editname1 : EditText
    private lateinit var editname2 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)




            btnplay= findViewById<Button>(R.id.game)
            editname1=findViewById<EditText>(R.id.editTextText)
            editname2=findViewById<EditText>(R.id.editTextText2)
            btnplay.setOnClickListener {



                    val artDialogBuilder= AlertDialog.Builder(this@play)


                    artDialogBuilder.setMessage("First Turn: Pick any tile, gain 3 points for it.\n" +
                            "Later Turns: Only click your own colored tiles, gain 1 point each turn.\n" +
                            "Conquer at 4 Points:\n" +
                            "Your color spreads to surrounding tiles (up/down/left/right).\n" +
                            "Each surrounding tile gains 1 point of your color.\n" +
                            "Conquer opponent's color if it touches, turning it to yours.\n" +
                            "Repeat: Conquer and expand until you eliminate all opponent's colors.\n" +
                            "Goal: Eliminate opponent's color to win.\n"+
                            "Do yo want to proceed?")

                artDialogBuilder.setTitle("rules :")

                artDialogBuilder.setCancelable(false)
                artDialogBuilder.setPositiveButton("yes"){ dialog, which->
                    val Intent = Intent(this,game::class.java)
                    Intent.putExtra("Player 1",editname1.text.toString())
                    Intent.putExtra("Player 2",editname2.text.toString())
                    startActivity(Intent)
                   // Toast.makeText(, "you clicked yes", Toast.LENGTH_SHORT).show()
                }
                artDialogBuilder.setNegativeButton("no"){ dialog, which->
                    dialog.dismiss()
                }

                val alertDialog:AlertDialog= artDialogBuilder.create()
                alertDialog.show()
            }
        }
    }
