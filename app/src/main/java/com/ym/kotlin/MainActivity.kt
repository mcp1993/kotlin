package com.ym.kotlin

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ym.kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.security.auth.Subject

@BindingMethods(BindingMethod(type = AppCompatImageView::class, attribute = "image", method = "setImageDrawable"))

class MainActivity : AppCompatActivity() {




    private val vm:VmOne by ViewModelLazy<VmOne>(VmOne::class
        ,{viewModelStore},{defaultViewModelProviderFactory})

    val vm1:VmOne by viewModels<VmOne>{defaultViewModelProviderFactory}

//    val vmTwo :VmTwo by viewModels<VmTwo> { Vmfactory(application) }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModelStore onCreate才创建
        val vm2:VmTwo = ViewModelProvider(viewModelStore,defaultViewModelProviderFactory).get(VmTwo::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val liveA = MutableLiveData<String>()
        liveA.value = "3"
        val liveB = MediatorLiveData<String>()



        val liveObserver = Observer<String>{
            it?.let {

            }
        }
    }

    private fun test() = runBlocking {
        val a1 = async {
            delay(2000)
            100
        }
        a1.join()
        a1.await()
    }

    private suspend fun geta1(){
        delay(2000)
    }


}



