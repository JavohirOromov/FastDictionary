package com.example.fastdictionary.screen.splash;
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fastdictionary.R
import com.example.fastdictionary.databinding.FragmentSplashBinding

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 29/03/25
 * Javohir's MacBook Air
 */
class SplashFragment: Fragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        object: CountDownTimer(1500,1000){

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
            }
        }.start()
    }
}