package com.example.tictactoegame

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoegame.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOfflineBtn.setOnClickListener{
            createOfflineGame()
        }

        binding.createOnlineGameBtn.setOnClickListener{
            createOnlineGame()
        }

        binding.joinOnlineGameBtn.setOnClickListener{
            joinOnlineGame()
        }


    }
    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()
    }

    fun createOnlineGame(){
        GameData.myId = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000,10000).toString()
            )
        )
        startGame()
    }

    fun joinOnlineGame(){
        var gameId = binding.inputGameId.text.toString()
        if (gameId.isEmpty()){
            binding.inputGameId.setError("Please Enter Game ID")
            return
        }
        GameData.myId="O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model = it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.inputGameId.setError("Please Enter Valid Game ID")
                }
                else{
                    model.gameStatus = GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startGame()
                }
            }
    }

    fun startGame(){
        startActivity(Intent(this,GameActivity::class.java))
    }

    }
