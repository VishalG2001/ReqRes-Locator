package com.example.apiintegration.ui
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiintegration.databinding.ActivityHomeBinding
import com.example.apiintegration.models.User
import com.example.apiintegration.models.UserResponse
import com.example.apiintegration.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var users: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(emptyList())
        binding.rvUsers.adapter = userAdapter

        fetchUsers()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUsers(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnCurrentLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "logging out", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this,MainActivity::class.java)
            startActivity(intent2)
            finish()
        }
    }

    private fun fetchUsers() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUsers()
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    users = response.body()!!.data
                    userAdapter.updateUsers(users)
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterUsers(query: String) {
        val filteredUsers = users.filter {
            it.first_name.contains(query, ignoreCase = true) ||
                    it.last_name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true)
        }
        userAdapter.updateUsers(filteredUsers)
    }
}